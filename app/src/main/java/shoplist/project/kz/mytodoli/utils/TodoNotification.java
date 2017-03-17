package shoplist.project.kz.mytodoli.utils;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import shoplist.project.kz.mytodoli.R;
import shoplist.project.kz.mytodoli.screen.remind.RemindActivity;

/**
 * Created by Andrey on 3/15/2017.
 */

public class TodoNotification extends IntentService {
    public static final String TODOTEXT = "todotexttitle";
    public static final String TODOID = "todoid";
    private String todoText;
    private int id;

    public TodoNotification() {
        super("TodoNotification");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        todoText = intent.getStringExtra(TODOTEXT);
        id = (int) intent.getSerializableExtra(TODOID);

        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Intent i = new Intent(this, RemindActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra(TodoNotification.TODOID, id);
        Intent deleteIntent = new Intent(this, DeleteNotification.class);
        deleteIntent.putExtra(TODOID, id);
        Notification notification = new Notification.Builder(this)
                .setContentTitle(todoText)
                .setSmallIcon(R.drawable.ic_done_all_white_24dp)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setDeleteIntent(PendingIntent.getService(this, id, deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT))
                .setContentIntent(PendingIntent.getActivity(this, id, i, PendingIntent.FLAG_UPDATE_CURRENT))
                .build();

        manager.notify(100, notification);
    }
}
