package kitchenpos.application.order;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.repository.table.OrderTableRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class OrderValidator {

    private final OrderTableRepository orderTableRepository;

    public OrderValidator(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public void checkOrderValidate(Order order) {

        checkOrderTableEmpty(order.getOrderTableId());
        checkOrderLineItemsEmpty(order.getOrderLineItems());
    }

    public void checkOrderLineItemsEmpty(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문 항목 리스트가 비어있습니다.");
        }
    }

    public void checkOrderTableEmpty(Long orderTableId) {

        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 주문테이블이 아닙니다."));

        orderTable.checkIsEmpty();
    }

}
