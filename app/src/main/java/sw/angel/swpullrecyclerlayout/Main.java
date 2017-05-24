package sw.angel.swpullrecyclerlayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import sw.coord.CoordiateActivity;

/**
 * Created by Angel on 2017/5/24.
 */
public class Main extends Activity implements View.OnClickListener {

    private Button recyclerlayout;
    private Button coordinatorlayout;
    private Button swcoordinatorlayout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initial();
    }

    private void initial() {
        recyclerlayout = (Button) findViewById(R.id.recyclerlayout);
        coordinatorlayout = (Button) findViewById(R.id.coordinatorlayout);
        swcoordinatorlayout = (Button) findViewById(R.id.swcoordinatorlayout);
        recyclerlayout.setOnClickListener(this);
        coordinatorlayout.setOnClickListener(this);
        swcoordinatorlayout.setOnClickListener(this);
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
        }
    }
}
