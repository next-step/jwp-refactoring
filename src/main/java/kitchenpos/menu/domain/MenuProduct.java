package kitchenpos.menu.domain;

public class MenuProduct {
    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    public MenuProduct() {
    }

    public MenuProduct(Long seq, Long menuId, Long productId, long quantity) {
        validateQuantity(quantity);
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    private void validateQuantity(long quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("메뉴 상품의 수량는 0개 이상 이어야 합니다.");
        }
    }

    public static MenuProduct of(Long seq, Long menuId, Long productId, long quantity) {
        return new MenuProduct(seq, menuId, productId, quantity);
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
