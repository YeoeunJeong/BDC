package project.yeoeun.bdc.chooseperson;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import project.yeoeun.bdc.R;
import project.yeoeun.bdc.main.MainActivity;
import project.yeoeun.bdc.util.Constants;
import project.yeoeun.bdc.util.GobobBaseActivity;

public class PersonSettingActivity extends GobobBaseActivity {
    //private Spinner spinner1;
//private Spinner spinner2;
    @Bind(R.id.person_setting_spinner1)
    Spinner spinner1;

    @Bind(R.id.person_setting_spinner2)
    Spinner spinner2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_setting);

        ButterKnife.bind(this);
        initDialogView();
    }


    private void initDialogView() {
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.total_person_count, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner1.setSelection(4);
        spinner1.setOnItemSelectedListener(mSpinner1OnSelectedListener);
    }

    private AdapterView.OnItemSelectedListener mSpinner1OnSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            setSpinner2(spinner1.getSelectedItem().toString());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private void setSpinner2(String selectedItem) {
        int totalCount = Integer.parseInt(selectedItem) - 1;
        String[] array = new String[totalCount];
        for (int i = 0; i < totalCount; i++) {
            array[i] = String.valueOf(i + 1);
        }
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, array);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setSelection(0);
        spinner2.setAdapter(adapter2);
    }

    @OnClick(R.id.person_setting_start_btn)
    public void mOnClick(View view) {
        if (view.getId() == R.id.person_setting_start_btn) {
            Intent intent = new Intent(PersonSettingActivity.this, ChoosePersonActivity.class);
            intent.putExtra(Constants.PERSON_COUNT_TOTAL, spinner1.getSelectedItem().toString());
            intent.putExtra(Constants.PERSON_COUNT_WIN, spinner2.getSelectedItem().toString());
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        overridePendingTransition(R.anim.slide_down_in, R.anim.slide_down_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
