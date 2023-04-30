package cz.idio.api.cant;




import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cz.idio.CantFragment;
import cz.idio.R;
import cz.idio.api.response.CantResponse;
import cz.idio.api.response.Day;
import cz.idio.api.response.Vend;
import cz.idio.model.OnOrderButtonClickListener;



public class CantAdapter extends RecyclerView.Adapter<CantAdapter.ViewHolder> {
    public List<Vend> mVends;
    static Day vend;
    CantResponse cantResponse;
    private final LayoutInflater mInflater;
    Context mContext;
    private static List<Day> mday;
    private   String dayNameFood;
    private static OnOrderButtonClickListener mOrderButtonClickListener;
    public CantAdapter(Context context, OnOrderButtonClickListener listener) {
        mInflater = LayoutInflater.from(context);
        mVends = new ArrayList<>();
        cantResponse = new CantResponse();
        mContext = context;
        mOrderButtonClickListener = listener;
        setHasStableIds(true);
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.cant_card, parent, false);
        ViewHolder vh = new ViewHolder(itemView, mOrderButtonClickListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
         vend =mday.get(position);
        String dayName = vend.getDay();
        holder.dayTextView.setText(dayName);
        holder.orderButton.setPressed(false);
        holder.orderButton.setSelected(false);

        Log.d("CantAdapter", "Binding position: " + position);
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
        holder.orderButton.setTag(state);
        switch (state) {
            case 1:
                Log.d("CantAdapter", "State 1 at position: " + position);
                holder.orderButton.setText("žádná nabídka");
                holder.orderButton.setEnabled(false);
                holder.orderButton.setCompoundDrawables(null, null, null, null);
                break;
            case 2:
                Log.d("CantAdapter", "State 2 at position: " + position);
                holder.orderButton.setText("objednávka ukončena");
                holder.orderButton.setEnabled(false);
                break;
            case 3:
                Log.d("CantAdapter", "State 3 at position: " + position);
                holder.orderButton.setText("objednat");
                holder.orderButton.setEnabled(true);
               //holder.orderButton.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                holder.orderButton.setCompoundDrawables(null, null, null, null);
                break;
            case 4:
                Log.d("CantAdapter", "State 4 at position: " + position);
                holder.orderButton.setEnabled(true);
                holder.orderButton.setText(R.string.btn_label_cantel);
                // Získání ikony z vašich zdrojů
                Drawable icon = ContextCompat.getDrawable(mContext, android.R.drawable.checkbox_on_background);
                // Nastavení ikony nalevo od textu
                if (icon != null) {
                    icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
                    holder.orderButton.setCompoundDrawables(icon, null, null, null);
                }
                // Nastavení mezery mezi ikonou a textem
                holder.orderButton.setCompoundDrawablePadding((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, mContext.getResources().getDisplayMetrics()));
                //     orderButton.setTextColor(ContextCompat.getColor(getContext(), R.color.button_background_color));

                break;
            default:
                Log.d("CantAdapter", "State default at position: " + position);
                holder.orderButton.setEnabled(true);
                break;
        }
        vend.setOrderButton(holder.orderButton);

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


        public ViewHolder(View itemView, OnOrderButtonClickListener listener) {
            super(itemView);
            dayTextView = itemView.findViewById(R.id.dayTextView);
            foodSpinner = itemView.findViewById(R.id.foodSpinner);
            orderButton = itemView.findViewById(R.id.orderButton);
            orderButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        int buttonState = (int) view.getTag();
                        if (position != RecyclerView.NO_POSITION) {
                            Log.d("CantAdapter", "Clicked position: " + position + ", button state: " + buttonState);
                            listener.onOrderButtonClick(position);
                        }
                    }
                }
            });







        }
    }
    @Override
    public long getItemId(int position) {
        if (position >= 0 && position < mday.size()) {
            return mday.get(position).hashCode();
        } else {
            return RecyclerView.NO_ID;
        }
    }


}
