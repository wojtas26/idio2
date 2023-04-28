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
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import android.icu.util.Calendar;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cz.idio.api.ApiClient;

import cz.idio.api.ColorTextDayDecorator;
import cz.idio.api.EventDecorator;
import cz.idio.api.response.DayItem;
import cz.idio.api.response.LoginResponse;
import cz.idio.api.response.Reg;
import cz.idio.api.response.Tube;
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
    private com.prolificinteractive.materialcalendarview.MaterialCalendarView calendarView;

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
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(calendar.getTime());
        String dateday = formattedDate;
       // Přidejte červenou barvu pro neděle
        Collection<CalendarDay> sundays = new ArrayList<>();

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.MONTH, Calendar.JANUARY);
        startCalendar.set(Calendar.DAY_OF_MONTH, 1);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.MONTH, Calendar.DECEMBER);
        endCalendar.set(Calendar.DAY_OF_MONTH, 31);

        while (startCalendar.before(endCalendar)) {
            if (startCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                int year = startCalendar.get(Calendar.YEAR);
                int month = startCalendar.get(Calendar.MONTH) + 1; // Přidejte 1 k měsíci, aby byl založený na jedničkách
                int day = startCalendar.get(Calendar.DAY_OF_MONTH);
                CalendarDay calendarDay = CalendarDay.from(year, month, day);
                sundays.add(calendarDay);
            }
            startCalendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        ColorTextDayDecorator sundayDecorator = new ColorTextDayDecorator(ContextCompat.getColor(mContext.getApplicationContext(), R.color.red), sundays);
        calendarView.addDecorator(sundayDecorator);




        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                Call<WorklistResponse> call = apiService.getWorklist(mod, cmd, complogin, login, pwd, id, date.getYear()+"-0"+date.getMonth()+"-01");
                call.enqueue(new Callback<WorklistResponse>()

                {
                    @Override
                    public void onResponse
                            (Call < WorklistResponse > call, Response < WorklistResponse > response) {
                        WorklistResponse worklistResponse = response.body();
                        dayItems = worklistResponse.getDay();
                        Collection<CalendarDay> dates = new ArrayList<>();
                        Collection<CalendarDay> holidayDays = new ArrayList<>();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                        if (dayItems != null) {
                            for (DayItem dayItem : dayItems) {
                                // Get the CalendarDay object from the DayItem object
                                Date date;
                                try {
                                    date = sdf.parse(dayItem.getDay());
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(date);
                                    int year = calendar.get(Calendar.YEAR);
                                    int month = calendar.get(Calendar.MONTH) + 1; // Přidejte 1 k měsíci, aby byl založený na jedničkách
                                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                                    isValidDate(year, month, day);
                                    CalendarDay calendarDay = CalendarDay.from(year, month, day);

                                    // Přidejte den do seznamu pouze pokud má nějaké hodnoty
                                    if (dayItem.getReg() != null && !dayItem.getReg().isEmpty() && dayItem.getReg().get(0).getRegistrationId() != 5) {
                                        dates.add(calendarDay);
                                    } else if (dayItem.getReg() != null && !dayItem.getReg().isEmpty() && dayItem.getReg().get(0).getRegistrationId() == 5) {
                                        holidayDays.add(calendarDay);
                                        if (dayItem.getTube() != null && dayItem.getTube().get(0).getNames().equals("Dovolená")) {
                                            holidayDays.add(calendarDay);
                                        }
                                    } else if (dayItem.getWorkShiftId() == -1) {
                                        holidayDays.add(calendarDay);
                                    }
                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            EventDecorator decorator1 = new EventDecorator(ContextCompat.getColor(mContext.getApplicationContext(), R.color.purple_200), holidayDays);
                            calendarView.addDecorator(decorator1);
                            EventDecorator decorator = new EventDecorator(ContextCompat.getColor(mContext.getApplicationContext(), R.color.purple_700), dates);
                            calendarView.addDecorator(decorator);

                        }
                    }
                    @Override
                    public void onFailure (Call < WorklistResponse > call, Throwable t){
                        Log.e(TAG, "Failed to get worklist for month: " + t.getMessage());
                    }
                });

            }
        });
// Set a OnDateChangeListener for the CalendarView
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                // Get the selected date in the format of yyyy-MM-dd
                String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", date.getYear(), date.getMonth(), date.getDay());
                Log.d(TAG, "onDateSelected: "+selectedDate);

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

                } else {
                    // Set the text views to display a message indicating that there is no data available for the selected date
                    hwTimeInTxt.setText("No data available for this date");
                    hwTimeOutTxt.setText("");
                    workShiftTxt.setText("");
                    workTxt.setText("");
                    breakTxt.setText("");
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

        Call<WorklistResponse> call = apiService.getWorklist(mod, cmd, complogin, login, pwd, id, dateday);
        call.enqueue(new Callback<WorklistResponse>()

        {
            @Override
            public void onResponse
                    (Call < WorklistResponse > call, Response < WorklistResponse > response){
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

                }
            }

            @Override
            public void onFailure (Call < WorklistResponse > call, Throwable t){
                Log.e(TAG, "Failed to get worklist for month: " + t.getMessage());
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
    public boolean isValidDate(int year, int month, int day) {
        boolean valid = true;

        // Zkontrolujte, zda je den v rozsahu 1-31
        if (day < 1 || day > 31) {
            valid = false;
        }

        // Zkontrolujte, zda je měsíc v rozsahu 1-12
        if (month < 1 || month > 12) {
            valid = false;
        }

        // Zkontrolujte, zda má měsíc 31 dní
        if ((month == 4 || month == 6 || month == 9 || month == 11) && day == 31) {
            valid = false;
        }

        // Zkontrolujte, zda je únor v přestupném roce
        if (month == 2) {
            boolean leapYear = (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));
            if (day > 29 || (day == 29 && !leapYear)) {
                valid = false;
            }
        }

        return valid;
    }
}
