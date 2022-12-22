package kitchenpos.order.application.validator;

import java.util.List;

public interface OrderLineItemsSizeValidator {
    void validateOrderLineItems(int orderLineItemsSize, List<Long> menuIds);
}
