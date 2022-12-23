package order.validator;

import order.domain.Order;
import order.dto.OrderRequest;

import java.util.List;

public interface OrderValidator {
    void validate(OrderRequest orderRequest);
    void validateOnGoingOrderStatus(List<Order> orders);
}
