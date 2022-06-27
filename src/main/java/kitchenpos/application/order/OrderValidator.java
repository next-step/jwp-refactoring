package kitchenpos.application.order;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderLineItems;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.exception.CreateOrderException;
import kitchenpos.exception.NotEqualsMenuAndOrderLineItemMenuException;
import kitchenpos.exception.NotExistOrderLineItemsException;
import kitchenpos.exception.NotFoundOrderTableException;
import kitchenpos.exception.OrderTableAlreadyEmptyException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class OrderValidator {

    private static final String CANNOT_CHANGE_ORDER_STATUS = "완료 주문은 상태를 바꿀 수 없습니다.";
    private static final String ORDER_TABLE_IS_NOT_EMPTY = "주문 생성 시 주문테이블은 필수입니다.";

    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(MenuRepository menuRepository,
                          OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validate(Order order) {
        validateOrder(order.getOrderTableId());
        validateOrderLineItems(order.getOrderLineItems());
        validateExistMenu(order.getOrderLineItems());
        validateEmptyOrderTable(order.getOrderTableId());
    }

    public void validateChangeOrderStatus(Order order) {
        if (order.getOrderStatus().isCompletion()) {
            throw new IllegalArgumentException(CANNOT_CHANGE_ORDER_STATUS);
        }
    }

    private void validateOrder(Long orderTableId) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new NotFoundOrderTableException(orderTableId));

        if (orderTable == null) {
            throw new CreateOrderException(ORDER_TABLE_IS_NOT_EMPTY);
        }
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

    private void validateEmptyOrderTable(Long orderTableId) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId).orElseThrow(IllegalArgumentException::new);
        if (orderTable.isEmpty()) {
            throw new OrderTableAlreadyEmptyException(orderTable.getId());
        }
    }
}
