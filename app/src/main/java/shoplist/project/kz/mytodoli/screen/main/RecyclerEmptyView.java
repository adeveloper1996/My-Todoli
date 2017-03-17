package shoplist.project.kz.mytodoli.screen.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Andrey on 3/17/2017.
 */

public class RecyclerEmptyView extends RecyclerView {
    private View emptyView;

    private AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            showEmptyView();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            showEmptyView();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            showEmptyView();
        }
    };



    public RecyclerEmptyView(Context context) {
        super(context);
    }

    public void showEmptyView(){

        Adapter<?> adapter = getAdapter();
        if(adapter!=null && emptyView!=null){
            if(adapter.getItemCount()==0){
                emptyView.setVisibility(VISIBLE);
                RecyclerEmptyView.this.setVisibility(GONE);
            }
            else{
                emptyView.setVisibility(GONE);
                RecyclerEmptyView.this.setVisibility(VISIBLE);
            }
        }

    }

    public RecyclerEmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerEmptyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if(adapter!=null){
            adapter.registerAdapterDataObserver(observer);
            observer.onChanged();
        }
    }

    public void setEmptyView(View v){
        emptyView = v;
    }
}
