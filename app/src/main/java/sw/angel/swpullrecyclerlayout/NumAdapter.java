package sw.angel.swpullrecyclerlayout;

import android.content.Context;
import com.angel.adapter.SWRecyclerAdapter;
import com.angel.adapter.SWViewHolder;
import com.angel.layout.SwipeLinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angel on 2017/4/7.
 */
public class NumAdapter extends SWRecyclerAdapter<String> implements SwipeLinearLayout.OnSwipeListener{

    private List<String> list;
    private Context context;
    List<SwipeLinearLayout> swipeLinearLayouts = new ArrayList<>();

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
        swipeLinearLayouts.add(holder.getSWSlipLayout(R.id.sll));
        holder.getSWSlipLayout(R.id.sll).setOnSwipeListener(this);
        holder.getSWSlipLayout(R.id.sll).scrollTo(0,0);
    }

    @Override
    public void onDirectionJudged(SwipeLinearLayout thisSll, boolean isHorizontal) {
        if (false == isHorizontal) {
            for (SwipeLinearLayout sll : swipeLinearLayouts) {
                if (null == sll) {
                    continue;
                }
                sll.scrollAuto(SwipeLinearLayout.DIRECTION_SHRINK);
            }
        } else {
            for (SwipeLinearLayout sll : swipeLinearLayouts) {
                if (null == sll) {
                    continue;
                }
                if (!sll.equals(thisSll)) {
                    //划开一个sll， 其他收缩
                    sll.scrollAuto(SwipeLinearLayout.DIRECTION_SHRINK);
                }
            }
        }
    }
}
