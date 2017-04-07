# SWPullRecyclerLayout
打造独一无二的刷新框架  
====  
交流群
-------     
232748032  

介绍
-------      
详情请看：http://blog.csdn.net/sw950729/article/details/60478901 后续更新会在此说明。

最新版本  
-------      
complie 'com.angel:SWPullRecyclerLayout:1.1.1'
  

SWRecyclerAdapter的使用介绍      
-------    目前adpter我给注入了textview 、ImageView、button 近期事情比较多，后续我会更新一篇关于独一无二的万能适配器的文章 请关注http://blog.csdn.net/sw950729 的更新。
```
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
```
