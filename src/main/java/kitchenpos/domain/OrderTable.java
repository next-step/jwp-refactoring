package kitchenpos.domain;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderTable {
    private static final String KEY_COLUMN_NAME = "id";

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    protected OrderTable() {}

    public OrderTable(long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable from(final ResultSet resultSet) throws SQLException {
        return new OrderTable(resultSet.getLong(KEY_COLUMN_NAME), resultSet.getObject("table_group_id", Long.class)
                , resultSet.getInt("number_of_guests"), resultSet.getBoolean("empty"));
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public void setTableGroupId(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }
}
