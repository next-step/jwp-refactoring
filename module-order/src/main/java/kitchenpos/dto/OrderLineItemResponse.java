package kitchenpos.dto;

import kitchenpos.menu.dto.MenuResponse;

public class OrderLineItemResponse {

    private Long seq;
    private MenuResponse menu;
    private long quantity;

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public MenuResponse getMenu() {
        return menu;
    }

    public void setMenu(MenuResponse menu) {
        this.menu = menu;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
}
