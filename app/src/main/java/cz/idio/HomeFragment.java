package cz.idio;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import cz.idio.api.ApiClient;
import cz.idio.api.response.DayItem;
import cz.idio.api.response.WorklistResponse;
import cz.idio.databinding.FragmentHomeBinding;
import cz.idio.model.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;



public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private Context mContext;
    private TextView totalHoursTextView, nightHoursTextView, weekendHoursTextView, overtimeHoursTextView,holidayHoursTextView, holidayCompensationTextView;
    private BarChart barChart;
    private Spinner monthSpinner;
    private List<DayItem> dayItems = new ArrayList<>();
    private double totalWorkedHours;
    private double nightHours;
    private double weekendHours;
    private double overtimeHours;
    private double holidayHours;
    private double holidayCompensationHours;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        requireActivity().setTitle(mContext.getString(R.string.title_home_fragment));

        // Inicializace UI prvků
        totalHoursTextView = binding.totalHours;
        nightHoursTextView = binding.nightHours;
        weekendHoursTextView = binding.weekendHours;
        overtimeHoursTextView = binding.overtimeHours;
        holidayHoursTextView = binding.holidayHours;
        holidayCompensationTextView = binding.holidayCompensation;
        barChart = binding.barChart;
        monthSpinner = binding.monthSpinner;

        // Nastavení Spinneru
        setupMonthSpinner();

        return binding.getRoot();
    }

    private void setupMonthSpinner() {
        // Vytvoření seznamu měsíců v češtině
        List<String> months = Arrays.asList("Leden", "Únor", "Březen", "Duben", "Květen", "Červen", "Červenec", "Srpen", "Září", "Říjen", "Listopad", "Prosinec");

        // Vytvoření ArrayAdapteru pro Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, months);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(adapter);

        // Nastavení OnItemSelectedListener pro Spinner
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Získání vybraného měsíce a načtení dat
                fetchDataForMonth(position + 1); // Měsíce jsou indexovány od 1 do 12
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nic nedělat
            }
        });

        // Nastavení výchozího vybraného měsíce na aktuální měsíc
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH); // Vrací hodnotu od 0 (leden) do 11 (prosinec)
        monthSpinner.setSelection(currentMonth);
    }

    private void fetchDataForMonth(int month) {
        SharedPreferences preferences = mContext.getSharedPreferences("data", Context.MODE_PRIVATE);
        String mod = "WorkList";
        String cmd = "GetPerson";
        String complogin = " ";
        ApiService apiService = ApiClient.getClient(mContext).create(ApiService.class);
        String login = preferences.getString("username", "");
        String pwd = preferences.getString("pwdSha1", "");
        int id = Integer.parseInt(preferences.getString("personId", ""));
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month - 1); // Nastavení měsíce
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        String dateday = sdf.format(calendar.getTime());

        Call<WorklistResponse> call = apiService.getWorklist(mod, cmd, complogin, login, pwd, id, dateday);
        call.enqueue(new Callback<WorklistResponse>() {
            @Override
            public void onResponse(@NonNull Call<WorklistResponse> call, @NonNull Response<WorklistResponse> response) {
                WorklistResponse worklistResponse = response.body();
                if (worklistResponse != null) {
                    dayItems = worklistResponse.getDay();
                    totalWorkedHours = worklistResponse.getWork() / 60f;
                    nightHours = worklistResponse.getAddPayList().stream().filter(pay -> pay.getName().equals("Práce v noci")).findFirst().map(pay -> pay.getLen() / 60f).orElse(0f);
                    weekendHours = worklistResponse.getAddPayList().stream().filter(pay -> pay.getName().equals("Práce o víkendu")).findFirst().map(pay -> pay.getLen() / 60f).orElse(0f);
                    overtimeHours = worklistResponse.getAddPayList().stream().filter(pay -> pay.getName().equals("Práce přesčas")).findFirst().map(pay -> pay.getLen() / 60f).orElse(0f);
                    // Načtení hodin dovolené z pole Tubes
                    holidayHours = worklistResponse.getTubes().stream().filter(tube -> tube.getNames().equals("Dovolená")).findFirst().map(tube -> tube.getLen() / 60f).orElse(0f);
                    holidayCompensationHours = worklistResponse.getCompHoliday() / 60f;

                    // Aktualizace UI prvků s novými daty
                    updateUI();
                }
            }

            @Override
            public void onFailure(@NonNull Call<WorklistResponse> call, @NonNull Throwable t) {
                Log.e("HomeFragment", "Failed to get worklist: " + t.getMessage());
            }
        });
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void updateUI() {
        // Nastavení textu pro celkové hodiny
        totalHoursTextView.setText(String.format("odpracované hodiny: %.2f", totalWorkedHours));
        nightHoursTextView.setText(String.format("práce v noci: %.2f", nightHours));
        weekendHoursTextView.setText(String.format("práce o víkendu: %.2f", weekendHours));
        overtimeHoursTextView.setText(String.format("přesčas: %.2f", overtimeHours));
        holidayHoursTextView.setText(String.format("dovolená: %.2f", holidayHours));
        holidayCompensationTextView.setText(String.format("Náhrada za svátky: %.2f", holidayCompensationHours));

        // Nastavení dat pro sloupcový graf
        setBarChartData();
    }

    private void setBarChartData() {
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, (float) totalWorkedHours));
        entries.add(new BarEntry(1, (float) nightHours));
        entries.add(new BarEntry(2, (float) weekendHours));
        entries.add(new BarEntry(3, (float) overtimeHours));
        entries.add(new BarEntry(4, (float) holidayCompensationHours));
        entries.add(new BarEntry(5, (float) holidayHours));

        BarDataSet dataSet = new BarDataSet(entries, "Hodiny práce");
        dataSet.setColors(new int[]{R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark, R.color.colorAccentDark, R.color.colorPrimaryLight}, mContext);
        BarData barData = new BarData(dataSet);
        barChart.setData(barData);

        // Nastavení osy X
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(Arrays.asList("Celkem", "Noční", "Víkend", "Přesčas", "svátky", "Dovolená")));

        // Nastavení osy Y
        YAxis leftAxis = barChart.getAxisLeft();
        YAxis rightAxis = barChart.getAxisRight();
        leftAxis.setGranularity(1f);
        rightAxis.setGranularity(1f);

        barChart.invalidate(); // refresh
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;


    }
}