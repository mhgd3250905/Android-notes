package com.immoc.ww.greendaodemo.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.immoc.ww.greendaodemo.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.github.mikephil.charting.components.YAxis.YAxisLabelPosition.OUTSIDE_CHART;
import static com.immoc.ww.greendaodemo.R.id.chart;


public class ChartFeagment extends Fragment{


    @Bind(chart)
    LineChart mChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart_feagment, container, false);
        ButterKnife.bind(this, view);
        initUI();
        return view;
    }

    /* @描述 初始化UI */
    private void initUI() {

        List<Entry> entries = new ArrayList<Entry>();

        for (int i = 0; i < 20; i++) {
            // turn your data into Entry objects
            entries.add(new Entry(i, 2*i));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Test"); // add entries to dataset

        //数据内容是否可以高亮选择
        dataSet.setHighlightEnabled(true);
        //是否显示高亮提示线
        dataSet.setDrawHighlightIndicators(true);
        //设置高亮提示先颜色
        dataSet.setHighLightColor(Color.RED);

        dataSet.setColor(R.color.colorAccent);
        dataSet.setValueTextColor(R.color.colorPrimary); // styling, ...
        LineData lineData = new LineData(dataSet);
        mChart.setData(lineData);

        //启用、禁用交互
        interactionWithChart(mChart);
        //图表的图幅以及减速
        flingAndSeceleration(mChart);
        //突出高亮
        highlighting(mChart);
        //高亮编程性
        hightProgrammatically(mChart);
        //绘制轴线开关:y轴右边
        drawAxis(mChart.getAxisRight());
        //自定义轴的范围
        customizeRange(mChart.getAxisLeft());
        //调整轴的造型
        modifyingAxis(mChart.getAxisLeft());
        //添加一个辅助线
        addLimitLine(mChart.getAxisLeft());

        mChart.invalidate();
    }

    /* @描述 启用、禁用交互 */
    private void interactionWithChart(LineChart chart) {
        chart.setTouchEnabled(true);//是否开启触摸相关的交互方式
        chart.setDragEnabled(true);//是否开启拖拽相关的交互方式
        chart.setScaleEnabled(true);//是否开启xy轴的缩放
        chart.setScaleXEnabled(true);//是否开启x轴的缩放
        chart.setScaleYEnabled(true);//是否开启y轴的缩放
        //是否开启双指捏合缩放:如果关闭了，仍然可以完成x或y一个轴的缩放
        chart.setPinchZoom(true);
    }

    /* @描述 图表的图幅以及摩擦系数 */
    private void flingAndSeceleration(LineChart chart) {
        //如果设置为true，图表继续滚动后润色，达到一种滚动平滑的效果
        //默认值：true
        chart.setDragDecelerationEnabled(true);
        //设置摩擦系数[0:1]:float
        // 0表示摩擦最大，基本上一滑就停
        // 1表示没有摩擦，会自动转化为0.9999,及其顺滑
        //chart.setDragDecelerationFrictionCoef(0.5f);
    }

    /* @描述 突出高亮 */
    private void highlighting(LineChart chart){
        //默认为true:保证在拖动是图像被充分放大了
        chart.setHighlightPerDragEnabled(true);
        //默认为true
        // 设置为true之后可以通过点击的方式高亮选择数据点
        //设置为false之后无法通过点击方式选择，但仍然可以通过拖拽实现
        chart.setHighlightPerTapEnabled(true);
        //高亮选择只可以在距离高亮点指定范围内
        //当选择点与高亮点距离超过设置值时高亮消失
        //设置之后高亮拖拽相对不好用
        //chart.setMaxHighlightDistance(10f);
    }

    /* @描述 高亮编程性 */
    private void hightProgrammatically(LineChart chart){
        //chart.highlightValue(3f,6f,1);
    }

    /* @描述 轴线的绘制 */
    private void drawAxis(AxisBase mAxis){
        //设置是否启用轴线：如果关闭那么就默认没有轴线/标签/网格线
        mAxis.setEnabled(true);
        //设置是否开启绘制轴的标签
        mAxis.setDrawLabels(false);
        //是否绘制轴线
        mAxis.setDrawAxisLine(false);
        //是否绘制网格线
        mAxis.setDrawGridLines(false);
    }

    /* @描述 自定义轴的范围 */
    private void customizeRange(YAxis mAxis){
        //设置坐标轴最大值：如果设置那么轴不会根据传入数据自动设置
        mAxis.setAxisMaximum(10f);
        //重置已经设置的最大值，自动匹配最大值
        mAxis.resetAxisMaximum();
        //设置坐标轴最小值：如果设置那么轴不会根据传入数据自动设置
        mAxis.setAxisMinimum(5f);
        //重置已经设置的最小值，自动匹配最小值
        mAxis.resetAxisMinimum();
        //将图表中最高值的顶部间距（占总轴范围的百分比）与轴上的最高值相比较。
        mAxis.setSpaceMax(10);
        //将图表中最低值的底部间距（占总轴范围的百分比）与轴上的最低值相比较。
        mAxis.setSpaceMin(10);
        //设置标签个数以及是否精确（false为模糊，true为精确）
        mAxis.setLabelCount(20,false);
        //如果设置为true，此轴将被反转，这意味着最高值将在底部，最低的顶部值。
        mAxis.setInverted(true);
        //设置轴标签应绘制的位置。无论是inside_chart或outside_chart。
        mAxis.setPosition(OUTSIDE_CHART);
        //如果设置为true那么下面方法设置最小间隔生效，默认为false
        mAxis.setGranularityEnabled(true);
        //设置Y轴的值之间的最小间隔。这可以用来避免价值复制当放大到一个地步，小数设置轴不再数允许区分两轴线之间的值。
        mAxis.setGranularity(10f);
    }

    /* @描述 调整轴的造型 */
    private void modifyingAxis(AxisBase mAxis){
        //设置坐标轴标签文字颜色
        mAxis.setTextColor(Color.GREEN);
        //设置坐标轴标签文字大小
        mAxis.setTextSize(10f);
        //设置坐标轴标签文字样式
        mAxis.setTypeface(Typeface.DEFAULT_BOLD);
        //设置此轴网格线颜色
        mAxis.setGridColor(Color.RED);
        //设置此轴网格线宽度
        mAxis.setGridLineWidth(0.5f);
        //设置坐标轴的颜色
        mAxis.setAxisLineColor(Color.RED);
        //设置坐标轴的宽度
        mAxis.setAxisLineWidth(1f);
        //使用虚线组成的网格线
        //参数：linelength：虚线长度
        // spacelength:虚线间隔长度
        // phase：虚线出发点（从第一根虚线的哪里出发）
        mAxis.enableGridDashedLine(40f,2f,20f);
    }

    //设置一个限制的线
    private void addLimitLine(AxisBase mAxis){
        LimitLine ll = new LimitLine(10f, "Critical Blood Pressure");
        ll.setLineColor(Color.RED);
        ll.setLineWidth(4f);
        ll.setTextColor(Color.BLACK);
        ll.setTextSize(12f);

        mAxis.addLimitLine(ll);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


//    /* @描述 触摸开始（ACTION_DOWN） */
//    @Override
//    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
//        Toast.makeText(getContext(), "触摸开始！", Toast.LENGTH_SHORT).show();
//    }
//
//    /* @描述 触摸结束 */
//    @Override
//    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
//        Toast.makeText(getContext(), "触摸结束！", Toast.LENGTH_SHORT).show();
//    }
//
//    /* @描述 长按 */
//    @Override
//    public void onChartLongPressed(MotionEvent me) {
//        Toast.makeText(getContext(), "长按！", Toast.LENGTH_SHORT).show();
//    }
//
//    /* @描述 双击 */
//    @Override
//    public void onChartDoubleTapped(MotionEvent me) {
//        Toast.makeText(getContext(), "双击！", Toast.LENGTH_SHORT).show();
//    }
//
//    /* @描述 单击 */
//    @Override
//    public void onChartSingleTapped(MotionEvent me) {
//        Toast.makeText(getContext(), "单击！", Toast.LENGTH_SHORT).show();
//    }
//
//    /* @描述 图幅描述 */
//    @Override
//    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
//        if (velocityX>10f){
//            Toast.makeText(getContext(), "X方向滑动速度大于10f！", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    /* @描述 缩放 */
//    @Override
//    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
//        if (scaleX>2){
//            Toast.makeText(getContext(), "X方向缩放大于2f！", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    /* @描述 手势移动 */
//    @Override
//    public void onChartTranslate(MotionEvent me, float dX, float dY) {
//        if (dX>100f){
//            Toast.makeText(getContext(), "x滑动大于100f！", Toast.LENGTH_SHORT).show();
//        }
//    }
}
