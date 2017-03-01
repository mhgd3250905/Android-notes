package com.immoc.ww.greendaodemo;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

/**
 * Created by admin on 2017/3/1.
 */
/*
* 
* 描    述：
* 作    者：ksheng
* 时    间：2017/3/1$ 1:20$.
*/
public class MyMarkView extends MarkerView{
    private TextView tvContent;
    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public MyMarkView(Context context, int layoutResource) {
        super(context, layoutResource);
        tvContent= (TextView) findViewById(R.id.tv_content);

    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        tvContent.setTextColor(getResources().getColor(R.color.colorAccent));
        tvContent.setText("您选择的是"+e.getY());

        super.refreshContent(e, highlight);
    }

    private MPPointF mOffset;

    @Override
    public MPPointF getOffset() {
        if(mOffset==null){
            mOffset=new MPPointF(0,0);
        }
        return mOffset;
    }
}
