package kitchenpos.dto.order;

import kitchenpos.domain.Quantity;
import kitchenpos.domain.order.OrderLineMenu;
import kitchenpos.dto.menu.MenuResponse;

import static java.util.stream.Collectors.*;

import java.util.List;

public class OrderLineMenuResponse {
    private Long id;
    private int quantity;
    private int amount;
    private MenuResponse menuResponse;

    public OrderLineMenuResponse(Long id, Quantity quantity, int amount, MenuResponse menuResponse) {
        this.id = id;
        this.quantity = quantity.getQuantity();
        this.amount = amount;
        this.menuResponse = menuResponse;
    }

    public static OrderLineMenuResponse of(OrderLineMenu orderLineMenu) {
        return new OrderLineMenuResponse(orderLineMenu.getId(), orderLineMenu.getQuantity(), orderLineMenu.getAmount(), MenuResponse.of(orderLineMenu.getMenu()));
    }

    public static List<OrderLineMenuResponse> ofList(List<OrderLineMenu> orderLineMenus) {
        return orderLineMenus.stream()
                .map(OrderLineMenuResponse::of)
                .collect(toList());
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getAmount() {
        return amount;
    }

    public MenuResponse getMenuResponse() {
        return menuResponse;
    }
}
