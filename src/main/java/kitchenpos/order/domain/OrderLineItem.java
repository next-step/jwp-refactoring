package kitchenpos.order.domain;

import java.util.Objects;

public class OrderLineItem {
    public static final String MENU_NULL_EXCEPTION_MESSAGE = "메뉴가 없을 수 없습니다.";
    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Long orderId, Long menuId, int quantity) {
        validate(menuId);
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem(long seq, long orderId, long menuId, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    private void validate(Long menuId) {
        if (Objects.isNull(menuId)) {
            throw new IllegalArgumentException(MENU_NULL_EXCEPTION_MESSAGE);
        }
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
