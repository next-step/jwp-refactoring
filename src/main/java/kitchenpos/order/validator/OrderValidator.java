package kitchenpos.order.validator;

import kitchenpos.common.constant.ErrorCode;
import kitchenpos.order.domain.Order;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderValidator {
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public void validateCreateOrder(OrderRequest orderRequest) {
        validateOrderTable(orderRequest.getOrderTableId());
        validateOrderLineItems(orderRequest.getOrderLineItems());
    }

    private void validateOrderTable(Long id) {
        OrderTable orderTable = orderTableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.ORDER_TABLE_IS_NOT_EXIST.getMessage()));

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException(ErrorCode.ORDER_TABLE_IS_EMPTY.getMessage());
        }
    }

    private void validateOrderLineItems(List<OrderLineItemRequest> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException(ErrorCode.ORDER_LINE_ITEMS_IS_EMPTY.getMessage());
        }
    }
}
