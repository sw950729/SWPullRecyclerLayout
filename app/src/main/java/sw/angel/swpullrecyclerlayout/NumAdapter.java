package sw.angel.swpullrecyclerlayout;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.angel.adapter.SWRecyclerAdapter;
import com.angel.adapter.SWViewHolder;
import com.angel.interfaces.OnSwipeStatusListener;
import com.angel.layout.SWSlipeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angel on 2017/4/7.
 */
public class NumAdapter extends SWRecyclerAdapter<String> implements OnSwipeStatusListener {

    private List<String> list;
    private Context context;
    private SWSlipeLayout swSlipeLayout;
    private List<SWSlipeLayout> swSlipeLayouts = new ArrayList<>();

    public NumAdapter(Context context, List<String> list) {
        super(context, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public int getItemLayoutId(int layoutID) {
        return R.layout.item;
    }

    @Override
    public void bindData(SWViewHolder holder, final int position, String item) {
        TextView textView = holder.getTextView(R.id.text);
        swSlipeLayout = (SWSlipeLayout) holder.getView(R.id.sll);
        textView.setText(list.get(position) + "");
        swSlipeLayouts.add(holder.getSWSlipLayout(R.id.sll));
        textView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                for (SWSlipeLayout swSlipeLayout : swSlipeLayouts) {
                    swSlipeLayout.close();
                }
                Toast.makeText(context, position + 1 + "", 1000).show();
            }
        });
        swSlipeLayout.setOnSwipeStatusListener(this);
    }

    public void onStatusChanged(SWSlipeLayout layout, SWSlipeLayout.Status status) {
        if (status == SWSlipeLayout.Status.Open) {
            for (SWSlipeLayout swSlipeLayout : swSlipeLayouts) {
                if (swSlipeLayout != layout) {
                    swSlipeLayout.close();
                }
            }
        }
    }
}
