package shoplist.project.kz.mytodoli.app;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Andrey on 3/12/2017.
 */

public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
