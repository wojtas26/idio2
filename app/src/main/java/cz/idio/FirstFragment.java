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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import android.icu.util.Calendar;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cz.idio.api.ApiClient;

import cz.idio.api.ColorTextDayDecorator;
import cz.idio.api.EventDecorator;
import cz.idio.api.response.DayItem;
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
    private TextView hwTimeInTxt,workHoursTextView;
    private TextView hwTimeOutTxt;
    private TextView workShiftTxt;
    private TextView workTxt ;
    private TextView breakTxt;
    private com.prolificinteractive.materialcalendarview.MaterialCalendarView calendarView;
    private LinearLayout hwTimeContainer;
    private ProgressBar WorkProgress;
    String hwTimeIn = "";
    String hwTimeOut = "";
    String workShiftName = "";
    double workLen;
    double sumWork;
    double work = 0;
    double pause = 0;
    private List<DayItem> dayItems = new ArrayList<>();
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        requireActivity().setTitle(mContext.getString(R.string.title_first_fragment));
      /*  // Vytvořte ScrollView a nastavte jeho parametry
        ScrollView scrollView = new ScrollView(requireContext());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        scrollView.setLayoutParams(layoutParams);
        // Odeberte váš kontejner z kořenového zobrazení (binding.getRoot()) a přidejte jej do ScrollView
        ViewGroup parent = (ViewGroup) binding.container.getParent();
        if (parent != null) {
            parent.removeView(binding.container);
        }
        scrollView.addView(binding.container);

        // Nastavte ScrollView jako hlavní zobrazení vaší aktivity
        binding.getRoot().addView(scrollView);*/
        SharedPreferences preferences = mContext.getSharedPreferences("data", Context.MODE_PRIVATE);
        calendarView = binding.calendarView;
        binding.getRoot().isScrollContainer();
        hwTimeContainer = binding.hwTimeContainer;
        WorkProgress = binding.hoursProgressBar;
        workHoursTextView = binding.workHoursTextView;
        String mod = "WorkList";
        String cmd = "GetPerson";
        String complogin = " ";
        ApiService apiService = ApiClient.getClient(mContext).create(ApiService.class);
        String login = preferences.getString("username", "");
        String pwd = preferences.getString("pwdSha1", "");
        int id = Integer.parseInt(preferences.getString("personId", ""));
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateday = sdf.format(calendar.getTime());
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
                int month = startCalendar.get(Calendar.MONTH) + 1;
                int day = startCalendar.get(Calendar.DAY_OF_MONTH);
                CalendarDay calendarDay = CalendarDay.from(year, month, day);
                sundays.add(calendarDay);
            }
            startCalendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        ColorTextDayDecorator sundayDecorator = new ColorTextDayDecorator(ContextCompat.getColor(mContext.getApplicationContext(), R.color.red), sundays);
        calendarView.addDecorator(sundayDecorator);
        Call<WorklistResponse> call = apiService.getWorklist(mod, cmd, complogin, login, pwd, id, dateday);
        call.enqueue(new Callback<WorklistResponse>()

        {
            @Override
            public void onResponse
                    (@NonNull Call < WorklistResponse > call, @NonNull Response < WorklistResponse > response) {
                WorklistResponse worklistResponse = response.body();
                assert worklistResponse != null;
                dayItems = worklistResponse.getDay();
                workLen = worklistResponse.getWorkLen()/60f;
                sumWork = worklistResponse.getWork()/60f;
                workHoursTextView.setText("Odpracované hodiny: " + sumWork + " / Pracovní fond: " + workLen);
                WorkProgress.setMax((int) workLen);
                WorkProgress.setProgress((int) sumWork);
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
            public void onFailure (@NonNull Call < WorklistResponse > call, @NonNull Throwable t){
                Log.e(TAG, "Failed to get worklist for month: " + t.getMessage());
            }
        });

        calendarView.setOnMonthChangedListener((widget, date) -> {
            Call<WorklistResponse> call1 = apiService.getWorklist(mod, cmd, complogin, login, pwd, id, date.getYear()+"-0"+date.getMonth()+"-01");
            call1.enqueue(new Callback<WorklistResponse>()

            {
                @Override
                public void onResponse
                        (@NonNull Call < WorklistResponse > call1, @NonNull Response < WorklistResponse > response) {
                    WorklistResponse worklistResponse = response.body();
                    assert worklistResponse != null;
                    dayItems = worklistResponse.getDay();
                    workLen = worklistResponse.getWorkLen()/60f;
                    sumWork = worklistResponse.getWork()/60f;
                    workHoursTextView.setText("Odpracované hodiny: " + sumWork + " / Pracovní fond: " + workLen);
                    WorkProgress.setMax((int) workLen);
                    WorkProgress.setProgress((int) sumWork);
                    Collection<CalendarDay> dates = new ArrayList<>();
                    Collection<CalendarDay> holidayDays = new ArrayList<>();
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                    if (dayItems != null) {
                        for (DayItem dayItem : dayItems) {
                            // Get the CalendarDay object from the DayItem object
                            Date date;
                            try {
                                date = sdf1.parse(dayItem.getDay());
                                Calendar calendar1 = Calendar.getInstance();
                                calendar1.setTime(date);
                                int year = calendar1.get(Calendar.YEAR);
                                int month = calendar1.get(Calendar.MONTH) + 1; // Přidejte 1 k měsíci, aby byl založený na jedničkách
                                int day = calendar1.get(Calendar.DAY_OF_MONTH);
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
                public void onFailure (@NonNull Call < WorklistResponse > call1, @NonNull Throwable t){
                    Log.e(TAG, "Failed to get worklist for month: " + t.getMessage());
                }
            });

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

                }
            }
            @SuppressLint("SetTextI18n")
            private void loadDataForDate(DayItem dayItem) {
                calendarView.setDateTextAppearance(android.R.style.TextAppearance_Medium);
                List<Reg> regList = dayItem.getReg();

                List<String> hwTimeInList = new ArrayList<>();
                List<String> hwTimeOutList = new ArrayList<>();

                if (regList != null && !regList.isEmpty()) {
                    for (Reg reg : regList) {
                        hwTimeContainer.removeAllViews();
                        if (reg.getRegistrationId() == 1) {
                            hwTimeInList.add(reg.getHwTime());
                        } else if (reg.getRegistrationId() == 2) {
                            hwTimeOutList.add(reg.getHwTime());
                        }
                    }
                }
                hwTimeContainer.removeAllViews();
                int maxLength = Math.max(hwTimeInList.size(), hwTimeOutList.size());
                for (int i = 0; i < maxLength; i++) {
                    if (i < hwTimeInList.size()) {
                        hwTimeInTxt = new TextView(mContext);
                        hwTimeInTxt.setText("Příchod " + (i + 1) + ": " + hwTimeInList.get(i));
                        hwTimeContainer.addView(hwTimeInTxt);
                    }

                    if (i < hwTimeOutList.size()) {
                        hwTimeOutTxt = new TextView(mContext);
                        hwTimeOutTxt.setText("Odchod " + (i + 1) + ": " + hwTimeOutList.get(i));
                        hwTimeContainer.addView(hwTimeOutTxt);
                    }
                }
                workShiftName = dayItem.getWorkShiftName();
                work = dayItem.getWork();
                pause = 0;

                if (dayItem.isRepair()) {
                    pause = (dayItem.getTube() != null && dayItem.getTube().size() > 1) ? Integer.parseInt(String.valueOf(dayItem.getTube().get(1).getLen())) : 0;
                    if (pause == 0 && dayItem.getTube() != null && !dayItem.getTube().isEmpty()) {
                        assert regList != null;
                        if(regList.get(0).getRegistrationId()==5) {
                           workShiftName = dayItem.getTube().get(0).getNames();
                       }
                    }
                } else if (dayItem.getTube() != null && dayItem.getTube().size() == 2) {
                    pause = Integer.parseInt(String.valueOf(dayItem.getTube().get(1).getLen()));
                }
                workShiftTxt = new TextView(mContext);
                workTxt = new TextView(mContext);
                breakTxt = new TextView(mContext);
                workShiftTxt.setText("Pracovní směna: " + workShiftName);
                hwTimeContainer.addView(workShiftTxt);
                workTxt.setText("Přítomnost: " + work / 60 + " hod.");
                hwTimeContainer.addView(workTxt);
                breakTxt.setText("Přestávka: " + pause + " min.");
                hwTimeContainer.addView(breakTxt);
                Log.d(TAG, "Příchod: " + hwTimeIn + " Odchod: " + hwTimeOut + " Pracovní směna: " + workShiftName + " Přítomnost: " + work / 60 + " Přestávka: " + pause);

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
