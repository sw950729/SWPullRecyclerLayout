package sw.angel.swpullrecyclerlayout;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import com.angel.interfaces.OnTouchUpListener;
import com.angel.layout.SWPullScollerLayout;

/**
 * Created by Angel on 2017/5/24.
 */
public class ScorllActivity extends Activity implements OnTouchUpListener{

    private SWPullScollerLayout swPullScollerLayout;
    private View header;
    private View footer;
    private View content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scrollerlayout);
        initial();
    }

    private void initial() {
        swPullScollerLayout = (SWPullScollerLayout) findViewById(R.id.scroller);
        header = LayoutInflater.from(this).inflate(R.layout.header, null);
        footer = LayoutInflater.from(this).inflate(R.layout.footer, null);
        content = LayoutInflater.from(this).inflate(R.layout.scrollercontent, null);
        swPullScollerLayout.addHeaderView(header, 100);
        swPullScollerLayout.addFooterView(footer, 100);
        swPullScollerLayout.addContentView(content);
        swPullScollerLayout.addOnTouchUpListener(this);
    }

    @Override
    public void onRefreshing() {
        Log.i("angel", "OnRefreshing: 正在刷新");
    }

    @Override
    public void onLoading() {
        Log.i("angel", "OnLoading: 正在加载");
    }
}
