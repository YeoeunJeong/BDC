package project.yeoeun.bdc.vo;

import java.util.List;

public class FusionTableInfo {
/* "kind": "fusiontables#table",
 "tableId": "1e7y6mtqv891111111111_aaaaaaaaa_CvWhg9gc",
 "name": "Insects,
 "columns": [
 ]
  "description": "Insect Tracking Information.",
 "isExportable": true
 */
    String kind;
    String tableId;
    String name;
    List<Column> columns;
    String description;
    boolean isExportable;

    public String getKind() {
        return kind;
    }

    public String getTableId() {
        return tableId;
    }

    public String getName() {
        return name;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public String getDescription() {
        return description;
    }

    public boolean isExportable() {
        return isExportable;
    }
}
