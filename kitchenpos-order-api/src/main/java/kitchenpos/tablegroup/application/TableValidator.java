package kitchenpos.tablegroup.application;

import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.exception.EntityNotFoundExceptionCode;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.application.OrderValidator;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.exception.OrderExceptionCode;
import kitchenpos.tablegroup.domain.OrderTable;
import kitchenpos.tablegroup.domain.OrderTableRepository;
import kitchenpos.tablegroup.exception.OrderTableExceptionCode;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TableValidator implements OrderValidator {
    private static final int MINIMUM_ORDER_MENU_SIZE = 1;

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableValidator(MenuRepository menuRepository, OrderRepository orderRepository,
            OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public void validateToCreateOrder(Long orderTableId, List<Long> menuIds) {
        OrderTable orderTable = findOrderTableById(orderTableId);
        List<Menu> menus = findAllMenuById(menuIds);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException(OrderExceptionCode.ORDER_TABLE_CANNOT_BE_EMPTY.getMessage());
        }

        if (menus.size() < MINIMUM_ORDER_MENU_SIZE) {
            throw new IllegalArgumentException(OrderExceptionCode.MUST_BE_GREATER_THAN_MINIMUM_SIZE.getMessage());
        }
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundExceptionCode.NOT_FOUND_BY_ID));
    }

    private List<Menu> findAllMenuById(List<Long> menuIds) {
        List<Menu> menus = menuRepository.findAllById(menuIds);
        if(menuIds.size() != menus.size()) {
            throw new EntityNotFoundException(EntityNotFoundExceptionCode.NOT_FOUND_BY_ID);
        }

        return menus;
    }

    @Override
    public void validateToChangeEmpty(OrderTable orderTable) {
        if (orderTable.hasTableGroup()) {
            throw new IllegalArgumentException(OrderTableExceptionCode.ALREADY_INCLUDED_IN_ANOTHER_TABLE_GROUP.getMessage());
        }

        List<Order> orders = findAllByOrderTableId(orderTable.getId());
        orders.forEach(Order::checkCookingOrEatingMealOrder);
    }

    private List<Order> findAllByOrderTableId(Long orderTableId) {
        return orderRepository.findAllByOrderTableId(orderTableId);
    }

    @Override
    public void validateToUngroup(List<Long> orderTableIds) {
        List<Order> orders = findAllOrderByOrderTableIds(orderTableIds);
        orders.forEach(Order::checkCookingOrEatingMealOrder);
    }

    private List<Order> findAllOrderByOrderTableIds(List<Long> orderTableIds) {
        return orderRepository.findAllByOrderTableIds(orderTableIds);
    }
}
