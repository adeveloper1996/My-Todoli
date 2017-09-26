package shoplist.project.kz.mytodoli.screen.main;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;
import shoplist.project.kz.mytodoli.R;
import shoplist.project.kz.mytodoli.model.TodoItem;
import shoplist.project.kz.mytodoli.screen.add_todo.AddTodoActivity;
import shoplist.project.kz.mytodoli.utils.MyItemTouchHelper;
import shoplist.project.kz.mytodoli.utils.TodoNotification;

public class MainActivity extends AppCompatActivity{
    private RecyclerEmptyView recyclerView;
    private Realm realm;
    private RealmResults<TodoItem> realmResults;
    private TodoAdapter adapter;
    private ItemTouchHelper itemTouchHelper;
    private Boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTodoActivity.start(MainActivity.this);
//                startActivity(new Intent(MainActivity.this, AddTodoActivity.class));
            }
        });

        realm = Realm.getDefaultInstance();
        realmResults = realm.where(TodoItem.class).findAll();
        recyclerView = (RecyclerEmptyView)findViewById(R.id.recycler_todo);
        recyclerView.setEmptyView(findViewById(R.id.no_content));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if(realmResults.size() != 0){
            adapter = new TodoAdapter(realmResults,this);
            ItemTouchHelper.Callback callback = new MyItemTouchHelper(adapter);
            itemTouchHelper = new ItemTouchHelper(callback);
            itemTouchHelper.attachToRecyclerView(recyclerView);
            recyclerView.setAdapter(adapter);
            setAlarms();
        }

    }

    private void setAlarms(){
        if(realmResults != null){
            for(TodoItem item : realmResults){
                if(item.isReminder() && item.getToDoDate() != null){
                    if(item.getToDoDate().before(new Date())){
                        realm.beginTransaction();
                        item.setToDoDate(null);
                        realm.commitTransaction();
                        continue;
                    }

                    Intent i = new Intent(this, TodoNotification.class);
                    i.putExtra(TodoNotification.TODOID, item.getId());
                    i.putExtra(TodoNotification.TODOTEXT, item.getTitle());
                    createAlarm(i, item.getId(), item.getToDoDate().getTime());
                }
                Log.i("ssss","main_id" + item.getId());
            }
        }
    }

    private AlarmManager getAlarmManager(){
        return (AlarmManager)getSystemService(ALARM_SERVICE);
    }


    public void createAlarm(Intent i, int requestCode, long timeInMillis){
        AlarmManager alarmManager = getAlarmManager();
        PendingIntent pi = PendingIntent.getService(this,requestCode, i, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pi);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onBackPressed() {
        getIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Нажмите еще раз чтобы выйти", Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }
    }
}
