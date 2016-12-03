package mackdev.mackpad;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        String title = "None";
        if (id == R.id.nav_calendar) {
            title = "Calendar";
            fragment = new calendar();
        } else if (id == R.id.nav_notes) {
            title = "Notes";
            fragment = new notes();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // set the toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void addNote(View view){
        EditText mEdit = (EditText) findViewById(R.id.editText);
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        EditText dateText = (EditText) findViewById(R.id.dateText);
        CheckBox alarmCheckbox = (CheckBox)findViewById(R.id.alarm);
        Boolean alarmState = alarmCheckbox.isChecked();
        int alarmValue = alarmState ? 1 : 0;
        values.put(DB.DBNotes.COLUMN_NAME_TEXT, mEdit.getText().toString());
        values.put(DB.DBNotes.COLUMN_NAME_DATE, dateText.getText().toString());
        values.put(DB.DBNotes.COLUMN_NAME_ALARM, alarmValue);
        if(alarmState){
            Intent alarmIntent = new Intent(view.getContext(), alarmReceiver.class);
            Calendar calendar = Calendar.getInstance();
//            String ymd = dateText.getText().toString() + " 09:00:00";
            String ymd = dateText.getText().toString();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            try{
                date = (Date)df.parse(ymd);
            } catch (ParseException e) {
                Log.d("Error", e.toString());
            }
            calendar.setTime(date);
            calendar.add(Calendar.HOUR, new Date().getHours());
            calendar.add(Calendar.SECOND, new Date().getSeconds());
            calendar.add(Calendar.MINUTE, new Date().getMinutes());
            calendar.add(Calendar.SECOND, 10);
            Log.d("Time", calendar.getTime().toString());
            alarmIntent.putExtra("Note", mEdit.getText().toString());
            PendingIntent pending_intent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            //Set the Alarm Manager
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);

        }
//        long newRowId = db.insert(DB.DBNotes.TABLE_NAME, null, values);
//        int number = db.delete(DB.DBNotes.TABLE_NAME, null, null);
//        Log.v("numberDeleted", Integer.toString(number));
    }
}
