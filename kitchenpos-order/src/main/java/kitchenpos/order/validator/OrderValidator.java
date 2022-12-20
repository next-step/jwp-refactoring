package kitchenpos.order.validator;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.ordertable.domain.OrderTable;

public interface OrderValidator {

    void validateNotCompleteOrders(List<Order> orders);

    void validatorOrder(OrderRequest orderRequest);

    void validatorTable(OrderTable orderTable);
}
