package kichenpos.order.application;

import kichenpos.order.domain.Order;

import java.util.List;

public interface OrderValidator {

    void validateOrder(Order order);
    void validateUngroup(List<Long> orderTableIds);
    void validateChangeEmpty(Long orderTableId);
}
