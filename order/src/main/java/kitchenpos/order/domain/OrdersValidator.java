package kitchenpos.order.domain;

import java.util.List;

import org.springframework.stereotype.Component;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menus;
import kitchenpos.menu.exception.NotRegistedMenuOrderException;
import kitchenpos.order.exception.EmptyOrderLineItemOrderException;
import kitchenpos.order.exception.NotChangableOrderStatusException;

@Component
public class OrdersValidator {
    private final MenuService menuService;

    public OrdersValidator (
        final MenuService menuService
    ) {
        this.menuService = menuService;
    }

    public void checkExistOfMenu(final OrderLineItems orderLineItems) {
        List<Long> menuIds = orderLineItems.findMenuIds();
        Menus menus = Menus.of(menuService.findAllByIdIn(menuIds));

        if (menus.size() != menuIds.size()) {
            throw new NotRegistedMenuOrderException("요청된 메뉴와 조회된 메뉴의 수가 일치하지 않습니다.");
        }
    }

    public void checkEmptyOfOrderLineItems(final OrderLineItems orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new EmptyOrderLineItemOrderException("주문상품이 없습니다.");
        }
    }

    public void validateionOfChageOrderStatus(Orders order) {
        if (order.isCompletion()) {
            throw new NotChangableOrderStatusException("주문이 계산완료된 상태입니다.");
        }
    }
}
