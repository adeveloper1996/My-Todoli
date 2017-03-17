package shoplist.project.kz.mytodoli.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Andrey on 3/12/2017.
 */

public class TodoItem extends RealmObject {
    @PrimaryKey
    private int id;
    private String title;
    private boolean isReminder;
    private Date toDoDate;
    private int todoColor;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isReminder() {
        return isReminder;
    }

    public void setReminder(boolean reminder) {
        isReminder = reminder;
    }

    public Date getToDoDate() {
        return toDoDate;
    }

    public void setToDoDate(Date toDoDate) {
        this.toDoDate = toDoDate;
    }

    public int getTodoColor() {
        return todoColor;
    }

    public void setTodoColor(int todoColor) {
        this.todoColor = todoColor;
    }
}
