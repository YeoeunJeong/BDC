package project.yeoeun.bdc.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import project.yeoeun.bdc.R;
import project.yeoeun.bdc.choosemenu.MapActivity;
import project.yeoeun.bdc.chooseperson.ChoosePersonActivity;
import project.yeoeun.bdc.chooseperson.PersonSettingActivity;
import project.yeoeun.bdc.network.FusionTableService;
import project.yeoeun.bdc.network.ServiceGenerator;
import project.yeoeun.bdc.util.Constants;
import project.yeoeun.bdc.util.CreateMenuCard;
import project.yeoeun.bdc.util.GobobBaseActivity;
import project.yeoeun.bdc.util.GpsInfo;
import project.yeoeun.bdc.vo.FusionTableInfo;
import project.yeoeun.bdc.vo.FusionTableList;
import project.yeoeun.bdc.vo.MenuCard;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends GobobBaseActivity {
    private ArrayList<String> mList;
    private ArrayAdapter<String> mAdapter;
    private int i;
    private String mChosenMenu;
    private View mPersonDialogView;
    private Spinner mSpinner1, mSpinner2;
    private HashMap<Integer, MenuCard> mCardHashMap;
    private FusionTableInfo mFusionTableInfo;
    private FusionTableList mFusionTableList;

    @Bind(R.id.main_swipe_view)
    SwipeFlingAdapterView flingContainer;
    @Bind(R.id.main_swipe_view_last)
    TextView lastMenuTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        checkFirstTime();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int getDeviceHeight_Pixel = displayMetrics.heightPixels;
        int getDeviceWidth_Pixel = displayMetrics.widthPixels;

        int getDeviceDpi = displayMetrics.densityDpi;

        Log.i("height, width, dpi", getDeviceHeight_Pixel + ", " + getDeviceWidth_Pixel + ", " + getDeviceDpi);
        createSwipeView();

        //ye
        requestRowFusionTable();
    }

    private void createSwipeView() {
        mList = new ArrayList<String>();
        addList();
        //choose your favorite adapter
        mAdapter = new ArrayAdapter<String>(this, R.layout.swipe_card_item, R.id.menuCardImg, mList);


        //set the listener and the adapter
        flingContainer.setAdapter(mAdapter);
        flingContainer.setFlingListener(mFlingListener);

        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                mChosenMenu = mList.get(itemPosition);
                createMenuDialog();
            }
        });
    }

    private void addList() {
        mCardHashMap = new CreateMenuCard().getCardHashMap();

//        String[] menuList = {"김치찌개", "설렁탕", "순대국", "파스타", "돈까스", "돈부리", "초밥", "찜닭", "감자탕", "해장국"};
        int[] randomList = new int[Constants.MENU_CARDS_COUNTS];
        int menuListSize = mCardHashMap.size();
        Random random = new Random();

        for (int i = 0; i < Constants.MENU_CARDS_COUNTS; i++) {
            randomList[i] = random.nextInt(menuListSize);
            for (int j = 0; j < i; j++) {
                if (randomList[i] == randomList[j]) {
                    i--;
                }
            }
        }

        for (int k : randomList) {
            mList.add(mCardHashMap.get(k).getMenuName());
//            mList.add(menuList[k]);
        }

    }

    private void createMenuDialog() {
        new AlertDialog.Builder(
                MainActivity.this)
                .setTitle(mList.get(0) + "(으)로 골랐습니다")
                .setMessage("근처에 <" + mList.get(0) + "> 음식점이 있나 보러갈까요?")
                .setPositiveButton("지도 보기", mMenuOnClickListener)
                .setNegativeButton("취소", null)
                .show();
    }

    private DialogInterface.OnClickListener mMenuOnClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            GpsInfo gpsInfo = new GpsInfo(MainActivity.this);
            if (gpsInfo.isGetLocation()) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                intent.putExtra(Constants.CHOSEN_MENU, mChosenMenu);
                startActivity(intent);
            } else {
                gpsInfo.showSettingsAlert();
            }
        }
    };

    private SwipeFlingAdapterView.onFlingListener mFlingListener = new SwipeFlingAdapterView.onFlingListener() {
        @Override
        public void removeFirstObjectInAdapter() {
            // this is the simplest way to delete an object from the Adapter (/AdapterView)
//            Log.d("LIST", "removed object!");
//            mList.remove(0);
//            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onLeftCardExit(Object dataObject) {
            //Do something on the left!
            //You also have access to the original object.
            //If you want to use it just cast it (String) dataObject

            mList.remove(0);
            mAdapter.notifyDataSetChanged();
            if (!checkLast()) {
                makeToast(MainActivity.this, "버려!");
            }
        }

        @Override
        public void onRightCardExit(Object dataObject) {
            mList.add(dataObject.toString());
            mList.remove(0);
            mAdapter.notifyDataSetChanged();
            if (!checkLast()) {
                makeToast(MainActivity.this, "고민!");
            }
        }

        @Override
        public void onAdapterAboutToEmpty(int itemsInAdapter) {
            // Ask for more data here
//            mList.add("치킨은 옳다".concat(String.valueOf(i)));
//            mAdapter.notifyDataSetChanged();
//            flingContainer.setVisibility(View.GONE);
            Log.d("LIST", "notified");
            i++;
        }

        @Override
        public void onScroll(float scrollProgressPercent) {
            View view = flingContainer.getSelectedView();
            if (view != null) {
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        }
    };

    @OnClick(R.id.main_choose_person_btn)
    public void mOnClick(View v) {
        if (v.getId() == R.id.main_choose_person_btn) {
            startActivity(new Intent(MainActivity.this, PersonSettingActivity.class));
            overridePendingTransition(R.anim.slide_up_in, R.anim.slide_up_out);
//            createPersonDialog();
        }
    }

    private void createPersonDialog() {
        LayoutInflater inflater = getLayoutInflater();
        mPersonDialogView = inflater.inflate(R.layout.dialog_person_layout, null);
        initDialogView();
        new AlertDialog.Builder(
                MainActivity.this)
                .setTitle("인원 수 설정")
//                .setMessage("준비 중 입니다")
                .setView(mPersonDialogView)
                .setPositiveButton("확인", mPersonOnClickListener)
                .setNegativeButton("취소", null)
                .show();
    }

    private void initDialogView() {
        mSpinner1 = (Spinner) mPersonDialogView.findViewById(R.id.person_dialog_spinner1);
        mSpinner2 = (Spinner) mPersonDialogView.findViewById(R.id.person_dialog_spinner2);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.total_person_count, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner1.setAdapter(adapter1);
        mSpinner1.setSelection(4);
        mSpinner1.setOnItemSelectedListener(mSpinner1OnSelectedListener);
    }

    private AdapterView.OnItemSelectedListener mSpinner1OnSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            setSpinner2(mSpinner1.getSelectedItem().toString());
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
        mSpinner2.setSelection(0);
        mSpinner2.setAdapter(adapter2);
    }

    private DialogInterface.OnClickListener mPersonOnClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Intent intent = new Intent(MainActivity.this, ChoosePersonActivity.class);
            intent.putExtra(Constants.PERSON_COUNT_TOTAL, mSpinner1.getSelectedItem().toString());
            intent.putExtra(Constants.PERSON_COUNT_WIN, mSpinner2.getSelectedItem().toString());
            startActivity(intent);
        }
    };


    private boolean checkLast() {
        for (int i = 0; i < mList.size(); i++) {
            Log.i("checkLast" + i, mList.get(i));
        }
        if (mList.size() == 1) {
            makeToast(this, "짠! 당신의 마음속에 남은 마지막 카드");
            flingContainer.setVisibility(View.GONE);
            lastMenuTv.setText(mList.get(0));
            lastMenuTv.setVisibility(View.VISIBLE);
            lastMenuTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createMenuDialog();
                }
            });
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    static void makeToast(Context ctx, String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }

    private void requestFusionTable() {
        FusionTableService fusionTableService = ServiceGenerator.getInstance().getFusionTableService();

        Call<FusionTableInfo> fusionTableCall = fusionTableService.getFusionTable(Constants.FUSION_TABLE_KEY);
        fusionTableCall.enqueue(mFusionTableCallBack);
    }

    Callback<FusionTableInfo> mFusionTableCallBack = new Callback<FusionTableInfo>() {
        @Override
        public void onResponse(Response<FusionTableInfo> response) {
            mFusionTableInfo = response.body();
            Log.i("FusionTableInfo", mFusionTableInfo.getName());
            Log.i("FusionTableInfo", mFusionTableInfo.getColumns().get(0).getName());
        }

        @Override
        public void onFailure(Throwable t) {
            t.printStackTrace();
        }
    };

    private void requestRowFusionTable() {
        FusionTableService fusionTableService = ServiceGenerator.getInstance().getFusionTableService();

        Call<FusionTableList> fusionTableCall = fusionTableService.getRowFusionTable(
                Constants.FUSION_TABLE_QUERY, Constants.FUSION_TABLE_KEY);
        fusionTableCall.enqueue(mRowFusionTableCallBack);
    }

    Callback<FusionTableList> mRowFusionTableCallBack = new Callback<FusionTableList>() {
        @Override
        public void onResponse(Response<FusionTableList> response) {
            mFusionTableList = response.body();

            mFusionTableList.getColumns();
            Log.i("FusionTableList", mFusionTableList.getColumns().get(1));
            Log.i("FusionTableList", mFusionTableList.getRows().get(0).get(1));
        }

        @Override
        public void onFailure(Throwable t) {
            t.printStackTrace();
        }
    };

    private void checkFirstTime() {
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        if (!pref.getBoolean("isFirst", false)) {
            SharedPreferences.Editor edit = pref.edit();
            edit.putBoolean("isFirst", true);
            edit.commit();

            RelativeLayout parent = (RelativeLayout) findViewById(R.id.main_parent);
            final LinearLayout guideLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.guide_layout, null);
            parent.addView(guideLayout);

            guideLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    guideLayout.setVisibility(View.GONE);
                }
            });
        }
    }
}
