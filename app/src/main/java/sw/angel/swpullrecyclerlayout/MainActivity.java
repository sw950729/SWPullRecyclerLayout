package sw.angel.swpullrecyclerlayout;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import com.angel.interfaces.OnTouchUpListener;
import com.angel.layout.SWPullRecyclerLayout;
import com.angel.utils.SWSlipeManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements OnTouchUpListener {

    private SWPullRecyclerLayout recycler;
    private View header;
    private View footer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        recycler.addHeaderView(header, 100);
        recycler.addFooterView(footer, 100);
        NumAdapter adapter = new NumAdapter(this, list);
        recycler.setMyRecyclerView(new LinearLayoutManager(this), adapter);
        recycler.addOnTouchUpListener(this);
    }

    public void OnRefreshing() {
        Log.i("angel", "OnRefreshing: 正在刷新");
//        recycler.setIsScrollRefresh(false);
//        recycler.setScrollTo(recycler.getTotal(), 0);
//        SWSlipeManager.getInstance().close();
    }

    public void OnLoading() {
        Log.i("angel", "OnLoading: 正在加载");
//        recycler.setIsScrollLoad(false);
//        recycler.setScrollTo(recycler.getTotal(), 0);
//        SWSlipeManager.getInstance().close();
    }
}
