package cz.idio;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.ResourceBundle;

import cz.idio.databinding.FragmentSettingBinding;


public class SettingFragment extends Fragment {
    private FragmentSettingBinding binding;
    private Context mContext;
    private Spinner spinner;
    private EditText urlEdtText;
    private Button saveBtn;

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
       mContext = context;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(inflater, container, false);
        requireActivity().setTitle(mContext.getString(R.string.title_setting_fragment));
        SharedPreferences preferences = mContext.getSharedPreferences("data", Context.MODE_PRIVATE);
        urlEdtText = binding.editTextText;
        urlEdtText.setText(preferences.getString("url", ""));
        spinner = binding.spinner;
        int[] menuIds = {4, 2, 3, 1, 5};
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext, R.array.menu_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        int savedMenuId = preferences.getInt("vydejna", -1);
        int savedMenuPosition = -1;
        for (int i = 0; i < menuIds.length; i++) {
            if (menuIds[i] == savedMenuId) {
                savedMenuPosition = i;
                break;
            }
        }
        if (savedMenuPosition != -1) {
            spinner.setSelection(savedMenuPosition);
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int selectedMenuId = menuIds[position];
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("vydejna",selectedMenuId);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
        urlEdtText = binding.editTextText;
        saveBtn = binding.button;
        if (saveBtn != null) {
            saveBtn.setOnClickListener(v -> {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("url", urlEdtText.getText().toString());
                editor.apply();
            });
        }
        return binding.getRoot();
    }
}