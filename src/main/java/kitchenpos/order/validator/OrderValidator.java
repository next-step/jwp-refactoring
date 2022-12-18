package kitchenpos.order.validator;

import kitchenpos.order.domain.Order;
import kitchenpos.order.dto.OrderRequest;

import java.util.List;

public interface OrderValidator {
    void validate(OrderRequest orderRequest);
    void validateOnGoingOrderStatus(List<Order> orders);
}
