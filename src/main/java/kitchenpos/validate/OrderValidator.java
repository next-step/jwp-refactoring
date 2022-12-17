package kitchenpos.validate;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderLineItemRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class OrderValidator {

    public void validateCreate(List<OrderLineItemRequest> orderLineItemRequests, OrderTable orderTable){
        if (CollectionUtils.isEmpty(orderLineItemRequests) ||
                existsMenuIdIsNull(orderLineItemRequests) ||
                orderTable.isEmpty()
        ) {
            throw new IllegalArgumentException();
        }
    }

    private boolean existsMenuIdIsNull(List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
                .anyMatch(orderLineItemRequest -> orderLineItemRequest.getMenuId() == null);
    }
}
