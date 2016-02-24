package project.yeoeun.bdc.vo;

public class Column {
    /*
    "kind": "fusiontables#column",
       "columnId": 0,
       "name": "Species",
       "type": "STRING"
     */
    private String kind;
    private int columnId;
    private String name;
    private String type;

    public String getKind() {
        return kind;
    }

    public int getColumnId() {
        return columnId;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
