package com.immoc.ww.greendaodemo.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.immoc.ww.greendaodemo.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class PieChartFragment extends Fragment {


    @Bind(R.id.chart)
    PieChart mChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pie_chart_fragment, container, false);
        ButterKnife.bind(this, view);
        initUI();
        return view;
    }

    /* @描述 初始化UI */
    private void initUI() {
        List<PieEntry> entries = new ArrayList<>();


        entries.add(new PieEntry(26.7f, "Yellow"));
        entries.add(new PieEntry(55f, "Red"));
        entries.add(new PieEntry(30.8f, "Blue"));
        entries.add((new PieEntry(66f, "Green")));

        PieDataSet set = new PieDataSet(entries, "Election Results");

        set.setSliceSpace(10f);
        set.setSelectionShift(50f);

        set.setColors(new int[]{Color.YELLOW,Color.RED,Color.BLUE,Color.GREEN});

        PieData data = new PieData(set);
        mChart.setData(data);
        stylingPieChart(mChart);
        mChart.invalidate(); // refresh
    }

    /* @描述 设置PieChart的样式 */
    private void stylingPieChart(PieChart mChart) {
        //设置饼快的标签
        mChart.setDrawSliceText(true);
        //设置为true，那么饼块的内容会按照相应数据的百分比显示，否则显示实际值
        mChart.setUsePercentValues(false);
        //设置中心文字
        mChart.setCenterText(getString(R.string.centerText));
        //可以理解为设置中心文字的区域宽度，如果文字超过宽度会进行换行
        // 实际计算为内径的按照百分比缩小后作为中心文字区域的宽度
        mChart.setCenterTextRadiusPercent(20f);
        //可以理解为设置内径
        mChart.setHoleRadius(40f);
        //设置半透明圈的半径
        mChart.setTransparentCircleRadius(50f);
        //设置半透明圈的颜色
        mChart.setTransparentCircleColor(getResources().getColor(R.color.colorPrimary));
        //设置半透明圈的透明度[0~255]
        mChart.setTransparentCircleAlpha(99);
        //设置饼图的最大角度：默认为360°
        mChart.setMaxAngle(90f);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


}
