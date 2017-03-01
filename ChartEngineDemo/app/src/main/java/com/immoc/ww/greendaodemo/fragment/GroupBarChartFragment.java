package com.immoc.ww.greendaodemo.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.immoc.ww.greendaodemo.MyMarkView;
import com.immoc.ww.greendaodemo.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class GroupBarChartFragment extends Fragment {


    @Bind(R.id.chart_bar)
    BarChart mChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_bar_chart_fragment, container, false);
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
        BarDataSet dataSet = new BarDataSet(entries, "BarDataSet1"); // add entries to dataset
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        BarDataSet dataSet2 = new BarDataSet(entries2, "BarDataSet2"); // add entries to dataset
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        //设置数据显示颜色：柱子颜色
        dataSet2.setColor(Color.RED);
        dataSet2.setBarBorderColor(Color.BLUE);

        float groupSpace=1f;
        float barSpace=0.2f;
        float barWidth=0.45f;


        //柱状图数据集
        BarData data = new BarData(dataSet,dataSet2);

        //data.setDrawValues(false);
        //设置柱子宽度
        data.setBarWidth(barWidth);
        mChart.setData(data);//装载数据
        mChart.groupBars(0f,groupSpace,barSpace);

        setGeneralStyling(mChart);
        IMarker marker= new MyMarkView(getContext(),R.layout.makerview);
        mChart.setMarker(marker);
        mChart.setDrawValueAboveBar(true);

        mChart.invalidate();//刷新


    }

    /* @描述 图表的基本设置 */
    private void setGeneralStyling(BarChart chart) {
        //设置背景
        chart.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        //设置图表右下角出现的说明文本,以及说明文本的基本设置。
        //初始化一个Description对象
        Description desc=new Description();
        //设置文本内容
        desc.setText(getString(R.string.desc));
        //设置文本大小
        desc.setTextSize(20f);
        //设置文本颜色
        desc.setTextColor(Color.WHITE);
        //设置文本位置，文本右上角相对于（0，0）的坐标位置
        desc.setPosition(400f,400f);
        //引入说明文本
        chart.setDescription(desc);
        //设置图表数据为空时候的文本
        chart.setNoDataText(getString(R.string.Nodata));
        //是否绘制图表背景的网格（开关，如果设置为false那么网格设置都失效）
        chart.setDrawGridBackground(true);
        //设置网格背景颜色
        chart.setGridBackgroundColor(Color.YELLOW);
        //设置边框开关
        chart.setDrawBorders(true);
        //设置边框颜色
        chart.setBorderColor(Color.CYAN);
        //设置边框宽度
        chart.setBorderWidth(20f);
        //设置图表上最大可见绘制值标签的数目。这只需要影响setdrawvalues()时启用。
//        chart.setMaxVisibleValueCount(30);

        //用来描述Y轴的显示，如果设置为true，那么Y轴会显示X变化范围内Y值最大的变化范围
        chart.setAutoScaleMinMaxEnabled(false);
        //设置为true那么图标在放大之后无法进行拖拽，默认为false
        chart.setKeepPositionOnRotation(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
