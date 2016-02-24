package project.yeoeun.bdc.choosemenu;


import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import project.yeoeun.bdc.R;
import project.yeoeun.bdc.search.Item;
import project.yeoeun.bdc.search.DetailParser;
import project.yeoeun.bdc.util.Constants;
import project.yeoeun.bdc.util.GobobBaseActivity;
import project.yeoeun.bdc.util.ImageGenerator;
import project.yeoeun.bdc.vo.RestaurantMenu;

public class DetailActivity extends GobobBaseActivity {
    private Item mItem;
    private DetailParser mParser;
    private ArrayList<RestaurantMenu> mMenuList;

    private RestaurantMenu mMenu;
    String homePageUrl = null;
    //
    public static final String RESTAURANT_DETAIL_URL = "http://place.map.daum.net/";

    private String intro;
    private ArrayList basicList;
    private ArrayList detailList;
    private ArrayList<RestaurantMenu> menuList;
    private ActionBar mActionbar;

    @Bind(R.id.detail_restaurant_img)
    ImageView restaurantIv;

    @Bind(R.id.detail_basic_info_address)
    TextView addressTv;

    @Bind(R.id.detail_basic_info_phone1)
    TextView phone1Tv;

    @Bind(R.id.detail_basic_info_phone2)
    TextView phone2Tv;

    @Bind(R.id.detail_basic_info_phone_layout)
    LinearLayout phoneLayout;

    @Bind(R.id.detail_basic_info_homepage_url)
    TextView homepageTv;

    @Bind(R.id.detail_basic_info_distance)
    TextView distanceTv;

    @Bind(R.id.detail_basic_info_category)
    TextView categoryTv;

    @Bind(R.id.detail_menu_list)
    TextView menuTv;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.detail_menu_layout)
    LinearLayout menuLayout;

    @Bind(R.id.detail_menu_scroll_view)
    HorizontalScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        mItem = getItem();
//        mParser = new DetailParser(Integer.parseInt(mItem.id));
        menuList = new ArrayList<>();
        new ParserTask().execute();

        setActionbar(toolbar);
    }

    private void setActionbar(Toolbar toolbar) {
//        setSupportActionBar(toolbar);
        mActionbar = getSupportActionBar();
//        mActionbar.setDisplayHomeAsUpEnabled(true);
        mActionbar.setDisplayShowTitleEnabled(true);

//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
        mActionbar.setTitle(mItem.title);
    }


    private Item getItem() {
        return (Item) getIntent()
                .getBundleExtra(Constants.RESTAURANT_ITEM_BUNDLE)
                .getSerializable(Constants.RESTAURANT_ITEM);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private class ParserTask extends AsyncTask {
        String restaurantImageUrl = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            Document doc;
            try {
                // id # // class .
                doc = Jsoup.connect(RESTAURANT_DETAIL_URL + mItem.id).timeout(10000).get();

                restaurantImageUrl = doc.select("div.wrap_info div.box_info img#previewTarget").attr("src");
                homePageUrl = doc.select("div.wrap_info div.box_info dd#homepageToolTip").text();

                Log.i("****", restaurantImageUrl);
                Log.i("****", mItem.imageUrl);
                if (restaurantImageUrl.equals(mItem.imageUrl)) {
                    Log.i("****", "same");
                }

                Elements detailInfo = doc.select("div.section_detailinfo dl.list");
                for (Element item : detailInfo) {
                    Elements title = item.select("dt.screen_out");
                    Elements content = item.select("dd.desc");
                    Log.i("DetailAct 1", title.text() + " : " + content.text());
                }

                intro = doc.select("div#mainTab div.box_intro p.desc").text();
                Log.i("DetailAct 2-0", intro);

                Elements listInfo = doc.select("div#mainTab div.box_intro dl.list_info");
//                    Log.i("DetailAct RestaurantDetail1", listInfo.text());
                for (Element item : listInfo) {
                    Elements title = item.select("dt.tit");
                    Elements content = item.select("dd.cont");
                    Log.i("DetailAct 2", title.text() + " : " + content.text());

                }
                Log.i("DetailAct ", "");
                Elements listRelation = doc.select("div#mainTab div.box_intro dl.list_relation");
                Log.i("DetailAct 3", listRelation.text());
                for (Element item : listRelation) {
                    Elements title = item.select("dt.tit");
                    Elements content = item.select("dd.cont");
                    Log.i("DetailAct 4", title.text() + " : " + content.text());
                }

                Log.i("DetailAct ", "");

                Elements menuInfo = doc.select("div#mainTab div.box_intro div.section_menu li");
                for (Element item : menuInfo) {

                    String imgUrl = null;
                    String title = null;
                    String content = null;
                    if (item.select("img").attr("src") != null) {
                        imgUrl = item.select("img").attr("src");
                    }
                    if (item.select("span.tit").text() != null) {
                        title = item.select("span.tit").text();
                    }
                    if (item.select("strong.price").text() != null) {
                        content = item.select("strong.price").text();
                    }

                    RestaurantMenu menu = new RestaurantMenu(imgUrl, title, content);

                    menuList.add(menu);
                    Log.i("DetailAct 5", imgUrl.toString() + " : " + title + " : " + content);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (!restaurantImageUrl.isEmpty()) {
                setRestaurantImage(restaurantImageUrl);
            } else {
                restaurantIv.setVisibility(View.GONE);
            }

            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < menuList.size(); i++) {
                stringBuilder.append(menuList.get(i).getImgUrl());
                stringBuilder.append(" ");
                stringBuilder.append(menuList.get(i).getTitle());
                stringBuilder.append(" ");
                stringBuilder.append(menuList.get(i).getPrice());
                stringBuilder.append("\n");
            }
            menuTv.setText(stringBuilder.toString());

            setBasicInfo();
            setMenuInfo();
        }

        private void setRestaurantImage(String imageUrl) {
            ImageGenerator.getInstance().createImageService(imageUrl, restaurantIv);
        }

        private void setBasicInfo() {
            //title newAddress phone  direction distance

            distanceTv.setText("- 현재 위치로부터 약 " + (int) mItem.distance + "m 거리에 위치해있습니다");
            addressTv.setText("- " + mItem.newAddress + " (" + mItem.address + ")");
            String[] category = mItem.category.split(">");
            if (!mItem.phone.isEmpty()) {
                phone1Tv.setText("- 전화번호 : ");
                phone2Tv.setText(mItem.phone);
            } else {
                phoneLayout.setVisibility(View.GONE);
            }
            categoryTv.setText("- 카테고리 : " + category[category.length - 1].trim());
            if (!homePageUrl.isEmpty()) {
                homepageTv.setVisibility(View.VISIBLE);
                homepageTv.setText("- 웹페이지 : <u>" + homePageUrl + "</u>");
            }
        }

        private void setMenuInfo() {

            // menuItemLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.custom_menu_info, menuLayout);
//            ImageView menuIv = (ImageView) menuItemLayout.findViewById(R.id.custom_detail_menu_info_img);
//            TextView menuNameTv = (TextView) menuItemLayout.findViewById(R.id.custom_detail_menu_info_name);
//            TextView menuPriceTv = (TextView) menuItemLayout.findViewById(R.id.custom_detail_menu_info_price);


//            noticeIv.setImageResource(R.drawable.ready_menu);

            for (RestaurantMenu menu : menuList) {
                LinearLayout menuItemLayout = new LinearLayout(DetailActivity.this);

                menuItemLayout.setOrientation(LinearLayout.VERTICAL);
                menuItemLayout.setPadding(15, 15, 15, 15);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.FILL_PARENT);

                Resources r = getResources();
                float pxX = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, r.getDisplayMetrics());
                float pxY = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 160, r.getDisplayMetrics());
                layoutParams.width = (int) pxX;
                layoutParams.height = (int) pxY;
                layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
                layoutParams.weight = 1.0f;
                menuItemLayout.setLayoutParams(layoutParams);

                if (!menu.getImgUrl().isEmpty()) {
                    ImageView menuIv = new ImageView(DetailActivity.this);
                    menuIv.setLayoutParams(
                            new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT));
                    menuItemLayout.addView(menuIv);
                    ImageGenerator.getInstance().createImageService(menu.getImgUrl(), menuIv);
                }
                if (!menu.getTitle().isEmpty()) {
                    TextView menuNameTv = new TextView(DetailActivity.this);
                    menuNameTv.setText(menu.getTitle());
                    menuItemLayout.addView(menuNameTv);
                }
                if (!menu.getPrice().isEmpty()) {
                    TextView menuPriceTv = new TextView(DetailActivity.this);
                    menuPriceTv.setText(menu.getPrice());
                    menuItemLayout.addView(menuPriceTv);
                }

                menuLayout.addView(menuItemLayout);
            }
            if (!menuList.isEmpty()) {
                scrollView.setVisibility(View.VISIBLE);
            }
        }
    }

    @OnClick(R.id.detail_basic_info_phone2)
    public void phoneOnClick() {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mItem.phone));
//            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mStorePhoneNumber));
        startActivity(intent);
    }

    @OnClick(R.id.detail_basic_info_homepage_url)
    public void pageOnClick() {
        Intent intent = new Intent(Intent.ACTION_SEARCH);
        intent.putExtra(SearchManager.QUERY, homePageUrl);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }
}
