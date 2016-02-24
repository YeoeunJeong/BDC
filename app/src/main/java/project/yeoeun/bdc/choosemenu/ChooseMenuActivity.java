package project.yeoeun.bdc.choosemenu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.daum.mf.map.api.MapPoint;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import project.yeoeun.bdc.R;
import project.yeoeun.bdc.search.Item;
import project.yeoeun.bdc.search.OnFinishSearchListener;
import project.yeoeun.bdc.search.Searcher;
import project.yeoeun.bdc.util.Constants;
import project.yeoeun.bdc.util.GpsInfo;

public class ChooseMenuActivity extends AppCompatActivity {

    private ArrayList<String> mList;
    private ArrayList<TextView> mTvList;
    private String mChosenMenu;
    private GpsInfo mGpsInfo;
    private int mRestaurantCount;

    @Bind(R.id.choose_menu_et_layout)
    LinearLayout inputLayout;

    @Bind(R.id.choose_menu_added_list)
    LinearLayout listLayout;

    @Bind(R.id.choose_menu_et)
    EditText menuEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_menu);
        ButterKnife.bind(this);
        mList = new ArrayList<>();
        mTvList = new ArrayList<>();
    }

    @OnClick({R.id.choose_menu_add_btn, R.id.choose_menu_add_ok_btn, R.id.choose_menu_choose_btn})
    void mOnClick(View v) {
        switch (v.getId()) {
            case R.id.choose_menu_add_btn:
                inputLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.choose_menu_add_ok_btn:
                String input = menuEt.getText().toString();

                if (!StringUtils.isWhitespace(input)) {
                    hideSoftKeyboard(menuEt);

                    mList.add(input);
                    inputLayout.setVisibility(View.INVISIBLE);
                    TextView menuTv = new TextView(ChooseMenuActivity.this);
                    menuTv.setText(input);
                    menuTv.setLayoutParams(
                            new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT));
                    menuTv.setPadding(10, 10, 10, 10);
                    mTvList.add(menuTv);
                    listLayout.addView(menuTv);
                    menuEt.setText("");
                }
                break;
            case R.id.choose_menu_choose_btn:
                if (mList.size() >= 2) {
                    // Math.random() // n >= 0.0 && n < 1.0
                    int i = (int) (Math.random() * 100 % mList.size());
                    mChosenMenu = mList.get(i);

                    createDialog(getRestaurant(mChosenMenu));


                } else {
                    Toast.makeText(ChooseMenuActivity.this, "메뉴를 추가해주세요", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private int getRestaurant(final String chosenMenu) {
        mGpsInfo = new GpsInfo(ChooseMenuActivity.this);

        double latitude = 37; // 위도
        double longitude = 127; // 경도
        if (mGpsInfo.isGetLocation()) {
            latitude = mGpsInfo.getLatitude();
            longitude = mGpsInfo.getLongitude();
        } else {
            mGpsInfo.showSettingsAlert();
        }

        int radius = 1500; // 중심 좌표부터의 반경거리. 특정 지역을 중심으로 검색하려고 할 경우 사용. meter 단위 (0 ~ 10000)
        int page = 1; // 페이지 번호 (1 ~ 3). 한페이지에 15개
        String apikey = Constants.API_KEY;
        Searcher searcher = new Searcher(); // net.daum.android.map.openapi.search.Searcher
        searcher.searchKeyword(
                getApplicationContext(), chosenMenu, latitude, longitude, radius, page, apikey,
                new OnFinishSearchListener() {
                    @Override
                    public void onSuccess(List<Item> itemList) {
                        mRestaurantCount = itemList.size();
                    }

                    @Override
                    public void onFail() {
                        Toast.makeText(ChooseMenuActivity.this, "API_KEY의 제한 트래픽이 초과되었습니다.", Toast.LENGTH_SHORT).show();
                        mRestaurantCount = 0;
                    }
                });
        return  mRestaurantCount;
    }

    private void createDialog(int restaurantCount) {
        new AlertDialog.Builder(
                ChooseMenuActivity.this)
                .setTitle("골랐다")
                .setMessage("<" + mChosenMenu + "> (으)로 골랐습니다." + restaurantCount + "개의 음식점이 있습니다")
                .setPositiveButton("지도에서 위치 확인하러가기", mOnClickListener)
                .setNegativeButton("취소", null)
                .show();
    }


    private DialogInterface.OnClickListener mOnClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Intent intent = new Intent(ChooseMenuActivity.this, MapActivity.class);
            intent.putExtra(Constants.CHOSEN_MENU, mChosenMenu);
            startActivity(intent);
        }
    };

    private void hideSoftKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
