# SWPullRecyclerLayout
打造独一无二的刷新框架  
====  
交流群
-------     
android交流中心：232748032 <a target="_blank" href="http://shang.qq.com/wpa/qunwpa?idkey=8581e738855f7d4f19bfa79d955e25d9ae870a7e722739ae1b6cb5772fad4f9a"><img border="0" src="http://img.blog.csdn.net/20151113153010631" alt="android交流中心" title="android交流中心"></a>

介绍
-------      
详情请看：http://blog.csdn.net/sw950729/article/details/60478901 后续更新会在此说明。

最新版本  
-------      
complie 'com.angel:SWPullRecyclerLayout:1.1.3'    
  
因有小伙伴用1.1.3出现了问题，我刚进行了更新，因最近在更新新的功能以及维护公司项目，时间有限，后期我会重新更新jcenter。如不放心使用，可直接使用1.0.0版本。目前建议大家直接导入moudle使用，具体原因如下：刷新时，header直接归位，然后进行刷新操作，加载亦如此，现代码已更新，使用时在接口中使用此方法即可。    
```
    public void OnRefreshing() {
        Log.i("angel", "OnRefreshing: 正在刷新");
//        recycler.setIsScrollRefresh(false);
//        recycler.setScrollTo(recycler.getTotal(), 0);

    }

    public void OnLoading() {
        Log.i("angel", "OnLoading: 正在加载");
//        recycler.setIsScrollLoad(false);
//        recycler.setScrollTo(recycler.getTotal(), 0);
    }
 ```  
    
SWRecyclerAdapter的使用介绍         
--------         
目前adpter我给注入了textview 、ImageView、button 近期事情比较多，后续我会更新一篇关于独一无二的万能适配器的文章 请关注http://blog.csdn.net/sw950729 的更新。
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
SWDiffCallBack
---------
使用介绍请看：http://blog.csdn.net/sw950729/article/details/70052693
