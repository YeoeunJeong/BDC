package project.yeoeun.bdc.chooseperson;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import project.yeoeun.bdc.R;

public class CardTestActivity extends AppCompatActivity {
    private ArrayList<String> mList;
    private ArrayAdapter<String> mAdapter;
    private int i;

    @Bind(R.id.choose_man_swipe_view)
    SwipeFlingAdapterView flingContainer;

    // https://github.com/Diolor/Swipecards
    // https://github.com/inmyfree/awsome-3D-listview //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_test);
        ButterKnife.bind(this);

        mList = new ArrayList<String>();
        mList.add("떡볶이");
        mList.add("중국집");
        mList.add("곱창");
        mList.add("칼국수");

        //choose your favorite adapter
        mAdapter = new ArrayAdapter<String>(this, R.layout.swipe_card_item, R.id.menuCardImg, mList);

//        new ArrayAdapter<>(HashMap);
        //set the listener and the adapter
        flingContainer.setAdapter(mAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                mList.remove(0);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                makeToast(CardTestActivity.this, "Left!");
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                makeToast(CardTestActivity.this, "Right!");
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
                mList.add("치킨은 옳다".concat(String.valueOf(i)));
                mAdapter.notifyDataSetChanged();
                Log.d("LIST", "notified");
                i++;
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });

        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                makeToast(CardTestActivity.this, "선택하였습니다!");
            }
        });
    }

    @OnClick(R.id.right)
    public void right() {
        /**
         * Trigger the right event manually.
         */
        flingContainer.getTopCardListener().selectRight();
    }

    @OnClick(R.id.left)
    public void left() {
        flingContainer.getTopCardListener().selectLeft();
    }

    static void makeToast(Context ctx, String s){
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
