package sw.angel.swpullrecyclerlayout;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import com.angel.interfaces.OnTouchUpListener;
import com.angel.layout.SWPullRecyclerLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angel on 2017/5/24.
 */
public class RecyclerActivity extends Activity implements OnTouchUpListener {
    private SWPullRecyclerLayout recycler;
    private View header;
    private View footer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerlayout);
        initial();
    }

    private void initial() {
        recycler = (SWPullRecyclerLayout) findViewById(R.id.recycler);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            list.add(i + 1 + "");
        }
        header = LayoutInflater.from(this).inflate(R.layout.header, null);
        footer = LayoutInflater.from(this).inflate(R.layout.footer, null);
        recycler.addSwipeRefreshView(100);
        recycler.addCircleProgressView(100);
        NumAdapter adapter = new NumAdapter(this, list);
        recycler.setMyRecyclerView(new LinearLayoutManager(this), adapter);
//        recycler.setShowHeaderAndFooter(false);
        recycler.addOnTouchUpListener(this);
    }

    @Override
    public void onRefreshing() {
        Log.i("angel", "OnRefreshing: 正在刷新");
//        recycler.closeRefresh();
    }

    @Override
    public void onLoading() {
        Log.i("angel", "OnLoading: 正在加载");
//        recycler.closeLoad();
    }
}
