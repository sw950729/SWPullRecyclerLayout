package sw.coord;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import sw.angel.swpullrecyclerlayout.NumAdapter;
import sw.angel.swpullrecyclerlayout.R;

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
        recyclerView.setAdapter(new NumAdapter(this, list));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
