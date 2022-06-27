package kitchenpos.order.validator;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.MenuService;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.orderTable.application.TableService;
import kitchenpos.orderTable.domain.OrderTable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class OrderValidator {
    private final MenuService menuService;
    private final TableService tableService;

    public OrderValidator(final MenuService menuService, final TableService tableService) {
        this.menuService = menuService;
        this.tableService = tableService;
    }

    public void validate(OrderRequest orderRequest) {
        validateNotEmpty(tableService.findOrderTable(orderRequest.getOrderTableId()));
        validateOrderLineItemsSize(orderRequest.getOrderLineItems());
    }

    private void validateNotEmpty(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문테이블이 비어있으면 안됩니다.");
        }
    }

    private void validateOrderLineItemsSize(List<OrderLineItemRequest> orderLineItems) {
        if (orderLineItems.size() != menuService.countByIdIn(findMenuIds(orderLineItems))) {
            throw new IllegalArgumentException("주문 항목들의 수와 조회된 메뉴 수가 일치하지 않습니다.");
        }
    }

    private List<Long> findMenuIds(List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
    }
}
