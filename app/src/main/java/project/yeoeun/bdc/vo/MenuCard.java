package project.yeoeun.bdc.vo;

public class MenuCard {
    private int id;
    private int imgId;
    private String menuName;

    public MenuCard(int id, int imgId, String menuName) {
        this.id = id;
        this.imgId = imgId;
        this.menuName = menuName;
    }

    public int getId() {
        return id;
    }

    public int getImgId() {
        return imgId;
    }

    public String getMenuName() {
        return menuName;
    }
}
