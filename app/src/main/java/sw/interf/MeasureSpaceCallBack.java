package sw.interf;

import android.view.View;

/**
 * Created by qzzhu on 17-5-18.
 */

public abstract class MeasureSpaceCallBack {
    public abstract int getContentWitdh();
    public abstract int getContentHeight();
    private static int getSize(int mode,int size,boolean width,final MeasureSpaceCallBack callBack){
        int result = 0;
        switch(mode){
            case View.MeasureSpec.EXACTLY:
                result = size;
                break;
            case View.MeasureSpec.AT_MOST:
                if(width)
                {
                    int mWitdh = callBack.getContentWitdh();
                    if(mWitdh > size)
                    {
                        result = size;
                    }else{
                        result = mWitdh;
                    }
                }else{
                    int mHeight = callBack.getContentHeight();
                    if(mHeight > size)
                    {
                        result = size;
                    }else{
                        result = mHeight;
                    }
                }
                break;
            case View.MeasureSpec.UNSPECIFIED:
                if(width)
                {
                    result = callBack.getContentWitdh();
                }else{
                    result = callBack.getContentHeight();
                }
                break;
        }
        return result;
    }

    /**
     * get the size real size
     * @param widthMeasureSpec size width that parent given
     * @param heightMeasureSpec size height that parent given
     * @param callBack callback to get view wrap content size
     * @return [0] = width [1] = height
     */
    public static int[] measureSize(int widthMeasureSpec, int heightMeasureSpec,final MeasureSpaceCallBack callBack){
        int modeWitdh = View.MeasureSpec.getMode(widthMeasureSpec);
        int sizeWitdh = View.MeasureSpec.getSize(heightMeasureSpec);

        int modeHeight = View.MeasureSpec.getMode(heightMeasureSpec);
        int sizeHeight = View.MeasureSpec.getSize(heightMeasureSpec);

        int resultWitdh = getSize(modeWitdh,sizeWitdh,true,callBack);
        int resultHeight = getSize(modeHeight,sizeHeight,false,callBack);

        return new int[]{resultWitdh,resultHeight};
    }

}
