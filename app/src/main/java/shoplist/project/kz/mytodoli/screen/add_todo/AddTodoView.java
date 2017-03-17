package shoplist.project.kz.mytodoli.screen.add_todo;

import shoplist.project.kz.mytodoli.utils.dialog.LoadingView;

/**
 * Created by Andrey on 3/11/2017.
 */

public interface AddTodoView extends LoadingView {
    void openMainActivity();
    void setTimeAndDate();
    void setDay(int year, int month, int day);
    void setTime(int hour, int minute);
    void showToast();
}
