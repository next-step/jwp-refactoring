package kitchenpos.table.utils;

public class TablePrimaryKey {

    private String tableName;
    private String primaryKey;

    public TablePrimaryKey(String tableName, String primaryKey) {
        this.tableName = tableName;
        this.primaryKey = primaryKey;
    }

    public String getTableName() {
        return tableName;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

}
