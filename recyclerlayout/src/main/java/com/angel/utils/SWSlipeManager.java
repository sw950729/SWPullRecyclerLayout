package com.angel.utils;

import com.angel.layout.SWSlipeLayout;

public class SWSlipeManager {

    private SWSlipeLayout swSlipeLayout;
    private static SWSlipeManager SWSlipeManager = new SWSlipeManager();

    public static SWSlipeManager getInstance() {
        return SWSlipeManager;
    }

    public void setSwSlipeLayout(SWSlipeLayout swSlipeLayout) {
        this.swSlipeLayout = swSlipeLayout;
    }

    public void clear() {
        swSlipeLayout = null;
    }

    public void close() {
        if (swSlipeLayout != null) {
            swSlipeLayout.close();
        }
    }

    /**
     * if s==null means no item is open
     *
     * @return
     */
    public boolean haveOpened() {
        return swSlipeLayout != null;
    }

    /**
     * if s==null means no item is open
     * true means two item is not the same one and one item is open
     *
     * @return
     */
    public boolean haveOpened(SWSlipeLayout s) {
        return swSlipeLayout != null && swSlipeLayout == s;
    }
}