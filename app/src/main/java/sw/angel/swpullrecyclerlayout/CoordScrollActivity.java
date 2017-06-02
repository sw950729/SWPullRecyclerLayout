package sw.angel.swpullrecyclerlayout;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import com.angel.interfaces.OnTouchUpListener;
import com.angel.layout.SWPullRecyclerLayout;
import com.angel.layout.SWPullScollerLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angel on 2017/6/1.
 */
public class CoordScrollActivity extends Activity implements OnTouchUpListener {
    private SWPullScollerLayout scollerLayout;
    private View header;
    private View footer;
    private View content;
//    private RecyclerView recyclerView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scrollerlayout);
        initial();
    }

    private void initial() {
        scollerLayout = (SWPullScollerLayout) findViewById(R.id.scroller);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            list.add(i + 1 + "");
        }
        header = LayoutInflater.from(this).inflate(R.layout.header, null);
        footer = LayoutInflater.from(this).inflate(R.layout.footer, null);
        content = LayoutInflater.from(this).inflate(R.layout.swscrolltest, null);
//        recyclerView = (RecyclerView) content.findViewById(R.id.recycler);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setAdapter(new NumAdapter(this, list));
        scollerLayout.addHeaderView(header, 100);
        scollerLayout.addFooterView(footer, 100);
        scollerLayout.addContentView(content);
        scollerLayout.addOnTouchUpListener(this);
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
