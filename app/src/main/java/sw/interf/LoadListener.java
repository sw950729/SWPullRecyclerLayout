package sw.interf;

import android.view.View;

import sw.coord.ImageBehavior;

/**
 * Created by Administrator on 2017/5/23 0023.
 */
public interface LoadListener {
    void onLoading(ImageBehavior behavior, View view);
    void onRefresh(ImageBehavior behavior,View view);
}
