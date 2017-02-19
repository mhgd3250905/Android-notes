package com.immoc.ww.greendaodemo.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
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


public class BarChartFragment extends Fragment {


    @Bind(R.id.chart_bar)
    BarChart mChart;

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
            entries2.add(new BarEntry(i,10 * i));
        }

        //创建数据集
        BarDataSet dataSet = new BarDataSet(entries, "BarDataSet1"); // add entries to dataset
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        BarDataSet dataSet2 = new BarDataSet(entries2, "BarDataSet2"); // add entries to dataset
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        //设置数据显示颜色：柱子颜色
        dataSet2.setColor(Color.RED);
        dataSet2.setBarBorderColor(Color.BLUE);

        List<IBarDataSet> dataSets=new ArrayList<IBarDataSet>();
        dataSets.add(dataSet);
        dataSets.add(dataSet2);
        //柱状图数据集
        BarData data = new BarData(dataSets);
        //设置柱子宽度
        data.setBarWidth(0.9f);
        mChart.setData(data);//装载数据
        mChart.setFitBars(true); //X轴自适应所有柱形图
        mChart.invalidate();//刷新


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
