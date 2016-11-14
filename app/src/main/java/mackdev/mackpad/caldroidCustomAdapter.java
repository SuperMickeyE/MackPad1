package mackdev.mackpad;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import hirondelle.date4j.DateTime;

public class caldroidCustomAdapter extends CaldroidGridAdapter {

    public caldroidCustomAdapter(Context context, int month, int year,
                                       Map<String, Object> caldroidData,
                                       Map<String, Object> extraData) {
        super(context, month, year, caldroidData, extraData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View cellView = convertView;

        // For reuse
        if (convertView == null) {
            cellView = inflater.inflate(R.layout.custom_cell, null);
        }

        int topPadding = cellView.getPaddingTop();
        int leftPadding = cellView.getPaddingLeft();
        int bottomPadding = cellView.getPaddingBottom();
        int rightPadding = cellView.getPaddingRight();

        TextView tv1 = (TextView) cellView.findViewById(R.id.tv1);
        TextView tv2 = (TextView) cellView.findViewById(R.id.tv2);

        tv1.setTextColor(Color.BLACK);

        // Get dateTime of this cell
        DateTime dateTime = this.datetimeList.get(position);
        Resources resources = context.getResources();

        // Set color of the dates in previous / next month
        if (dateTime.getMonth() != month) {
            tv1.setTextColor(resources
                    .getColor(com.caldroid.R.color.caldroid_darker_gray));
        }

        boolean shouldResetDiabledView = false;
        boolean shouldResetSelectedView = false;

        // Customize for disabled dates and date outside min/max dates
        if ((minDateTime != null && dateTime.lt(minDateTime))
                || (maxDateTime != null && dateTime.gt(maxDateTime))
                || (disableDates != null && disableDates.indexOf(dateTime) != -1)) {

            tv1.setTextColor(CaldroidFragment.disabledTextColor);
            if (CaldroidFragment.disabledBackgroundDrawable == -1) {
                cellView.setBackgroundResource(com.caldroid.R.drawable.disable_cell);
            } else {
                cellView.setBackgroundResource(CaldroidFragment.disabledBackgroundDrawable);
            }

            if (dateTime.equals(getToday())) {
                cellView.setBackgroundResource(com.caldroid.R.drawable.red_border_gray_bg);
            }

        } else {
            shouldResetDiabledView = true;
        }

        // Customize for selected dates
        if (selectedDates != null && selectedDates.indexOf(dateTime) != -1) {
            cellView.setBackgroundColor(resources
                    .getColor(com.caldroid.R.color.caldroid_sky_blue));

            tv1.setTextColor(Color.BLACK);

        } else {
            shouldResetSelectedView = true;
        }

        if (shouldResetDiabledView && shouldResetSelectedView) {
            // Customize for today
            if (dateTime.equals(getToday())) {
                cellView.setBackgroundResource(com.caldroid.R.drawable.red_border);
            } else {
                cellView.setBackgroundResource(com.caldroid.R.drawable.cell_bg);
            }
        }
        tv1.setText("" + dateTime.getDay());
//        tv2.setText("Hi");
        ArrayList<String> notes = getNotes(dateTime.format("YYYY-MM-DD"));
        tv2.setText(notes.get(0));

        // Somehow after setBackgroundResource, the padding collapse.
        // This is to recover the padding
        cellView.setPadding(leftPadding, topPadding, rightPadding,
                bottomPadding);

        // Set custom color if required
        setCustomResources(dateTime, cellView, tv1);

        return cellView;
    }

    public ArrayList<String> getNotes(String date){
        DBHelper helper = new DBHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] projection = {
                DB.DBNotes._ID,
                DB.DBNotes.COLUMN_NAME_DATE,
                DB.DBNotes.COLUMN_NAME_TEXT
        };
        String selection = DB.DBNotes.COLUMN_NAME_DATE + " = ?";
        String[] selectionArgs = { date };
//        Log.v("Selection", selection);
//        Log.v("Date", date);
        Cursor c = db.query(
                DB.DBNotes.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        ArrayList<String> notes = new ArrayList<String>();
        if(c.moveToFirst()){
            Log.v("TEST TEXT:", "FIRE");
            String s = c.getString(c.getColumnIndex(DB.DBNotes.COLUMN_NAME_TEXT));
            String d = c.getString(c.getColumnIndex(DB.DBNotes.COLUMN_NAME_DATE));
            notes.add(s);
//            Log.v("DATE", d);
            while(c.moveToNext()){
                s = c.getString(c.getColumnIndex(DB.DBNotes.COLUMN_NAME_TEXT));
                notes.add(s);
            }
        } else {
            notes.add("");
        }
        c.close();
        db.close();
        return notes;
    }
}
