package kitchenpos.order.validator;

import java.util.List;
import kitchenpos.order.application.OrderTableService;
import kitchenpos.order.dto.OrderLineItemRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class OrderValidator {

    private OrderTableService orderTableService;

    public OrderValidator(OrderTableService orderTableService) {
        this.orderTableService = orderTableService;
    }

    public void validateCreate(List<OrderLineItemRequest> orderLineItemRequests, Long orderTableId){
        boolean isTableEmpty = orderTableService.isTableEmpty(orderTableId);

        if (CollectionUtils.isEmpty(orderLineItemRequests) ||
                existsMenuIdIsNull(orderLineItemRequests) ||
                isTableEmpty
        ) {
            throw new IllegalArgumentException();
        }
    }

    private boolean existsMenuIdIsNull(List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
                .anyMatch(orderLineItemRequest -> orderLineItemRequest.getMenuId() == null);
    }
}
