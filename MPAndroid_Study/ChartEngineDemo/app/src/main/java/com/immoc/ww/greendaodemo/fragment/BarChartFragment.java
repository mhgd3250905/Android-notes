package com.immoc.ww.greendaodemo.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.immoc.ww.greendaodemo.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class BarChartFragment extends Fragment {


    @Bind(R.id.chart_bar)
    BarChart mChart;
    @Bind(R.id.btn_add)
    Button mBtnAdd;
    private BarData barData;
    private BarDataSet dataSet;
    private BarDataSet dataSet2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bar_chart_fragment, container, false);
        ButterKnife.bind(this, view);
        initUI();
        return view;
    }

    /* @描述 初始化UI */
    private void initUI() {

        List<BarEntry> entries = new ArrayList<BarEntry>();
        List<BarEntry> entries2 = new ArrayList<BarEntry>();

        for (int i = 0; i < 20; i++) {
            // turn your data into Entry objects
            entries.add(new BarEntry(i, 20 * i));
            entries2.add(new BarEntry(i, 10 * i));
        }

        //创建数据集
        // add entries to dataset
        dataSet = new BarDataSet(entries, "BarDataSet1 is the first dataSet");
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        // add entries to dataset
        dataSet2 = new BarDataSet(entries2, "BarDataSet2 is the second dataSet");
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        //设置数据显示颜色：柱子颜色
        dataSet2.setColor(Color.RED);
        dataSet2.setBarBorderColor(Color.BLUE);

        dataSet.setHighLightColor(getResources().getColor(R.color.colorAccent));

        List<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(dataSet);
        dataSets.add(dataSet2);
        //柱状图数据集
        barData = new BarData(dataSets);
        //设置柱子宽度
        barData.setBarWidth(0.9f);
        //清除数据
        //barData.clearValues();
        //设置高亮
        barData.setHighlightEnabled(true);

        //设置阴影颜色
        dataSet2.setBarShadowColor(Color.BLACK);
        //设置高亮条选中的透明度
        dataSet2.setHighLightAlpha(255);
        dataSet.setHighLightAlpha(255);
        //为条形堆栈的不同值设置标签，如果有的话。
        dataSet.setStackLabels(new String[]{"a","b","c","d"});


//        data.addDataSet(dataSet2);
//        data.addDataSet(dataSet);

        mChart.setData(barData);//装载数据
        mChart.setFitBars(true); //X轴自适应所有柱形图
        //设置图例
        legendSetting(mChart);
//        modifyingViewport(mChart);

        mChart.invalidate();//刷新
    }



    /* @描述  */
    private void modifyingViewport(BarChart mChart) {
        //设置视口可以显示的最大x范围
        mChart.setVisibleXRangeMaximum(30f);
        //设置视口可以显示的最小x范围
        mChart.setVisibleXRangeMinimum(0f);
        //设置Y轴可以显示的最大值
        mChart.setVisibleYRangeMaximum(500f,YAxis.AxisDependency.LEFT);
        //设置视口View偏移
        mChart.setViewPortOffsets(0f,0f,0f,0f);
        //添加额外的视口偏移
        mChart.setExtraOffsets(-10f,-10f,-10f,-10f);
        //重置视口偏移
        mChart.resetViewPortOffsets();
    }

    /* @描述 设置图例 */
    private void legendSetting(BarChart mChart) {
        //获取图例
        Legend legend = mChart.getLegend();
        //是否开启设置图例
        legend.setEnabled(true);
        //设置图例文字大小
        legend.setTextSize(10f);
        //设置图例文字颜色
        legend.setTextColor(Color.BLUE);
        //如果设置为true，那么当图例过多或者过长一行显示不下的时候就会自适应换行
        legend.setWordWrapEnabled(true);
        //设置表格的最大相对大小，默认为0.95f(95%)
        legend.setMaxSizePercent(0.95f);
        //设置图例位置
        legend.setPosition(Legend.LegendPosition.ABOVE_CHART_LEFT);
        //设置图例形状:如SQUARE、CIRCLE、LINE、DEFAULT
        legend.setForm(Legend.LegendForm.CIRCLE);
        //设置图例XY方向的间隔宽度
        legend.setXEntrySpace(20f);
        legend.setYEntrySpace(20f);
        //设置图例标签到文字之间的距离
        legend.setFormToTextSpace(20f);
        //设置文本包装
        legend.setWordWrapEnabled(true);

        //自定义设置图例
        LegendEntry legendEntry = new LegendEntry();
        legendEntry.label = getString(R.string.customizeLegend);
        legendEntry.formColor = Color.RED;
        legendEntry.formSize = 10;
        //设置图例
        legend.setCustom(new LegendEntry[]{legendEntry});
        //动态设置自定义图例
        legend.setExtra(new LegendEntry[]{legendEntry});
        //重置取消自定义的图例
        //legend.resetCustom();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.btn_add)
    public void onClick() {

        if (mChart.saveToGallery("test",100)) {
            Toast.makeText(getContext(), "图片保存成功", Toast.LENGTH_SHORT).show();
        }
//        Log.d("BarChartFragment", "mChart.getYMin():" + mChart.getYMin());
//        Log.d("BarChartFragment", "mChart.getYMax():" + mChart.getYMax());

//        Log.d("BarChartFragment", "mChart.getLowestVisibleX():" + mChart.getLowestVisibleX());
//        Log.d("BarChartFragment", "mChart.getHighestVisibleX():" + mChart.getHighestVisibleX());

        //重置所有缩放和拖动，使图完全符合它的边界（充分放大）
//        mChart.fitScreen();
        //经过缩放之后重新使左边从指定位置开始
//        mChart.moveViewToX(10f);
        //经过缩放之后重新使视口垂直中点为指定数值
//        mChart.moveViewTo(0f,100f, YAxis.AxisDependency.LEFT);
        //经过缩放之后重新使视口垂直中点为指定点
//        mChart.centerViewTo(10f,100f, YAxis.AxisDependency.LEFT);
        //有动画的移动视口View
//        mChart.moveViewToAnimated(10f,10f, YAxis.AxisDependency.LEFT,1000);
        //有动画的移动视口View到指定中心点
//        mChart.centerViewToAnimated(10,100f, YAxis.AxisDependency.LEFT,1000);
//        mChart.zoomIn();
//        mChart.zoomOut();
        //缩放指定倍数
//        mChart.zoom(2f,2f,2f,2f);
        //缩放指定倍数并指定缩放的Y轴
//        mChart.zoom(2f,2f,2f,2f, YAxis.AxisDependency.LEFT);
        //有动画的缩放
        //mChart.zoomAndCenterAnimated(2f,2f,2f,2f, YAxis.AxisDependency.LEFT,1000);
    }
}
