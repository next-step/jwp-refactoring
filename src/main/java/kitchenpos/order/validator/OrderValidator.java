package kitchenpos.order.validator;

import kitchenpos.order.dto.OrderRequest;

public interface OrderValidator {
    void validate(final OrderRequest orderRequest);
}
