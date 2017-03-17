package shoplist.project.kz.mytodoli.screen.main;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.util.Collections;
import java.util.List;
import java.util.zip.Inflater;

import io.realm.Realm;
import io.realm.RealmResults;
import shoplist.project.kz.mytodoli.R;
import shoplist.project.kz.mytodoli.model.TodoItem;
import shoplist.project.kz.mytodoli.screen.add_todo.AddTodoActivity;
import shoplist.project.kz.mytodoli.utils.MyItemTouchHelper;
import shoplist.project.kz.mytodoli.utils.TodoNotification;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by Andrey on 3/12/2017.
 */

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.MyHolder> implements MyItemTouchHelper.ItemTouchHelperAdapter{
    private List<TodoItem> itemList;
    private Context context;
    private Realm realm;

    public TodoAdapter(List<TodoItem> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        LinearLayout llTodoItem;
        ImageView img;
        TextView txtTitle;
        TextView txtDate;
        int id;

        public MyHolder(View itemView) {
            super(itemView);
            llTodoItem = (LinearLayout) itemView.findViewById(R.id.ll_todo_item);
            img = (ImageView) itemView.findViewById(R.id.img_to_do_item);
            txtTitle = (TextView) itemView.findViewById(R.id.txt_to_do_title);
            txtDate = (TextView) itemView.findViewById(R.id.txt_to_do_date);

            llTodoItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_todo_item:
                    Intent intent = new Intent(context,AddTodoActivity.class);
                    intent.putExtra(AddTodoActivity.ADDTODOID,id);
                    Log.i("ssss","id" + id);
                    context.startActivity(intent);
                    break;
            }
        }
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_to_do_recycler,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        TextDrawable myDrawable = TextDrawable.builder().beginConfig()
                .textColor(Color.WHITE)
                .useFont(Typeface.DEFAULT)
                .toUpperCase()
                .endConfig()
                .buildRound(itemList.get(position).getTitle().substring(0,1),itemList.get(position).getTodoColor());
        holder.img.setImageDrawable(myDrawable);
        holder.txtTitle.setText(itemList.get(position).getTitle());
        holder.id = itemList.get(position).getId();
        if(itemList.get(position).getToDoDate() != null){
            String timeToShow;
            if(android.text.format.DateFormat.is24HourFormat(context)){
                timeToShow = AddTodoActivity.formatDate("MMM d, yyyy  k:mm", itemList.get(position).getToDoDate());
            }
            else{
                timeToShow = AddTodoActivity.formatDate("MMM d, yyyy  h:mm a", itemList.get(position).getToDoDate());
            }
            holder.txtDate.setText(timeToShow);
        }
    }

    @Override
    public void onItemMoved(int fromPosition, int toPosition) {
        if(fromPosition<toPosition){
            for(int i=fromPosition; i<toPosition; i++){
                Collections.swap(itemList, i, i+1);
            }
        }
        else{
            for(int i=fromPosition; i > toPosition; i--){
                Collections.swap(itemList, i, i-1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemRemoved(int position) {
        Intent intent = new Intent(context, TodoNotification.class);
        deleteAlarm(intent,itemList.get(position).getId());

        realm = Realm.getDefaultInstance();
        RealmResults<TodoItem> realmResults = realm.where(TodoItem.class).findAll();
        realm.beginTransaction();
        realmResults.deleteFromRealm(position);
        realm.commitTransaction();
        notifyItemRemoved(position);
    }

    private void deleteAlarm(Intent i, int requestCode){
        if(doesPendingIntentExist(i, requestCode)){
            PendingIntent pi = PendingIntent.getService(context, requestCode,i, PendingIntent.FLAG_NO_CREATE);
            pi.cancel();
            getAlarmManager().cancel(pi);
        }
    }

    private boolean doesPendingIntentExist(Intent i, int requestCode){
        PendingIntent pi = PendingIntent.getService(context,requestCode, i, PendingIntent.FLAG_NO_CREATE);
        return pi!=null;
    }

    private AlarmManager getAlarmManager(){
        return (AlarmManager)context.getSystemService(ALARM_SERVICE);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
