package kitchenpos.tablegroup.application;

import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.exception.EntityNotFoundExceptionConstants;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.application.OrderValidator;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.tablegroup.domain.OrderTable;
import kitchenpos.tablegroup.domain.OrderTableRepository;
import kitchenpos.tablegroup.exception.TableExceptionConstants;
import org.springframework.stereotype.Component;

import java.util.List;

import static kitchenpos.tablegroup.exception.TableExceptionConstants.ALREADY_INCLUDED_IN_ANOTHER_TABLE_GROUP;
import static kitchenpos.tablegroup.exception.TableExceptionConstants.MUST_BE_GREATER_THAN_MINIMUM_ORDER_MENU_SIZE;
import static kitchenpos.tablegroup.exception.TableExceptionConstants.ORDER_TABLES_CANNOT_BE_EMPTY;

@Component
public class OrderTableValidator implements OrderValidator {
    private static final int NUMBER_OF_MINIMUM_ORDER_MENU = 1;

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderTableValidator(MenuRepository menuRepository, OrderRepository orderRepository,
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
            throw new IllegalArgumentException(ORDER_TABLES_CANNOT_BE_EMPTY.getErrorMessage());
        }
        if (menus.size() < NUMBER_OF_MINIMUM_ORDER_MENU) {
            throw new IllegalArgumentException(MUST_BE_GREATER_THAN_MINIMUM_ORDER_MENU_SIZE.getErrorMessage());
        }
    }

    @Override
    public void validateToChangeEmpty(OrderTable orderTable) {
        if (orderTable.hasTableGroup()) {
            throw new IllegalArgumentException(ALREADY_INCLUDED_IN_ANOTHER_TABLE_GROUP.getErrorMessage());
        }
        List<Order> orders = findAllByOrderTableId(orderTable.getId());
        orders.forEach(Order::checkCookingOrEatingMealOrder);
    }

    @Override
    public void validateToUngroup(List<Long> orderTableIds) {
        List<Order> orders = findAllOrderByOrderTableIds(orderTableIds);
        orders.forEach(Order::checkCookingOrEatingMealOrder);
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundExceptionConstants.NOT_FOUND_BY_ID));
    }

    private List<Menu> findAllMenuById(List<Long> menuIds) {
        List<Menu> menus = menuRepository.findAllById(menuIds);
        if(isLessThanNumberOfMinimumOrderMenu(menus)) {
            throw new EntityNotFoundException(EntityNotFoundExceptionConstants.NOT_FOUND_BY_ID);
        }
        return menus;
    }

    private boolean isLessThanNumberOfMinimumOrderMenu(List<Menu> menus) {
        return menus.size() < NUMBER_OF_MINIMUM_ORDER_MENU;
    }

    private List<Order> findAllByOrderTableId(Long orderTableId) {
        return orderRepository.findAllByOrderTableId(orderTableId);
    }

    private List<Order> findAllOrderByOrderTableIds(List<Long> orderTableIds) {
        return orderRepository.findAllByOrderTableIds(orderTableIds);
    }

}
