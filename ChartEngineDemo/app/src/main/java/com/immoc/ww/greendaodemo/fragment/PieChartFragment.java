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
        entries.add(new PieEntry(24.0f, "Red"));
        entries.add(new PieEntry(30.8f, "Blue"));
        entries.add((new PieEntry(18.5f, "Green")));

        PieDataSet set = new PieDataSet(entries, "Election Results");

        set.setColors(new int[]{Color.YELLOW,Color.RED,Color.BLUE,Color.GREEN});

        PieData data = new PieData(set);
        mChart.setData(data);
        mChart.invalidate(); // refresh
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


}
