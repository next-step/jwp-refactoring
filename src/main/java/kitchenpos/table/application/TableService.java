package kitchenpos.table.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.exception.TableException;
import kitchenpos.table.exception.TableExceptionType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public TableService(final OrderTableRepository orderTableRepository, final OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTable orderTable) {
        final OrderTable save = orderTableRepository.save(orderTable);
        return OrderTableResponse.of(save);
    }

    public List<OrderTableResponse> list() {
        return OrderTableResponse.ofList(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTable orderTable) {
        final OrderTable savedOrderTable = findById(orderTableId);
        validationOrderStatus(orderTableId);
        savedOrderTable.updateEmptyTable(orderTable);

        return OrderTableResponse.of(savedOrderTable);
    }

    private void validationOrderStatus(final Long orderTableId) {
        orderRepository.findByOrderTableId(orderTableId)
                .ifPresent(it -> {
                    if (it.getOrderStatus().enabledOrderCancel()) {
                        throw new TableException(TableExceptionType.IMPOSSIBLE_ORDER_STATUS);
                    }
                });
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTable orderTable) {
        final OrderTable savedOrderTable = findById(orderTableId);
        savedOrderTable.changeNumberOfGuest(orderTable);

        return OrderTableResponse.of(savedOrderTable);
    }

    private OrderTable findById(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new TableException(TableExceptionType.TABLE_NOT_FOUND));
    }
}
