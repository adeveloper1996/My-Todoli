package shoplist.project.kz.mytodoli.screen.add_todo;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmResults;
import shoplist.project.kz.mytodoli.R;
import shoplist.project.kz.mytodoli.model.TodoItem;
import shoplist.project.kz.mytodoli.screen.main.MainActivity;

public class AddTodoActivity extends AppCompatActivity implements AddTodoView,View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private AddTodoPresenter presenter;

    private FloatingActionButton fab;
    private EditText titleTodo;
    private EditText dayTodo;
    private EditText timeTodo;
    private SwitchCompat switComRemind;
    private LinearLayout userSetDateAndTime;
    private Realm realm;
    private boolean userHasReminder;
    private Date userRemindDate;
    private int userColor;
    private TodoItem todoItems;

    public static final String ADDTODOID = "addtodoid";
    private int id;

    public static void start(@NonNull Activity activity) {
        Intent intent = new Intent(activity, AddTodoActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        presenter = new AddTodoPresenter(this);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(0);

        initWidget();

        initRealm();
    }

    private void initRealm() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            id = (int) getIntent().getExtras().get(ADDTODOID);
            Log.i("ssss","id" + id);
            realm = Realm.getDefaultInstance();
            todoItems = realm.where(TodoItem.class).equalTo("id", id).findFirst();
            titleTodo.setText(todoItems.getTitle());
            userHasReminder = todoItems.isReminder();
            userColor = todoItems.getTodoColor();
            userRemindDate = todoItems.getToDoDate();

            if(userRemindDate !=  null && userHasReminder){
                switComRemind.setChecked(true);
                userSetDateAndTime.setVisibility(View.VISIBLE);
            }
        }else {
            randomColor();
        }
    }

    private void initWidget() {
        fab = (FloatingActionButton) findViewById(R.id.fab_add_todo);
        titleTodo = (EditText) findViewById(R.id.e_txt_title_todo);
        dayTodo = (EditText) findViewById(R.id.e_txt_todo_date);
        timeTodo = (EditText) findViewById(R.id.e_txt_todo_time);
        switComRemind = (SwitchCompat) findViewById(R.id.todo_has_date_switch_compat);
        userSetDateAndTime = (LinearLayout) findViewById(R.id.ll_date_and_time);

        fab.setOnClickListener(this);
        dayTodo.setOnClickListener(this);
        timeTodo.setOnClickListener(this);

        switComRemind.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userHasReminder = isChecked;
                presenter.setDateAndTime();
                setAnimate(isChecked);

            }
        });
    }

    @Override
    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }


    @Override
    public void setTimeAndDate() {
        if(userRemindDate != null){
            String userDate = formatDate("d MMM, yyyy", userRemindDate);
            String formatToUse;
            if(DateFormat.is24HourFormat(this)){
                formatToUse = "k:mm";
            }
            else{
                formatToUse = "h:mm a";

            }
            String userTime = formatDate(formatToUse, userRemindDate);
            timeTodo.setText(userTime);
            dayTodo.setText(userDate);

        }else {
            dayTodo.setText(R.string.today);
            boolean time24 = DateFormat.is24HourFormat(this);
            Calendar cal = Calendar.getInstance();
            if(time24){
                cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY)+1);
            }
            else{
                cal.set(Calendar.HOUR, cal.get(Calendar.HOUR)+1);
            }
            cal.set(Calendar.MINUTE, 0);
            userRemindDate = cal.getTime();
            String timeString;
            if(time24){
                timeString = formatDate("k:mm", userRemindDate);
            }
            else{
                timeString = formatDate("h:mm a", userRemindDate);
            }
            timeTodo.setText(timeString);
        }
    }

    @Override
    public void setDay(int year, int month, int day) {
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(AddTodoActivity.this,year, month, day);
        datePickerDialog.show(getFragmentManager(), "DateFragment");
    }

    @Override
    public void setTime(int hour, int minute) {
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(AddTodoActivity.this, hour, minute, DateFormat.is24HourFormat(AddTodoActivity.this));
        timePickerDialog.show(getFragmentManager(), "TimeFragment");
    }

    @Override
    public void showToast() {
        Toast.makeText(this,"title" , Toast.LENGTH_LONG).show();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_add_todo:
                presenter.clickFab(todoItems,titleTodo.getText().toString(),userHasReminder,userRemindDate,userColor);
                break;
            case R.id.e_txt_todo_date:
                presenter.setDay(userRemindDate);
                break;
            case R.id.e_txt_todo_time:
                presenter.setTime(userRemindDate);
                break;
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        setDateDialog(year, monthOfYear, dayOfMonth);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        setTimeDialog(hourOfDay, minute);
    }

    public static String formatDate(String formatString, Date dateToFormat){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatString);
        return simpleDateFormat.format(dateToFormat);
    }

    private void setTimeDialog(int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        if(userRemindDate!=null){
            calendar.setTime(userRemindDate);
        }

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(year, month, day, hourOfDay, minute, 0);
        userRemindDate = calendar.getTime();

        setReminderTextView();
        String dateFormat;
        if(DateFormat.is24HourFormat(this)){
            dateFormat = "k:mm";
        }
        else{
            dateFormat = "h:mm a";

        }
        timeTodo.setText(formatDate(dateFormat, userRemindDate));
    }

    private void setDateDialog(int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        int hour, minute;

        Calendar reminderCalendar = Calendar.getInstance();
        reminderCalendar.set(year, monthOfYear, dayOfMonth);

        if(reminderCalendar.before(calendar)){
            Toast.makeText(this, "My time-machine is a bit rusty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(userRemindDate!=null){
            calendar.setTime(userRemindDate);
        }

        if(DateFormat.is24HourFormat(this)){
            hour = calendar.get(Calendar.HOUR_OF_DAY);
        }
        else{

            hour = calendar.get(Calendar.HOUR);
        }
        minute = calendar.get(Calendar.MINUTE);

        calendar.set(year, monthOfYear, dayOfMonth, hour, minute);
        userRemindDate = calendar.getTime();
        setReminderTextView();
        String dateFormat = "d MMM, yyyy";
        dayTodo.setText(formatDate(dateFormat, userRemindDate));
    }

    private void setReminderTextView() {
    }

    public void setAnimate(boolean checked) {
        if(checked){
            setReminderTextView();
            userSetDateAndTime.animate().alpha(1.0f).setDuration(500).setListener(
                    new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            userSetDateAndTime.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }
                    }
            );
        }
        else{
            userSetDateAndTime.animate().alpha(0.0f).setDuration(500).setListener(
                    new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            userSetDateAndTime.setVisibility(View.INVISIBLE);
                            userRemindDate = null;
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    }
            );
        }
    }

    private void randomColor() {
        Random rnd = new Random();
        userColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
}
