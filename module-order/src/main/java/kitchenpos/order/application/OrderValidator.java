package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.exception.NotEqualsMenuAndOrderLineItemMenuException;
import kitchenpos.order.exception.NotExistOrderLineItemsException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class OrderValidator {

    private static final String CANNOT_CHANGE_ORDER_STATUS = "완료 주문은 상태를 바꿀 수 없습니다.";

    private final MenuRepository menuRepository;

    public OrderValidator(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public void validate(Order order) {
        validateOrder(order);
        validateOrderLineItems(order.getOrderLineItems());
        validateExistMenu(order.getOrderLineItems());
        validateEmptyOrderTable(order);
    }

    public void validateChangeOrderStatus(Order order) {
        if (order.getOrderStatus().isCompletion()) {
            throw new IllegalArgumentException(CANNOT_CHANGE_ORDER_STATUS);
        }
    }

    private void validateOrder(Order order) {
        order.orderTableExistCheck();
    }

    private void validateExistMenu(OrderLineItems orderLineItems) {
        List<Long> menuIds = orderLineItems.getReadOnlyValues()
                .stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        long menuCount = menuRepository.countByIdIn(menuIds);
        if (menuIds.size() != menuCount) {
            throw new NotEqualsMenuAndOrderLineItemMenuException(menuCount, menuIds.size());
        }
    }

    private void validateOrderLineItems(OrderLineItems orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems.getReadOnlyValues())) {
            throw new NotExistOrderLineItemsException();
        }
    }

    private void validateEmptyOrderTable(Order order) {
        order.orderTableEmptyCheck();
    }
}
