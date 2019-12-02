package com.luca.sabatini.appello;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;

import java.util.ArrayList;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));

        Cartesian cartesian = AnyChart.column();

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("01/10/2018", 60));
        data.add(new ValueDataEntry("05/10/2018", 60));
        data.add(new ValueDataEntry("07/10/2018", 50));
        data.add(new ValueDataEntry("09/10/2018", 42));
        data.add(new ValueDataEntry("10/10/2018", 30));
        data.add(new ValueDataEntry("14/10/2018", 26));
        data.add(new ValueDataEntry("18/10/2018", 24));
        data.add(new ValueDataEntry("21/10/2018", 36));
        data.add(new ValueDataEntry("31/10/2018", 20));

        Column column = cartesian.column(data);

        column.color("blue");
        column.tooltip()
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .format("{%Value}");

        cartesian.animation(true);
        cartesian.title("Statistic of students number");

        //cartesian.yScale().minimum(0d);

        //cartesian.yAxis(0).labels().format("${%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        //cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Date of lesson");
        cartesian.yAxis(0).title("Students number");

        anyChartView.setChart(cartesian);
    }
}
