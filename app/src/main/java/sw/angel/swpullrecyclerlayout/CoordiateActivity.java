package sw.angel.swpullrecyclerlayout;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.angel.adapter.SWRecyclerAdapter;
import com.angel.adapter.SWViewHolder;

import sw.angel.swpullrecyclerlayout.NumAdapter;
import sw.angel.swpullrecyclerlayout.R;
import sw.coord.ImageBehavior;
import sw.interf.LoadListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/22 0022.
 */

public class CoordiateActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coordinatetest);
        RecyclerView recyclerView= (RecyclerView) findViewById(R.id.recycler_test);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            list.add(i + 1 + "");
        }
        recyclerView.setAdapter(new SWRecyclerAdapter<String>(this,list){
            @Override
            public int getItemLayoutId(int layoutID) {
                return R.layout.item;
            }

            @Override
            public void bindData(SWViewHolder holder, int position, String item) {
                holder.getTextView(R.id.text).setText(item);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setBehaviorListener((ImageView) findViewById(R.id.top));
        setBehaviorListener((ImageView) findViewById(R.id.bottom));
    }

    private void setBehaviorListener(ImageView view){
        ImageBehavior behavior = ImageBehavior.from(view);
        behavior.setListener(listener);
    }


    private LoadListener listener = new LoadListener() {
        @Override
        public void onLoading(ImageBehavior behavior,View view) {
            Log.i("onLoading","ok");
            cancelStatue(behavior,view);
        }

        @Override
        public void onRefresh(ImageBehavior behavior,View view) {
            Log.i("onRefresh","ok");
            cancelStatue(behavior,view);
        }
    };


    private void cancelStatue(final ImageBehavior behavior,final View view){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                behavior.onComplete(view);
            }
        },3000);
    }
}
