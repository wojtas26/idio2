package cz.idio;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cz.idio.api.ApiClient;
import cz.idio.api.cant.CantAdapter;
import cz.idio.api.cant.CantAsyncTask;
import cz.idio.api.response.CantResponse;
import cz.idio.api.response.Day;
import cz.idio.databinding.FragmentCantBinding;
import cz.idio.model.ApiService;
import cz.idio.model.OnOrderButtonClickListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CantFragment extends Fragment implements View.OnClickListener, OnOrderButtonClickListener {
    private FragmentCantBinding binding;
    private Context mContext;
    private RecyclerView recyclerView;
    private Spinner mSpinner;
    private CantAdapter adapter;
    private Button btnBack, btnActual, btnNext;
    private TextView textDate;
    private Date currentDate;
   private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    private SimpleDateFormat sdfTask = new SimpleDateFormat("yyyy-MM-dd");
   private SharedPreferences preferences;
   private String login;
    private String pwd;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        preferences = mContext.getSharedPreferences("data",Context.MODE_PRIVATE);
        login = preferences.getString("username", "");
        pwd = preferences.getString("pwdSha1", "");
        binding = FragmentCantBinding.inflate(inflater, container, false);
        requireActivity().setTitle(mContext.getString(R.string.title_cant_fragment));
        recyclerView = binding.recyclerView;
        mSpinner = binding.menuSpinner;
        adapter = new CantAdapter(mContext, this);
        textDate = binding.textViewDate;
        adapter.setOnOrderButtonClickListener(this);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        currentDate = cal.getTime();
        String formattedDate = sdf.format(currentDate);
        textDate.setText(mContext.getString(R.string.date_cant_fragment)+ " " + formattedDate);
        btnBack = binding.buttonBack;
        btnActual = binding.btnActual;
        btnNext = binding.btnNext;
        btnBack.setOnClickListener(this);
        btnActual.setOnClickListener(this);
        btnNext.setOnClickListener(this);


        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                mContext,
                R.array.menu_items,
                android.R.layout.simple_spinner_item
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(spinnerAdapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Retrieve the Vend name from the selected item
                String selectedVend = parent.getItemAtPosition(position).toString();

                // Fetch data for selected Vend
                CantAsyncTask task = new CantAsyncTask(requireContext(), adapter);
                task.execute("Cant", "GetVend", " ", login, pwd, sdfTask.format(currentDate), selectedVend);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @SuppressLint("SetTextI18n")
    private void updateDateLabel() {
        String formattedDate = sdf.format(currentDate);
        textDate.setText(mContext.getString(R.string.date_cant_fragment)+ " " + formattedDate);
    }


    @SuppressLint("NonConstantResourceId")
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonBack:
                currentDate = getPreviousMonday();
                updateDateLabel();
                break;
            case R.id.btnActual:
                currentDate = getCurrentMonday();
                updateDateLabel();

                break;
            case R.id.btnNext:
                currentDate = getNextMonday();
                updateDateLabel();
                break;
        }
        // Retrieve the selected Vend from the spinner
        String selectedVend = mSpinner.getSelectedItem().toString();

        // Fetch data for selected Vend and new date
        CantAsyncTask task = new CantAsyncTask(requireContext(), adapter);
        task.execute("Cant", "GetVend", " ", login, pwd, sdfTask.format(currentDate), selectedVend);


    }

    private Date getCurrentMonday() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return cal.getTime();
    }

    private Date getNextMonday() {
        Date currentDate1 = currentDate;
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate1);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.add(Calendar.DATE, 7);
        return cal.getTime();
    }

    private Date getPreviousMonday() {
        Date currentDate1 = currentDate;
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate1);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.add(Calendar.DATE, -7);
        return cal.getTime();
    }


    public void onOrderButtonClick(int position) {
        Day day = adapter.getDay(position);
         day.getOrderButton();
       if (day.getState()==3){
           String mod = "Cant";
           String cmd = "AddDelMenu";
           String complogin = " ";
        String cantMenu_Id = day.getCantMenuItems().get(1).getCantMenuId();
        String pos = String.valueOf(day.getCantMenuItems().get(1).getPos());
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<CantResponse> call = apiService.getAddDelMenu(mod, cmd, complogin, login, pwd, cantMenu_Id, pos);
        call.enqueue(new Callback<CantResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<CantResponse> call, Response<CantResponse> response) {
                if (response.isSuccessful()) {
                    CantResponse addDelMenuResponse = response.body();
                    int status = addDelMenuResponse.getCantStatus();
                    setOrderButtonText(day, status);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<CantResponse> call, Throwable t) {

            }
        });
    }else if (day.getState()==4){
           String mod = "Cant";
           String cmd = "AddDelMenu";
           String complogin = "";
           String cantMenu_Id = "-"+day.getCantMenuItems().get(1).getCantMenuId();
           String pos = String.valueOf(day.getCantMenuItems().get(1).getPos());
           ApiService apiService = ApiClient.getClient().create(ApiService.class);
           Call<CantResponse> call = apiService.getAddDelMenu(mod, cmd, complogin, login, pwd, cantMenu_Id, pos);
           call.enqueue(new Callback<CantResponse>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(Call<CantResponse> call, Response<CantResponse> response) {
                    CantResponse addDelMenuResponse = response.body();
                    int status = addDelMenuResponse.getCantStatus();
                    setOrderButtonText(day, status);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<CantResponse> call, Throwable t) {

                }
            });
        }else if (day.getState()==2){
           String mod = "Cant";
           String cmd = "GetOrder";
           String complogin = "";
           String cantMenu_Id = "-"+day.getCantMenuItems().get(1).getCantMenuId();
           String pos = String.valueOf(day.getCantMenuItems().get(1).getPos());
           ApiService apiService = ApiClient.getClient().create(ApiService.class);
           Call<CantResponse> call = apiService.getAddDelMenu(mod, cmd, complogin, login, pwd, cantMenu_Id, pos);
           call.enqueue(new Callback<CantResponse>() {
               @SuppressLint("NotifyDataSetChanged")
               @Override
               public void onResponse(Call<CantResponse> call, Response<CantResponse> response) {
                   CantResponse addDelMenuResponse = response.body();
                   int status = addDelMenuResponse.getCantStatus();
                   setOrderButtonText(day, status);
                   adapter.notifyDataSetChanged();
               }

               @Override
               public void onFailure(Call<CantResponse> call, Throwable t) {

               }
           });
       }

    }

    public void setOrderButtonText(Day day, int status) {
        Button orderButton = day.getOrderButton();
        switch (status) {
            case 1:
                if (orderButton != null) {
                    day.setState(4);
                    orderButton.setText(R.string.btn_label_cantel);
                    orderButton.setEnabled(true);
                    // Získání ikony z vašich zdrojů
                    Drawable icon = ContextCompat.getDrawable(mContext, android.R.drawable.checkbox_on_background);
                    // Nastavení ikony nalevo od textu
                    if (icon != null) {
                        icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
                        orderButton.setCompoundDrawables(icon, null, null, null);
                    }
                    // Nastavení mezery mezi ikonou a textem
                    orderButton.setCompoundDrawablePadding((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    //orderButton.setTextColor(ContextCompat.getColor(getContext(), R.color.button_background_color));
                }
                Toast.makeText(getContext(), R.string.toast_label_order, Toast.LENGTH_SHORT).show();
                break;
            case 2:
                orderButton.setText("Na burze");
                // orderButton.setBackgroundResource(R.drawable.button_background_burza);
                break;
            case 3:
                Toast.makeText(getContext(), R.string.toast_label_order1, Toast.LENGTH_SHORT).show();
                break;
            case 4:
                Toast.makeText(getContext(), "Více objednávek v této sekci nelze uskutečnit", Toast.LENGTH_SHORT).show();
                break;
            case 5:
                Toast.makeText(getContext(), "Vaše strávnická kategorie má zakázáno objednávat stravu", Toast.LENGTH_SHORT).show();
                break;
            case 6:
                Toast.makeText(getContext(), "Nemáte dostatečný kredit pro objednání stravy", Toast.LENGTH_SHORT).show();
                break;
            case 7:
                Toast.makeText(getContext(), "Není co odebrat", Toast.LENGTH_SHORT).show();
                break;
            case 8:
                Toast.makeText(getContext(), "Vloženo na burzu", Toast.LENGTH_SHORT).show();
                break;
            case 9:

                if (orderButton != null) {
                    day.setState(3);
                    orderButton.setText(R.string.btn_order_label);
                    orderButton.setEnabled(true);
                    orderButton.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                }
                Toast.makeText(getContext(), "Položka byla odebrána z objednávky", Toast.LENGTH_SHORT).show();
                // orderButton.setBackgroundResource(R.color.button_background_color);
                break;
        }
        adapter.getItemId(status);
    }
}

