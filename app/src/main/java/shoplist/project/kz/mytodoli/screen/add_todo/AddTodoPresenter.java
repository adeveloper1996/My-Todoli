package shoplist.project.kz.mytodoli.screen.add_todo;

import android.app.Activity;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;
import shoplist.project.kz.mytodoli.model.TodoItem;

/**
 * Created by Andrey on 3/11/2017.
 */

public class AddTodoPresenter {
    private final AddTodoView view;
    private Realm realm;

    public AddTodoPresenter(AddTodoView view) {
        this.view = view;
    }

    public void clickFab(TodoItem todoItems, String title, boolean userHasReminder, Date userDataReminder, int userColor){
        realm = Realm.getDefaultInstance();
        TodoItem todoItem;
        if(title.equals("")){
            view.showToast();
        }else {
            if (todoItems == null){
                todoItem = new TodoItem();
                Log.i("ssss","123475");
                todoItem.setId((int) (1 + System.currentTimeMillis()));
                todoItem.setReminder(userHasReminder);
                todoItem.setTitle(title);
                todoItem.setToDoDate(userDataReminder);
                todoItem.setTodoColor(userColor);
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(todoItem);
                realm.commitTransaction();
            }
            else {
                todoItem = todoItems;
                Log.i("ssss","" + 113 + todoItem.getId());
                realm.beginTransaction();
                todoItem.setReminder(userHasReminder);
                todoItem.setTitle(title);
                todoItem.setToDoDate(userDataReminder);
                todoItem.setTodoColor(userColor);
//                if (!userHasReminder){
//                    todoItem.setToDoDate(null);
//                }else {
//                    if (userDataReminder != null){
//                        todoItem.setToDoDate(userDataReminder);
//                    }
//                }
                realm.commitTransaction();
            }

            view.openMainActivity();
        }
    }

    public void setDateAndTime(){
        view.setTimeAndDate();
    }

    public void setDay(Date toDoDate){
        Date date;
        if(toDoDate!=null){
            date = toDoDate;
        }
        else{
            date = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        view.setDay(year,month,day);
    }

    public void setTime(Date toDoDate){
        Date date;
        if(toDoDate!=null){
            date = toDoDate;
        }
        else{
            date = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        view.setTime(hour,minute);
    }
}
