package sw.angel.swpullrecyclerlayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Angel on 2017/5/24.
 */
public class Main extends Activity implements View.OnClickListener {

    private Button recyclerlayout;
    private Button coordinatorlayout;
    private Button swcoordinatorlayout;
    private Button swscrollerlayout;
    private Button coord;
    private Button swcorrd;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initial();
    }

    private void initial() {
        recyclerlayout = (Button) findViewById(R.id.recyclerlayout);
        coordinatorlayout = (Button) findViewById(R.id.coordinatorlayout);
        swcoordinatorlayout = (Button) findViewById(R.id.swcoordinatorlayout);
        swscrollerlayout = (Button) findViewById(R.id.swscrollerlayout);
        coord= (Button) findViewById(R.id.corrd);
        swcorrd= (Button) findViewById(R.id.swcorrd);
        swcorrd.setOnClickListener(this);
        coord.setOnClickListener(this);
        recyclerlayout.setOnClickListener(this);
        coordinatorlayout.setOnClickListener(this);
        swcoordinatorlayout.setOnClickListener(this);
        swscrollerlayout.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.recyclerlayout:
                startActivity(new Intent(this, RecyclerActivity.class));
                break;
            case R.id.coordinatorlayout:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.swcoordinatorlayout:
                startActivity(new Intent(this, CoordiateActivity.class));
                break;
            case R.id.swscrollerlayout:
                startActivity(new Intent(this, ScorllActivity.class));
                break;
            case R.id.corrd:
                startActivity(new Intent(this, CoordiateActivity.class));
                break;
            case R.id.swcorrd:
                startActivity(new Intent(this,CoordScrollActivity.class));
                break;
        }
    }
}
