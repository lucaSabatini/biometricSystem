package com.luca.sabatini.appello;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.AutoText;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.google.gson.Gson;
import com.luca.sabatini.appello.entities.Attendance;
import com.luca.sabatini.appello.entities.CheckSessionResponse;
import com.luca.sabatini.appello.entities.Corso;
import com.luca.sabatini.appello.student.ConfirmCourseAlert;
import com.luca.sabatini.appello.utils.RestConstants;
import com.luca.sabatini.appello.utils.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class StatisticsActivity extends AppCompatActivity {

    public RequestQueue queue;
    private Context context;
    private TextView textView;
    SharedPrefManager sp;
    private static final String TAG = "StatisticsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_statistics);

        textView = findViewById(R.id.no_attendance_textView);
        queue = Volley.newRequestQueue(Objects.requireNonNull(this));
        sp = new SharedPrefManager(this);
        context = this;

        StringRequest postRequest = new StringRequest(
                Request.Method.GET,
                RestConstants.getAttendancesByCourseUrl(sp.readCorsoId()),
                callbackGet,
                callbackError);

        queue.add(postRequest);
    }

    private Response.ErrorListener callbackError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            if (error.networkResponse != null) {
                Log.e(TAG, "onErrorResponse: callbackError: " + new String(error.networkResponse.data));
                Log.e(TAG, "onErrorResponse: callbackError: " + error.networkResponse.statusCode);
            } else {
                Log.e(TAG, "onErrorResponse: callbackError: " + error.getMessage());
            }
        }
    };

    private Response.Listener<String> callbackGet = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.d(TAG, "onCreate: " + response);
            ArrayList<Attendance> attendances = new ArrayList<>();
                try {
                    JSONArray items = new JSONArray(response);
                    if (items.length() == 0) {
                        Log.d(TAG, "onResponse:  nessuna presenza trovata");
                        findViewById(R.id.progress_bar).setVisibility(View.GONE);
                        textView.setVisibility(View.VISIBLE);
                        return;
                    }

                    for (int i = 0; i < items.length(); i++) {
                        Attendance attendance = new Gson().fromJson(items.getJSONObject(i).toString(), Attendance.class);
                        attendances.add(attendance);
                    }
                    Log.d(TAG, "onResponse: " + attendances);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AnyChartView anyChartView = findViewById(R.id.any_chart_view);
                anyChartView.setProgressBar(findViewById(R.id.progress_bar));


                Cartesian cartesian = AnyChart.column();

                TreeMap<String, Integer> map = new TreeMap<>();
                for(Attendance a: attendances){
                    if(map.containsKey(a.getDate())){
                        map.put(a.getDate(), map.get(a.getDate()) + 1);
                    }
                    else{
                        map.put(a.getDate(), 1);
                    }

                }
                Log.d(TAG, "onResponse: " + map);
                List<DataEntry> data = new ArrayList<>();

                for(Map.Entry<String,Integer> entry : map.entrySet()) {
                    String key = entry.getKey();
                    Integer value = entry.getValue();
                    data.add(new ValueDataEntry(key, value));
                    System.out.println(key + " => " + value);
                }

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
        };

}
