package kitchenpos.order.dto;

import java.math.BigDecimal;

public class OrderLineItemResponse {

    private Long seq;
    private Long menuId;
    private long quantity;
    private String menuName;
    private BigDecimal menuPrice;

    public OrderLineItemResponse() {
    }

    public OrderLineItemResponse(Long seq, Long menuId, long quantity, String menuName, BigDecimal menuPrice) {
        this.seq = seq;
        this.menuId = menuId;
        this.quantity = quantity;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice;
    }

    public void setMenuPrice(BigDecimal menuPrice) {
        this.menuPrice = menuPrice;
    }
}
