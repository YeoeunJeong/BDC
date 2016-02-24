package project.yeoeun.bdc.vo;

import java.util.List;

public class FusionTableList {

    /*
    "kind": "fusiontables#sqlresponse",
 "columns": [
  "rowid",
  "Product",
  "Inventory"
 ],
 "rows": [
  [
   "1",
   "Amber Bead",
   "1251500558"
  ],
  [
  ]
]
     */
    private String kind;
    private List<String> columns;
    private List<List<String>> rows;
//    private RowContent[] rows;

    public String getKind() {
        return kind;
    }

    public List<String> getColumns() {
        return columns;
    }

    public List<List<String>> getRows() {
        return rows;
    }
}
