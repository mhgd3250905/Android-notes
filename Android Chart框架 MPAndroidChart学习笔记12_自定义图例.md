# Android Chart框架 MPAndroidChart学习笔记12_图例
---
## 1.图例基本设置 ##

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

![](http://ww1.sinaimg.cn/mw690/006aPzcjgy1fczmm7ytihj30ea0lcabn)

## 2.图例的颜色样式自定义 ##

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
        legend.resetCustom();
![](http://ww1.sinaimg.cn/mw690/006aPzcjgy1fcznqsrq32j30e40la3zt)


