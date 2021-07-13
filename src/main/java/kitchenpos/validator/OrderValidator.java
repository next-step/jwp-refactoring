package kitchenpos.validator;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuDetailOption;
import kitchenpos.domain.MenuOption;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.exception.IllegalOperationException;
import kitchenpos.exception.MenuMismatchException;
import kitchenpos.exception.OrderTableNotFoundException;

@Component
public class OrderValidator {

    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(MenuRepository menuRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validate(Order order) {
        validate(order, getOrderTable(order), getMenus(order));
    }

    public void validate(Order order, OrderTable orderTable, Map<Long, Menu> menus) { // TODO default로 변경
        if (orderTable.isEmpty()) {
            throw new IllegalOperationException("빈 테이블에 주문할 수 없습니다.");
        }

        if (order.getOrderLineItems().isEmpty()) {
            throw new IllegalArgumentException("주문 상세 내역은 하나 이상 존재해야 합니다.");
        }

        order.getOrderLineItems()
            .forEach(item -> validateOrderLineItem(item, menus.get(item.getMenuId())));
    }

    private void validateOrderLineItem(OrderLineItem orderLineItem, Menu menu) {
        MenuOption menuOption = orderLineItem.getMenuOption();
        if (!menu.isSatisfiedBy(menuOption)) {
            throw new MenuMismatchException("메뉴가 변경되었습니다.");
        }

        List<MenuDetailOption> menuDetailOptions = orderLineItem.getMenuDetailOptions();
        if (!menu.getMenuProducts().isSatisfiedBy(menuDetailOptions)) {
            throw new MenuMismatchException("메뉴 세부항목이 변경되었습니다.");
        }
    }

    public void checkChangeable(Order order) {
        if (order.isCompleted()) {
            throw new IllegalOperationException("완결 된 주문은 상태를 변경할 수 없습니다.");
        }
    }

    private OrderTable getOrderTable(Order order) {
        return orderTableRepository.findById(order.getOrderTableId())
            .orElseThrow(() -> new OrderTableNotFoundException("해당 ID 의 주문 테이블이 존재하지 않습니다."));
    }

    private Map<Long, Menu> getMenus(Order order) {
        return menuRepository.findAllById(order.getMenuIds())
            .stream().collect(Collectors.toMap(Menu::getId, Function.identity()));
    }
}
