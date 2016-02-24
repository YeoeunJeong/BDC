package project.yeoeun.bdc.choosemenu;


import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import net.daum.mf.map.api.CameraPosition;
import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapView;
import net.daum.mf.map.api.MapView.POIItemEventListener;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import project.yeoeun.bdc.R;
import project.yeoeun.bdc.search.Item;
import project.yeoeun.bdc.search.OnFinishSearchListener;
import project.yeoeun.bdc.search.Searcher;
import project.yeoeun.bdc.util.Constants;
import project.yeoeun.bdc.util.GobobBaseActivity;
import project.yeoeun.bdc.util.GpsInfo;


public class MapActivity extends GobobBaseActivity implements POIItemEventListener {
    private MapView mMapView;
    private String mQuery;
    private HashMap<Integer, Item> mTagItemMap = new HashMap<Integer, Item>();
    private GpsInfo mGpsInfo;
    private ActionBar mActionbar;
    @Bind(R.id.map_view)
    ViewGroup mapViewContainer;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);

        mQuery = getIntent().getStringExtra(Constants.CHOSEN_MENU);

        init();
        showRestaurant();
    }

    private void init() {
        setActionbar(toolbar);
        mActionbar.setTitle(mQuery);

        mMapView = new MapView(this);
        mMapView.setDaumMapApiKey(Constants.API_KEY);

        mapViewContainer.addView(mMapView);
    }

    private void setActionbar(Toolbar toolbar) {
//        setSupportActionBar(toolbar);
        mActionbar = getSupportActionBar();
//        mActionbar.setDisplayHomeAsUpEnabled(true);
        mActionbar.setDisplayShowTitleEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void mapInit() {
        if (mGpsInfo.isGetLocation()) {
            mMapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(mGpsInfo.getLatitude(), mGpsInfo.getLongitude()), true);
            mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
            mMapView.setPOIItemEventListener(this);
//        mMapView.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter(this, mTagItemMap));
            mMapView.setZoomLevel(3, true);
            mMapView.zoomIn(true);
            mMapView.zoomOut(true);
        } else {
//            mGpsInfo.showSettingsAlert();
            Toast.makeText(MapActivity.this, "현재 위치를 찾지 못했습니다.", Toast.LENGTH_SHORT).show();
        }

    }


    private void showRestaurant() {
        mGpsInfo = new GpsInfo(MapActivity.this);
        double latitude = mGpsInfo.getLatitude(); // 위도
        double longitude = mGpsInfo.getLongitude(); // 경도
        int radius = 1500; // 중심 좌표부터의 반경거리. 특정 지역을 중심으로 검색하려고 할 경우 사용. meter 단위 (0 ~ 10000)
        int page = 1; // 페이지 번호 (1 ~ 3). 한페이지에 15개
        String apikey = Constants.API_KEY;
        Searcher searcher = new Searcher(); // net.daum.android.map.openapi.search.Searcher
        searcher.searchKeyword(
                getApplicationContext(), mQuery, latitude, longitude, radius, page, apikey,
                new OnFinishSearchListener() {
                    @Override
                    public void onSuccess(List<Item> itemList) {
                        mMapView.removeAllPOIItems(); // 기존 검색 결과 삭제
                        mapInit();
                        showResult(itemList); // 검색 결과 보여줌
                    }

                    @Override
                    public void onFail() {
//                showToast("API_KEY의 제한 트래픽이 초과되었습니다.");
                        Toast.makeText(MapActivity.this, "API_KEY의 제한 트래픽이 초과되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showResult(List<Item> itemList) {
        MapPointBounds mapPointBounds = new MapPointBounds();

        Log.i("map activity", itemList.size() + "개");

        int listSize = itemList.size();
        int count;
        if (listSize == 0) {
//            Toast.makeText(MapActivity.this, "다시 시도해 주십시오", Toast.LENGTH_SHORT).show();
            Log.i("MapActivity, showResult", "list Size 0");
            count = listSize;
//            return;
        } else if (listSize > 0 && listSize < Constants.SHOW_RESTAURANT_COUNTS) {
            count = listSize;
        } else {
            count = Constants.SHOW_RESTAURANT_COUNTS;
        }

        for (int i = 0; i < count; i++) {
//        for (int i = 0; i < itemList.size(); i++) {
            Item item = itemList.get(i);
            Log.i("map activity", i + "," + item.title);

            MapPOIItem poiItem = new MapPOIItem();
            poiItem.setItemName(item.title);
            poiItem.setTag(i);
            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(item.latitude, item.longitude);
            poiItem.setMapPoint(mapPoint);
            mapPointBounds.add(mapPoint);
            poiItem.setMarkerType(MapPOIItem.MarkerType.CustomImage);
            poiItem.setCustomImageResourceId(R.drawable.map_pin_unselected);

            poiItem.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
            poiItem.setCustomSelectedImageResourceId(R.drawable.map_pin_selected);
            poiItem.setCustomImageAutoscale(false);

//            poiItem.setCustomImageAnchor(0.5f, 1.0f);

            mMapView.addPOIItem(poiItem);
            mTagItemMap.put(poiItem.getTag(), item);
        }
//        mMapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds));
        mMapView.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(MapPoint.mapPointWithGeoCoord(mGpsInfo.getLatitude(), mGpsInfo.getLongitude()), 2)));


        MapPOIItem[] poiItems = mMapView.getPOIItems();
//        if (poiItems.length > 0) {
//            mMapView.selectPOIItem(poiItems[0], false);
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
        Log.i("onPOIItemSelected", mapPOIItem.getItemName());
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
        Log.i("onPOIItemTouched1", mapPOIItem.getItemName());
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {
        Item item = mTagItemMap.get(mapPOIItem.getTag());

        /*
        title imageUrl address newAddress;
        zipcode phone longitude latitude;
        distance category id;
        placeUrl direction addressBCode;
         */
        Intent intent = new Intent(MapActivity.this, DetailActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.RESTAURANT_ITEM, item);
        intent.putExtra(Constants.RESTAURANT_ITEM_BUNDLE, bundle);
        startActivity(intent);
    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {
        Log.i("onDraggablePOIItemMoved", mapPOIItem.getItemName());
        Toast.makeText(this, "Moved " + mapPOIItem.getItemName() + " Callout Balloon", Toast.LENGTH_SHORT).show();
    }


}
