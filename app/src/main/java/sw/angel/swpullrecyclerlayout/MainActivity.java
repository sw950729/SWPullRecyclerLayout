package sw.angel.swpullrecyclerlayout;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import com.angel.interfaces.OnTouchUpListener;
import com.angel.layout.SWPullRecyclerLayout;
import com.angel.utils.SWSlipeManager;
import sw.widget.LoadingProcess;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements OnTouchUpListener, AppBarLayout.OnOffsetChangedListener {

    private SWPullRecyclerLayout recycler;
    private View header;
    private View footer;
    private AppBarLayout appBarLayout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initial();
    }

    private void initial() {
        recycler = (SWPullRecyclerLayout) findViewById(R.id.recycler);
        appBarLayout = (AppBarLayout) findViewById(R.id.layout_appbar);
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
//        recycler.closeRefresh();
    }

    public void OnLoading() {
        Log.i("angel", "OnLoading: 正在加载");
//        recycler.closeLoad();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {
            recycler.setEnabled(true);
        } else {
            recycler.setEnabled(false);
        }
    }

    protected void onResume() {
        super.onResume();
        appBarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        appBarLayout.removeOnOffsetChangedListener(this);
    }
}
