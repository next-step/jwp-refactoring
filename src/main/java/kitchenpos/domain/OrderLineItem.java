package kitchenpos.domain;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderLineItem {
    private static final String KEY_COLUMN_NAME = "seq";

    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    protected OrderLineItem() {}

    public OrderLineItem(final Long seq, final Long orderId, final Long menuId, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItem from(final ResultSet resultSet) throws SQLException {
        return new OrderLineItem(resultSet.getLong(KEY_COLUMN_NAME), resultSet.getLong("order_id")
                , resultSet.getLong("menu_id"), resultSet.getLong("quantity"));
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(final Long seq) {
        this.seq = seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(final Long orderId) {
        this.orderId = orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(final Long menuId) {
        this.menuId = menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }
}
