package kitchenpos.order.domain;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class OrderValidator {
    private static final String ERROR_MESSAGE_ORDER_TABLE_EMPTY = "빈 테이블이 존재합니다.";
    private static final String ERROR_MESSAGE_ORDER_LINE_ITEM_EMPTY = "존재하지 않는 메뉴가 있습니다.";
    private static final String ERROR_MESSAGE_MENU_EMPTY = "주문항목이 비어있습니다.";
    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(MenuRepository menuRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validateCreate(OrderRequest orderRequest) {
        validateOrderTableEmpty(orderRequest);
        validateOrderLineItemEmpty(orderRequest.getOrderLineItemRequests());
        validateExistMenu(orderRequest.getOrderLineItemRequests());
    }

    private void validateOrderTableEmpty(OrderRequest orderRequest) {
        OrderTable orderTable = getOrderTableById(orderRequest.getOrderTableId());
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException(ERROR_MESSAGE_ORDER_TABLE_EMPTY);
        }
    }

    private void validateOrderLineItemEmpty(List<OrderLineItemRequest> orderLineItemRequests) {
        if (Objects.isNull(orderLineItemRequests) || orderLineItemRequests.isEmpty()) {
            throw new IllegalArgumentException(ERROR_MESSAGE_ORDER_LINE_ITEM_EMPTY);
        }
    }

    private void validateExistMenu(List<OrderLineItemRequest> orderLineItemRequests) {
        List<Long> menuIds = getMenuIds(orderLineItemRequests);

        if (orderLineItemRequests.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_MENU_EMPTY);
        }
    }

    private List<Long> getMenuIds(List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
    }

    private OrderTable getOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(NoSuchElementException::new);
    }
}
