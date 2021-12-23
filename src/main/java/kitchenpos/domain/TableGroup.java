package kitchenpos.domain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TableGroup {
    private static final String KEY_COLUMN_NAME = "id";

    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable> orderTables;

    protected TableGroup() {}

    public TableGroup(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroup from(final ResultSet resultSet) throws SQLException {
        return new TableGroup(resultSet.getLong(KEY_COLUMN_NAME), resultSet.getObject("created_date", LocalDateTime.class), new ArrayList<>());
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }
}
