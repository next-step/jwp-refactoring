package kitchenpos.menu.domain;

public class MenuProduct {
    public static final String MENU_NULL_EXCEPTION_MESSAGE = "메뉴는 필수입니다.";
    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    public MenuProduct(Long id, Long menuId, Long productId) {
        validate(menuId);
        this.seq = id;
        this.menuId = menuId;
        this.productId = productId;
    }

    public MenuProduct(long seq, Long menuId, long productId, long quantity) {
        validate(menuId);
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    private static void validate(Long menuId) {
        if (menuId == null) {
            throw new IllegalArgumentException(MENU_NULL_EXCEPTION_MESSAGE);
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
