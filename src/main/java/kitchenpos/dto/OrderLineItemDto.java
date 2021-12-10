package kitchenpos.dto;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;

public class OrderLineItemDto {
    private long seq;
    private Order order;
    private Menu menu;
    private long quantity;

    protected OrderLineItemDto() {
    }

    private OrderLineItemDto(long seq, Order order, Menu menu, long quantity) {
        this.seq= seq;
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }

    public static OrderLineItemDto of(long seq, Order order, Menu menu, long quantity) {
        return new OrderLineItemDto(seq, order, menu, quantity);
    }

    public long getSeq() {
        return this.seq;
    }

    public Order getOrder() {
        return this.order;
    }

    public Menu getMenu() {
        return this.menu;
    }

    public long getQuantity() {
        return this.quantity;
    }

    
}
