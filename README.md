# SWPullRecyclerLayout
打造独一无二的刷新框架    
=========    

效果图   
--------  
![github](https://github.com/sw950729/SWPullRecyclerLayout/blob/master/gif/GIF.gif)
![github](https://github.com/sw950729/SWPullRecyclerLayout/blob/master/gif/detele.gif)
![cithub](https://github.com/sw950729/SWPullRecyclerLayout/blob/master/gif/update.gif)   
   
交流群
-------     
android交流中心：232748032 <a target="_blank" href="http://shang.qq.com/wpa/qunwpa?idkey=8581e738855f7d4f19bfa79d955e25d9ae870a7e722739ae1b6cb5772fad4f9a"><img border="0" src="http://img.blog.csdn.net/20151113153010631" alt="android交流中心" title="android交流中心"></a>

介绍
-------      
详情请看：http://blog.csdn.net/sw950729/article/details/60478901   
```  
        header = LayoutInflater.from(this).inflate(R.layout.header, null);
        footer = LayoutInflater.from(this).inflate(R.layout.footer, null);
        recycler.addHeaderView(header, 100);
        recycler.addFooterView(footer, 100);
        NumAdapter adapter = new NumAdapter(this, list);
        recycler.setMyRecyclerView(new LinearLayoutManager(this), adapter);
//      recycler.setShowHeaderAndFooter(false);
        recycler.addOnTouchUpListener(this);
```  
setShowHeaderAndFooter是判断是否需要刷新和加载，默认为true。若为false，则和普通列表无区别。   

最新版本  
-------      
compile 'com.angel:SWPullRecyclerLayout:1.1.5'    加入了侧滑删除控件    
compile 'com.angel:SWPullRecyclerLayout:1.1.3'    修复了一些bug     
compile 'com.angel:SWPullRecyclerLayout:1.1.2'    修复了SWRecyclerAdapter的bug     
compile 'com.angel:SWPullRecyclerLayout:1.1.1'    修复了刷新加载回滚的bug     
compile 'com.angel:SWPullRecyclerLayout:1.1.0'    加入了SWRecyclerAdapter    
compile 'com.angel:SWPullRecyclerLayout:1.0.0'    初稿       

SWRecyclerAdapter的使用介绍    
-------  
目前adpter我给注入了textview 、ImageView、button，后续我会更新一篇关于独一无二的万能适配器的文章 请关注http://blog.csdn.net/sw950729 的更新。
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
```
newlist = new ArrayList<>();
for (int i = 1; i < list.size(); i++) {
      newlist.add(list.get(i) + "");
   }
newlist.add(5,list.size() + j + "");
j++;
//普通刷新
// list=newlist;
// adapter.setList(newlist);
// adapter.notifyDataSetChanged();
//强大的局部刷新
DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new SWDiffCallBack(list, newlist), true);
//利用DiffUtil.DiffResult对象的dispatchUpdatesTo（）方法，传入RecyclerView的Adapter
//别忘了将新数据给Adapter
list = newlist;
adapter.setList(list);
diffResult.dispatchUpdatesTo(adapter);    
```             

SWSlipeLayout
---------
使用介绍请看：http://blog.csdn.net/sw950729/article/details/71404281    


SWPullScollerLayout
---------
此控件是SWPullRecyclerLayout的升华版，SWPullRecyclerLayout是基于列表的刷新，准确的说是针对recyclerlayout的刷新和加载。而SWPullScrollerLayout是针对scrollview的刷新，即内部控件是任意的。因为还在修改SWPullRecyclerLayout，在此，jcenter先不更新，关于SWPullScrollerLayout的使用方法和SWPullRecyclerLayout差不多，具体内容如下：
```

        header = LayoutInflater.from(this).inflate(R.layout.header, null);
        footer = LayoutInflater.from(this).inflate(R.layout.footer, null);
        content = LayoutInflater.from(this).inflate(R.layout.scrollercontent, null);
        swPullScollerLayout.addHeaderView(header, 100);
        swPullScollerLayout.addFooterView(footer, 100);
        swPullScollerLayout.addContentView(content);
        swPullScollerLayout.addOnTouchUpListener(this);   
```   

如无法理解，可以下载demo看demo源码。    

关于
---------
为了让更多的人理解recyclerview以及nestedscrolling机制而开发的嵌套滑动机制的刷新和加载。 如感觉使用良好，麻烦右上角star一下，谢谢。 如果遇到一些问题，或者有更好的建议，欢迎在Issues提出你的建议。我会参考，然后综合各种情况进行考虑是否需要添加进去。
