package com.immoc.ww.greendaodemo.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        BarDataSet dataSet = new BarDataSet(entries, "BarDataSet1 is the first dataSet"); // add entries to dataset
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        BarDataSet dataSet2 = new BarDataSet(entries2, "BarDataSet2 is the second dataSet"); // add entries to dataset
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
        //设置图例
        legendSetting(mChart);
        mChart.invalidate();//刷新


    }

    /* @描述 设置图例 */
    private void legendSetting(BarChart mChart) {
        //获取图例
        Legend legend=mChart.getLegend();
        //是否开启设置图例
        legend.setEnabled(true);
        //设置图例文字大小
        legend.setTextSize(50f);
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
        legendEntry.label=getString(R.string.customizeLegend);
        legendEntry.formColor=Color.RED;
        legendEntry.formSize=10;
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
}
