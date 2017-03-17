package shoplist.project.kz.mytodoli.utils;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import shoplist.project.kz.mytodoli.model.TodoItem;

/**
 * Created by Andrey on 3/15/2017.
 */

public class DeleteNotification extends IntentService{
    private RealmResults<TodoItem> realmResults;
    private TodoItem todoItem;
    private Realm realm;

    public DeleteNotification() {
        super("DeleteNotification");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        assert intent != null;
        int id = (int) intent.getExtras().get("idtodo");

        todoItem = realm.where(TodoItem.class).equalTo("id", id).findFirst();

    }
}
