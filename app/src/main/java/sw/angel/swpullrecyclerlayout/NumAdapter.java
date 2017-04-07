package sw.angel.swpullrecyclerlayout;

import android.content.Context;
import com.angel.adapter.SWRecyclerAdapter;
import com.angel.adapter.SWViewHolder;

import java.util.List;

/**
 * Created by Angel on 2017/4/7.
 */
public class NumAdapter extends SWRecyclerAdapter<String> {

    private List<String> list;
    private Context context;

    public NumAdapter(Context context, List<String> list) {
        super(context, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item;
    }

    @Override
    public void bindData(SWViewHolder holder, int position, String item) {
        holder.getTextView(R.id.text).setText(list.get(position)+"");
    }
}
