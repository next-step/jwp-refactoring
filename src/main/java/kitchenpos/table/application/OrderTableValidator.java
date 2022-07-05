package kitchenpos.table.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.application.OrderValidator;
import kitchenpos.order.domain.*;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderTableValidator implements OrderValidator {
    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderTableValidator(OrderRepository orderRepository, MenuRepository menuRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public void validateOrder(Order order) {
        List<Menu> menus = menuRepository.findByIdIn(order.getMenuIds());
        List<OrderLineItem> orderLineItems = order.getOrderLineItems();

        if (orderLineItems.size() != menus.size()) {
            throw new IllegalArgumentException("중복된 메뉴가 있습니다.");
        }

        validateMenu(menus, orderLineItems);
        validateEmptyTable(order);
    }

    private void validateMenu(List<Menu> menus, List<OrderLineItem> orderLineItems) {
        Map<Long, OrderMenu> orderMenus = menuToOrderMenu(menus);

        for (OrderLineItem orderLineItem : orderLineItems) {
            OrderMenu orderMenu = orderMenus.get(orderLineItem.getMenuId());
            orderMenu.validateMenu(orderLineItem.getOrderMenu());
        }
    }

    private Map<Long, OrderMenu> menuToOrderMenu(List<Menu> menus) {
        return menus.stream()
                .collect(Collectors.toMap(
                        Menu::getId,
                        menu -> new OrderMenu(menu.getId(), menu.getName(), menu.getPrice()))
                );
    }

    private void validateEmptyTable(Order order) {
        final OrderTable orderTable = orderTableRepository.findById(order.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블을 찾을 수 없습니다."));
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블은 주문을 할 수 없습니다.");
        }
    }

    @Override
    public void validateUngroup(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, OrderStatus.findNotCompletionStatus())) {
            throw new IllegalArgumentException("조리 또는 식사 중인 테이블은 단체 지정을 해제할 수 없습니다.");
        }
    }

    @Override
    public void validateChangeEmpty(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, OrderStatus.findNotCompletionStatus())) {
            throw new IllegalArgumentException("조리 또는 식사 중인 테이블은 이용 여부를 변경할 수 없습니다.");
        }
    }
}
