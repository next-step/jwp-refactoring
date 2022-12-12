package kitchenpos.utils;

import java.util.List;

public class TableInformation {
    private final String tableName;
    private final List<String> idColumnNames;

    public TableInformation(String tableName, List<String> idColumnNames) {
        this.tableName = tableName;
        this.idColumnNames = idColumnNames;
    }

    public String getTableName() {
        return tableName;
    }

    public List<String> getIdColumnNames() {
        return idColumnNames;
    }
}
