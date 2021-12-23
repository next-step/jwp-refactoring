package kitchenpos.domain;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MenuProduct {
    private static final String KEY_COLUMN_NAME = "seq";

    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    protected MenuProduct() {}

    public MenuProduct(Long seq, Long menuId, Long productId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProduct from(final ResultSet resultSet) throws SQLException {
        return new MenuProduct(resultSet.getLong(KEY_COLUMN_NAME), resultSet.getLong("menu_id")
                , resultSet.getLong("product_id"), resultSet.getLong("quantity"));
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(final Long seq) {
        this.seq = seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(final Long menuId) {
        this.menuId = menuId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(final Long productId) {
        this.productId = productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }
}
