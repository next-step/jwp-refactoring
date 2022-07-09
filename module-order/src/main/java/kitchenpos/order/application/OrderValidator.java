package kitchenpos.order.application;

import kitchenpos.order.request.OrderRequest;

public interface OrderValidator {
    void validateOrderRequest(OrderRequest orderRequest);
}
