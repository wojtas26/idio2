package cz.idio.api.cant;



import static android.app.PendingIntent.getActivity;
import static androidx.core.content.ContentProviderCompat.requireContext;
import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cz.idio.CantFragment;
import cz.idio.R;
import cz.idio.api.ApiClient;
import cz.idio.api.response.CantResponse;
import cz.idio.api.response.Day;
import cz.idio.api.response.MenuItemFood;
import cz.idio.api.response.Vend;
import cz.idio.model.ApiService;
import cz.idio.model.OnOrderButtonClickListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CantAdapter extends RecyclerView.Adapter<CantAdapter.ViewHolder> {
    public List<Vend> mVends;
    Day vend;
    CantResponse cantResponse;
    private final LayoutInflater mInflater;
    Context mContext;
    private List<Day> mday;
    private   String dayNameFood;
    private OnOrderButtonClickListener mOrderButtonClickListener;
    public CantAdapter(Context context,OnOrderButtonClickListener listener) {
        mInflater = LayoutInflater.from(context);
        mVends = new ArrayList<>();
        cantResponse = new CantResponse();
        mContext = context;
        mOrderButtonClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.cant_card, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
         vend =mday.get(position);
        String dayName = vend.getDay();
        holder.dayTextView.setText(dayName);

        if (!vend.getCantMenuItems().isEmpty()) {
            if (vend.getCantMenuItems().size() >= 2) {
                dayNameFood = vend.getCantMenuItems().get(1).getNameFood();
                // Access the elements as needed
                holder.foodSpinner.setText(dayNameFood);
            } else {
                // Handle the case when the list has only one element
                holder.foodSpinner.setText("Žádné jídlo není k dispozici");
            }
        } else {
            // Handle the case when the list is empty
            holder.foodSpinner.setText("Žádné jídlo není k dispozici");
        }


        int state =vend.getState();
        switch (state) {
            case 1:
                holder.orderButton.setText("žádná nabídka");
                holder.orderButton.setEnabled(false);
                break;
            case 2:
                holder.orderButton.setText("objednávka ukončena");
                holder.orderButton.setEnabled(false);
                break;
            case 3:
                holder.orderButton.setText("objednat");
                holder.orderButton.setEnabled(true);
               holder.orderButton.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                break;
            case 4:
                holder.orderButton.setText("zrušit objednávku");
                holder.orderButton.setEnabled(true);
                holder.orderButton.setTextColor(ContextCompat.getColor(mContext, R.color.button_background_color));

                break;
            default:
                holder.orderButton.setEnabled(true);
                break;
        }
        vend.setOrderButton(holder.orderButton);
        holder.orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOrderButtonClickListener != null) {
                    mOrderButtonClickListener.onOrderButtonClick(position);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mday == null) { // Check if the list is null
            return 0;
        }
        return mday.size();
    }


    @SuppressLint("NotifyDataSetChanged")
    public void setVends(List<Vend> mDay) {
         mVends = mDay;
        notifyDataSetChanged();
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setDays(List<Day> days) {
        mday = days;
        notifyDataSetChanged();
    }

    public void setOnOrderButtonClickListener(CantFragment cantFragment) {
        mOrderButtonClickListener = (OnOrderButtonClickListener) cantFragment;

    }

    public Day getDay(int position) {
        return mday.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView dayTextView;
        public final TextView foodSpinner;
        public final Button orderButton;


        public ViewHolder(View itemView) {
            super(itemView);
            dayTextView = itemView.findViewById(R.id.dayTextView);
            foodSpinner = itemView.findViewById(R.id.foodSpinner);
            orderButton = itemView.findViewById(R.id.orderButton);
        }


    }
}
