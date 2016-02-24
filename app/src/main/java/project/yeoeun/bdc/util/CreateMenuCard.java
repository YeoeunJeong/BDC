package project.yeoeun.bdc.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import project.yeoeun.bdc.R;
import project.yeoeun.bdc.vo.MenuCard;

public class CreateMenuCard {
    private List<MenuCard> menuCardList;
    private  HashMap<Integer, MenuCard> mCardHashMap = new HashMap<>();

    public CreateMenuCard() {
        int i = 0;
        menuCardList = new ArrayList<>();
        menuCardList.add(new MenuCard(i++, R.drawable.menu_card_0_pasta, "파스타"));
        menuCardList.add(new MenuCard(i++, R.drawable.menu_card_1_seol_lung_tang, "설렁탕"));
        menuCardList.add(new MenuCard(i++, R.drawable.menu_card_2_don_ggas, "돈까스"));
        menuCardList.add(new MenuCard(i++, R.drawable.menu_card_3_gam_ja_tang, "감자탕"));
        menuCardList.add(new MenuCard(i++, R.drawable.menu_card_4_zzim_darg, "찜닭"));
        menuCardList.add(new MenuCard(i++, R.drawable.menu_card_5_sushi, "초밥"));
        menuCardList.add(new MenuCard(i++, R.drawable.menu_card_6_don_bu_ri, "돈부리"));
        menuCardList.add(new MenuCard(i++, R.drawable.menu_card_7_pizza, "피자"));
        menuCardList.add(new MenuCard(i++, R.drawable.menu_card_8_sun_dae_gug_bab, "순대국"));
        menuCardList.add(new MenuCard(i++, R.drawable.menu_card_9_dduk_bok_i, "떡볶이"));
        menuCardList.add(new MenuCard(i++, R.drawable.menu_card_10_kimchi_zzi_gae, "김치찌개"));

        for (i = 0; i < menuCardList.size(); i++) {
            mCardHashMap.put(menuCardList.get(i).getId(), menuCardList.get(i));
        }
    }

    public  HashMap<Integer, MenuCard> getCardHashMap() {
        return mCardHashMap;
    }
}
