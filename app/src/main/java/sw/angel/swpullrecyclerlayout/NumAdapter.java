package sw.angel.swpullrecyclerlayout;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.angel.adapter.SWRecyclerAdapter;
import com.angel.adapter.SWViewHolder;
import com.angel.layout.SWSlipeLayout;
import com.angel.utils.SWSlipeManager;

import java.util.List;

/**
 * Created by Angel on 2017/4/7.
 */
public class NumAdapter extends SWRecyclerAdapter<String> {

    private List<String> list;
    private Context context;
    private SWSlipeLayout swSlipeLayout;

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
        textView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (SWSlipeManager.getInstance().haveOpened()) {
                    SWSlipeManager.getInstance().close();
                } else {
                    Toast.makeText(context, position + 1 + "", 1000).show();
                }
            }
        });
    }

}
