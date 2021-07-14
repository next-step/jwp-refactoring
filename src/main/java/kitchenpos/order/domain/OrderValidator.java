package kitchenpos.order.domain;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuDetailOption;
import kitchenpos.menu.domain.MenuOption;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.generic.exception.IllegalOperationException;
import kitchenpos.menu.exception.MenuMismatchException;
import kitchenpos.order.exception.OrderTableNotFoundException;

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

        if (order.getOrderLineItems().size() != menus.size()) {
            throw new MenuMismatchException("메뉴 갯수와 주문 목록 수는 일치해야 합니다");
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
