package sw.coord;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import sw.angel.swpullrecyclerlayout.R;

/**
 * Created by Administrator on 2017/5/22 0022.
 */

public class CoordiateActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coordinatetest);
        RecyclerView recyclerView= (RecyclerView) findViewById(R.id.recycler_test);
        recyclerView.setAdapter(new RecyclerAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
