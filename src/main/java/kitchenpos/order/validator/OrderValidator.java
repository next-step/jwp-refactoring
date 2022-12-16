package kitchenpos.order.validator;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.dto.OrderRequest;

public interface OrderValidator {

    void validator(OrderRequest orderRequest);

    void validateIfNotCompletionOrders(List<Order> orders);
}
