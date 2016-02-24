package project.yeoeun.bdc.choosemenu;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.MapPOIItem;

import java.util.HashMap;

import project.yeoeun.bdc.R;
import project.yeoeun.bdc.search.Item;

public class CustomCalloutBalloonAdapter implements CalloutBalloonAdapter {
    private final View mCalloutBalloon;
    private Context mContext;
    private HashMap<Integer, Item> mTagItemMap = new HashMap<Integer, Item>();

    public  CustomCalloutBalloonAdapter(Context context, HashMap<Integer, Item> TagItemMap) {
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mCalloutBalloon = inflater.inflate(R.layout.custom_callout_balloon, null);
        mTagItemMap = TagItemMap;
    }

    @Override
    public View getCalloutBalloon(MapPOIItem poiItem) {
        ((ImageView) mCalloutBalloon.findViewById(R.id.custom_balloon_iv)).setImageResource(R.mipmap.ic_launcher);
        ((TextView) mCalloutBalloon.findViewById(R.id.custom_balloon_title_tv)).setText(poiItem.getItemName());
        ((TextView) mCalloutBalloon.findViewById(R.id.custom_balloon_address_tv)).setText("hello");

        Item item = mTagItemMap.get(poiItem.getTag());
        Log.i("CustomBalloonAdapter", poiItem.getItemName() + "," + poiItem.getTag());
        return mCalloutBalloon;
    }

    @Override
    public View getPressedCalloutBalloon(MapPOIItem poiItem) {
        Toast.makeText(mContext, "balloon clicked", Toast.LENGTH_SHORT).show();
        return null;
    }

}
