package kitchenpos.order.validator;

import java.util.List;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class OrderValidator {

    private final OrderTableDao orderTableDao;

    public OrderValidator(OrderTableDao orderTableDao) {
        this.orderTableDao = orderTableDao;
    }

    public void validateCreate(List<OrderLineItemRequest> orderLineItemRequests, Long orderTableId){
        OrderTable orderTable = getOrderTable(orderTableId);

        if (CollectionUtils.isEmpty(orderLineItemRequests) ||
                existsMenuIdIsNull(orderLineItemRequests) ||
                orderTable.isEmpty()
        ) {
            throw new IllegalArgumentException();
        }
    }

    private boolean existsMenuIdIsNull(List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
                .anyMatch(orderLineItemRequest -> orderLineItemRequest.getMenuId() == null);
    }

    private OrderTable getOrderTable(Long orderTableId) {
        return orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
