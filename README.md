# SWPullRecyclerLayout
打造独一无二的刷新框架    
=========    

效果图   
--------  
![github](https://github.com/sw950729/SWPullRecyclerLayout/blob/master/gif/GIF.gif)
![csdn](http://img.blog.csdn.net/20170411131831338?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvc3c5NTA3Mjk=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
![csdn](http://img.blog.csdn.net/20170508094409073?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvc3c5NTA3Mjk=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)   
   
交流群
-------     
android交流中心：232748032 <a target="_blank" href="http://shang.qq.com/wpa/qunwpa?idkey=8581e738855f7d4f19bfa79d955e25d9ae870a7e722739ae1b6cb5772fad4f9a"><img border="0" src="http://img.blog.csdn.net/20151113153010631" alt="android交流中心" title="android交流中心"></a>

介绍
-------      
详情请看：http://blog.csdn.net/sw950729/article/details/60478901 后续更新会在此说明。

最新版本  
-------      
complie 'com.angel:SWPullRecyclerLayout:1.1.5'    加入了侧滑删除控件    
complie 'com.angel:SWPullRecyclerLayout:1.1.3'    修复了一些bug     
complie 'com.angel:SWPullRecyclerLayout:1.1.2'    修复了SWRecyclerAdapter的bug     
complie 'com.angel:SWPullRecyclerLayout:1.1.1'    修复了刷新加载回滚的bug     
complie 'com.angel:SWPullRecyclerLayout:1.1.0'    加入了SWRecyclerAdapter    
complie 'com.angel:SWPullRecyclerLayout:1.0.0'    初稿       

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


SWSlipeLayout
---------
使用介绍请看：http://blog.csdn.net/sw950729/article/details/71404281 
