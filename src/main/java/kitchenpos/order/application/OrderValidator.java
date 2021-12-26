package kitchenpos.order.application;

import kitchenpos.order.dto.OrderLineItemRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class OrderValidator {

    public void validateEmpty(List<OrderLineItemRequest> orderLineItemRequests) {
        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new IllegalArgumentException();
        }
    }

}
