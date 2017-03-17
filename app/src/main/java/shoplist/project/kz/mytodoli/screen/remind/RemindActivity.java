package shoplist.project.kz.mytodoli.screen.remind;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import shoplist.project.kz.mytodoli.R;
import shoplist.project.kz.mytodoli.model.TodoItem;
import shoplist.project.kz.mytodoli.screen.main.MainActivity;
import shoplist.project.kz.mytodoli.utils.TodoNotification;

public class RemindActivity extends AppCompatActivity implements View.OnClickListener{
    private Spinner spinner;
    private Button btnRemove;
    private TextView txtTitle;
    private String title;
    private int id;
    private Realm realm;
    private TodoItem todoItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        initActionBar();

        initWidget();

        if (getIntent() != null){
            id = (int) getIntent().getExtras().get(TodoNotification.TODOID);
            Log.i("ssss",""+id);
            realm = Realm.getDefaultInstance();
            todoItem = realm.where(TodoItem.class).equalTo("id",id).findFirst();
            txtTitle.setText(todoItem.getTitle());
        }
    }

    private void initWidget() {
        spinner = (Spinner) findViewById(R.id.spinner_remind_snooze);
        btnRemove = (Button) findViewById(R.id.btn_remove_todo);
        txtTitle = (TextView) findViewById(R.id.txt_remind_title_todo);

        btnRemove.setOnClickListener(this);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.time_array)); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setElevation(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_remove_todo:
                realm.beginTransaction();
                todoItem.deleteFromRealm();
                realm.commitTransaction();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
    }

    private Date addTimeToDate(int mins){
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, mins);
        return calendar.getTime();
    }

    private int valueFromSpinner(){
        switch (spinner.getSelectedItemPosition()){
            case 0:
                return 10;
            case 1:
                return 15;
            case 2:
                return 30;
            case 3:
                return 60;
            default:
                return 0;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_remind, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_remind_done) {
            Date date = addTimeToDate(valueFromSpinner());
            realm.beginTransaction();
            todoItem.setToDoDate(date);
            todoItem.setReminder(true);
            realm.commitTransaction();
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
