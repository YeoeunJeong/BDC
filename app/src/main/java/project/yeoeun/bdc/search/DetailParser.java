package project.yeoeun.bdc.search;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import project.yeoeun.bdc.vo.RestaurantMenu;

public class DetailParser {
    public static final String RESTAURANT_DETAIL_URL = "http://place.map.daum.net/";

    private int mId;
    private String intro;
    private ArrayList basicList;
    private ArrayList detailList;
    private ArrayList<RestaurantMenu> menuList;

    public DetailParser(int mId) {
        this.mId = mId;
        getData();

        menuList = new ArrayList<>();
    }

    private void getData() {
//            Elements infoElements = restaurant.select("dc-restaurant-info");
//
//            Elements nameTag = contentElements.select("div.dc-restaurant-name");
//            Elements categorieTag = contentElements.select("div.dc-restaurant-category");
//            Elements infoTag = infoElements.select("div.dc-restaurant-info");

//            String name = nameTag.text();
//            String link = nameTag.select("a").attr("abs:href");
//            String category = categorieTag.text();
//            String addr = infoTag.get(1).select("div.dc-restaurant-info-text").text();
//            String phone = infoTag.get(2).select("div.dc-restaurant-info-text").text();


        Thread downloadThread = new Thread() {
            public void run() {
                Document doc;
                try {
                    // id # // class .
                    doc = Jsoup.connect(RESTAURANT_DETAIL_URL + mId).timeout(10000).get();

                    Elements detailInfo = doc.select("div.section_detailinfo dl.list");
                    for (Element item : detailInfo) {
                        Elements title = item.select("dt.screen_out");
                        Elements content = item.select("dd.desc");
                        Log.i("RestaurantDetail1", title.text() + " : " + content.text());
                    }

                    intro = doc.select("div#mainTab div.box_intro p.desc").text();
                    Log.i("RestaurantDetail2-0", intro);

                    Elements listInfo = doc.select("div#mainTab div.box_intro dl.list_info");
//                    Log.i("RestaurantDetail1", listInfo.text());
                    for (Element item : listInfo) {
                        Elements title = item.select("dt.tit");
                        Elements content = item.select("dd.cont");
                        Log.i("RestaurantDetail2", title.text() + " : " + content.text());

                    }
                    Log.i("RestaurantDetail", "");
                    Elements listRelation = doc.select("div#mainTab div.box_intro dl.list_relation");
                    Log.i("RestaurantDetail3", listRelation.text());
                    for (Element item : listRelation) {
                        Elements title = item.select("dt.tit");
                        Elements content = item.select("dd.cont");
                        Log.i("RestaurantDetail4", title.text() + " : " + content.text());
                    }

                    Log.i("RestaurantDetail", "");

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
                        Log.i("RestaurantDetail5", imgUrl.toString() + " : " + title + " : " + content);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        };
        downloadThread.start();
    }

    public String getIntro(){
        return intro;
    }

    public ArrayList getBasicList() {

        return basicList;
    }

    public ArrayList getDetailList() {

        return detailList;
    }

    public ArrayList getMenuList() {
        return menuList;
    }
}

