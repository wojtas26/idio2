package cz.idio.api.cant;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.idio.R;
import cz.idio.api.ApiClient;
import cz.idio.api.response.CantResponse;
import cz.idio.api.response.Day;

import cz.idio.api.response.MenuItemFood;
import cz.idio.api.response.Vend;
import cz.idio.model.ApiService;
import retrofit2.Response;

public class CantAsyncTask extends AsyncTask<String, Void, List<Vend>> {
    @SuppressLint("StaticFieldLeak")
    private Context mContext ;
    private CantAdapter mAdapter;
    private ProgressDialog mProgressDialog;
   public String foodIndex;
   private String selectedVendName ;
    private SharedPreferences preferences;
    private String login;
    private String pwd;
    public CantAsyncTask(Context context, CantAdapter adapter) {
        mContext = context;
        mAdapter = adapter;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setTitle("Probíhá komunikace se serverem");
        mProgressDialog.setMessage("Načítám data ...");
        mProgressDialog.show();
    }

    @Override
    protected List<Vend> doInBackground(String... params) {
        preferences = mContext.getSharedPreferences("data",Context.MODE_PRIVATE);
        login = preferences.getString("username", "");
        pwd = preferences.getString("pwdSha1", "");
        String mod = params[0];
        String cmd = params[1];
        String complogin = params[2];
                login = params[3];
                pwd = params[4];
        String date = params[5];
        selectedVendName = params[6];

        List<Vend> vends = new ArrayList<>();


        try {
            ApiService apiService = ApiClient.getClient(mContext).create(ApiService.class);
            Response<CantResponse> response = apiService.getCantData(mod, cmd, complogin, login, pwd, date).execute();

            if (response.isSuccessful() && response.body() != null) {
                List<Vend> vendList = response.body().getVends(); // seznam prodejen
                for (Vend vend : vendList) {
                    List<Day> days = vend.getDays();
                    for (Day day : days) {
                        int cantMenuDayId = day.getCantMenuDayId();
                        Response<CantResponse> responseMenu = apiService.getCantDataMenu(mod, "GetMenu", complogin, login, pwd, date, cantMenuDayId).execute();
                        if (responseMenu.isSuccessful() && responseMenu.body() != null) {
                            List<MenuItemFood> cantMenuItems = responseMenu.body().getCantMenuItems(); // seznam položek pro daný den
                            day.setCantMenuItems(cantMenuItems); // uložení seznamu položek do denního menu
                        }
                    }
                }
                vends.addAll(vendList); // přidání seznamu prodejen do seznamu vends
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return vends;
    }
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onPostExecute(List<Vend> vends) {
        super.onPostExecute(vends);
        // Přidání dat pro jednotlivé dny
        List<Vend> filteredVends = new ArrayList<>();
        List<Day> mday = null;
        for (Vend vend : vends) {
            if (vend.getName().equals(selectedVendName)) {
                filteredVends.add(vend);
                // Přidání dat pro jednotlivé dny
                mday = new ArrayList<>();
                for (Day day : vend.getDays()) {
                    int dayIndex = day.getPos();
                     foodIndex = String.valueOf(day.getCantMenuDayId());
                    if (dayIndex >= 0 && dayIndex <= 6) {
                        String[] daysOfWeek = mContext.getResources().getStringArray(R.array.days_of_week);
                        day.setDay(daysOfWeek[dayIndex]);
                    } else {
                        day.setDay(""); // nebo jiná výchozí hodnota
                    }

                    mday.add(day);
                }
                vend.setDays(mday);
            }
        }
// Naplnění RecyclerViewu daty
        mAdapter.setVends(filteredVends);
        mAdapter.setDays(mday);
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        mAdapter.notifyDataSetChanged();
    }
}
