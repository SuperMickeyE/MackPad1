package mackdev.mackpad;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Micah on 12/2/2016.
 */
public class alarmReceiver extends BroadcastReceiver{


    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        Intent mainActivity = new Intent(context, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context,0,mainActivity,0);
        Notification notification = new Notification.Builder(context)
                .setContentTitle("Alarm")
                .setSmallIcon(R.drawable.ic_alarm_black_24dp)
                .setContentText(intent.getStringExtra("Note"))
                .setContentIntent(pIntent)
                .build();
        notificationManager.notify(0,notification);
    }
}
