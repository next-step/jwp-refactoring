package kitchenpos.order.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.domain.OrderStatus;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class OrderRequest {
    private Long orderTableId;
    private OrderStatus orderStatus;
    @Size(min = 1)
    @NotNull
    private List<OrderMenuRequest> orderMenus;

    public OrderRequest(Long orderTableId, List<OrderMenuRequest> orderMenus) {
        this.orderTableId = orderTableId;
        this.orderMenus = orderMenus;
    }

    public OrderRequest(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderRequest() {
    }

    public List<OrderMenu> createOrderMenus(List<Menu> menus) {
        validateOrderMenus(menus);
        return orderMenus.stream()
                .map(orderMenu -> {
                    Menu menu = menus.stream()
                            .filter(filterMenu -> filterMenu.getId().equals(orderMenu.getMenuId()))
                            .findFirst()
                            .orElseThrow(IllegalArgumentException::new);
                    return new OrderMenu(menu, orderMenu.getQuantity());
                }).collect(Collectors.toList());
    }

    public List<Long> getMenuIds() {
        if (orderMenus == null) {
            return Collections.emptyList();
        }
        return orderMenus.stream()
                .map(OrderMenuRequest::getMenuId)
                .collect(Collectors.toList());
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<OrderMenuRequest> getOrderMenus() {
        return orderMenus;
    }

    private void validateOrderMenus(List<Menu> menus) {
        if(orderMenus == null) {
            throw new IllegalArgumentException("주문 요청한 메뉴 항목이 존재하지 않습니다.");
        }
        if (orderMenus.size() != menus.size()) {
            throw new IllegalArgumentException("주문 요청한 메뉴 중에 존재하지 않는 메뉴가 있습니다.");
        }
    }
}
