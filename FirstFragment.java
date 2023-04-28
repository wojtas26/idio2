package cz.idio;

import static android.content.ContentValues.TAG;


import android.annotation.SuppressLint;
import android.content.Context;

import android.content.SharedPreferences;

import android.os.Bundle;

import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cz.idio.api.ApiClient;

import cz.idio.api.response.DayItem;
import cz.idio.api.response.LoginResponse;
import cz.idio.api.response.Reg;
import cz.idio.api.response.WorklistResponse;
import cz.idio.databinding.FragmentFirstBinding;
import cz.idio.model.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private Context mContext;
    private SharedPreferences preferences;
    private TextView hwTimeInTxt;
    private TextView hwTimeOutTxt;
    private TextView workShiftTxt;
    private TextView workTxt;
    private TextView breakTxt;
    private CalendarView calendarView;

    String hwTimeIn = "";
    String hwTimeOut = "";
    String workShiftName = "";
    double work = 0;
    double pause = 0;
    private List<DayItem> dayItems = new ArrayList<>();
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        preferences = mContext.getSharedPreferences("data", Context.MODE_PRIVATE);
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        calendarView = binding.calendarView;
        hwTimeInTxt = binding.hwTimeInTxt;
        hwTimeOutTxt = binding.hwTimeOutTxt;
        workShiftTxt = binding.workShiftTxt;
        workTxt = binding.workTxt;
        breakTxt = binding.breakTxt;
        String mod = "WorkList";
        String cmd = "GetPerson";
        String complogin = " ";
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        String login = preferences.getString("username", "");
        String pwd = preferences.getString("pwdSha1", "");
        int id = Integer.parseInt(preferences.getString("personId", ""));
        long currentDate = calendarView.getDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentDate);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(calendar.getTime());
        String dateday = formattedDate;



        Call<WorklistResponse> call = apiService.getWorklist(mod, cmd, complogin, login, pwd, id, dateday);
        call.enqueue(new Callback<WorklistResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<WorklistResponse> call, Response<WorklistResponse> response) {
                WorklistResponse worklistResponse = response.body();
                dayItems = worklistResponse.getDay();
                for (DayItem dayItem : dayItems) {
                    // Get the date in milliseconds from the DayItem object
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    Date date = null;
                    try {
                        date = sdf.parse(dayItem.getDay());
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    long timeInMillis = date.getTime();

                    // Add the date to the calendar
                    calendarView.setDate(timeInMillis, false, true);
                }
                    calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                        @Override
                        @SuppressLint("DefaultLocale")
                        public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                            String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                            // Find the corresponding DayItem object
                            DayItem selectedDayItem = null;
                            for (DayItem dayItem : dayItems) {
                                if (dayItem.getDay().equals(selectedDate)) {
                                    selectedDayItem = dayItem;
                                    break;
                                }
                            }
                            // Load the data for the selected date
                            if (selectedDayItem != null) {
                                loadDataForDate(selectedDayItem);
                            }

                        }

                        private void loadDataForDate(DayItem dayItem) {
                            calendarView.setDateTextAppearance(android.R.style.TextAppearance_Medium);
                            List<Reg> regList = dayItem.getReg();
                            if (dayItem.isRepair()) {
                                hwTimeIn = regList.get(0).getHwTime();
                                hwTimeOut = "";
                                workShiftName = dayItem.getWorkShiftName();
                                work = dayItem.getWork();
                                if (dayItem.getTube().size() > 1) {
                                    pause = Integer.parseInt(String.valueOf(dayItem.getTube().get(1).getLen()));
                                } else {
                                    work = dayItem.getWorkLen();
                                    pause = 0;
                                    workShiftName = dayItem.getTube().get(0).getNames();
                                }
                            } else if (regList != null && !regList.isEmpty()) {
                                hwTimeIn = regList.get(0).getHwTime();
                                hwTimeOut = regList.get(1).getHwTime();
                                workShiftName = dayItem.getWorkShiftName();
                                work = dayItem.getWork();
                                if (dayItem.getTube().size() > 1) {
                                    pause = Integer.parseInt(String.valueOf(dayItem.getTube().get(1).getLen()));
                                } else {
                                    pause = 0;
                                }
                            } else {
                                hwTimeIn = "";
                                hwTimeOut = "";
                                workShiftName = "";
                                work = 0;
                                pause = 0;
                            }
                            hwTimeInTxt.setText("Příchod: " + hwTimeIn);
                            hwTimeOutTxt.setText("Odchod: " + hwTimeOut);
                            workShiftTxt.setText("Pracovní směna: " + workShiftName);
                            workTxt.setText("Přítomnost: " + work / 60);
                            breakTxt.setText("Přestávka: " + pause);
                            Log.d(TAG, "Příchod: " + hwTimeIn + " Odchod: " + hwTimeOut + " Pracovní směna: " + workShiftName + " Přítomnost: " + work / 60 + " Přestávka: " + pause);
                        }
                    });

                }


            @Override
            public void onFailure(Call<WorklistResponse> call, Throwable t) {
                Log.e(TAG, "CHYBA: " + t);
            }
        });


        return binding.getRoot();


    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}