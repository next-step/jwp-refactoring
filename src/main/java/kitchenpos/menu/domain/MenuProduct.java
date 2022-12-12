package kitchenpos.menu.domain;

import java.util.Objects;

public class MenuProduct {
    public static final String MENU_NULL_EXCEPTION_MESSAGE = "메뉴는 필수입니다.";
    public static final String PRODUCT_NULL_EXCEPTION_MESSAGE = "메뉴는 필수입니다.";
    public static final String QUANTITY_NULL_EXCEPTION_MESSAGE = "갯수는 필수입니다.";
    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    public MenuProduct(Long id, Long menuId, Long productId, Long quantity) {
        validate(menuId, productId, quantity);
        this.seq = id;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    private static void validate(Long menuId, Long productId, Long quantity) {
        if (Objects.isNull(menuId)) {
            throw new IllegalArgumentException(MENU_NULL_EXCEPTION_MESSAGE);
        }
        if (Objects.isNull(productId)) {
            throw new IllegalArgumentException(PRODUCT_NULL_EXCEPTION_MESSAGE);
        }
        if (Objects.isNull(quantity)) {
            throw new IllegalArgumentException(QUANTITY_NULL_EXCEPTION_MESSAGE);
        }
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
