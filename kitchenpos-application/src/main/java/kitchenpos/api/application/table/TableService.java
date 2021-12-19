package kitchenpos.api.application.table;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.domain.order.OrderRepository;
import kitchenpos.common.domain.order.OrderStatus;
import kitchenpos.common.domain.table.OrderTable;
import kitchenpos.common.domain.table.OrderTableRepository;
import kitchenpos.common.dto.table.OrderTableRequest;
import kitchenpos.common.dto.table.OrderTableResponse;
import kitchenpos.common.utils.StreamUtils;

@Service
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        OrderTable savedOrderTable = orderTableRepository.save(orderTableRequest.toOrderTable());
        return OrderTableResponse.from(savedOrderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();
        return StreamUtils.mapToList(orderTables, OrderTableResponse::from);
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = findOrderTable(orderTableId);
        validateChangeEmpty(orderTableId, orderTable);
        orderTable.updateEmpty(orderTableRequest.isEmpty());

        return OrderTableResponse.from(orderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        int numberOfGuests = orderTableRequest.getNumberOfGuests();
        OrderTable orderTable = findOrderTable(orderTableId);
        validateChangeNumberOfGuests(orderTable);
        orderTable.updateNumberOfGuests(numberOfGuests);

        return OrderTableResponse.from(orderTable);
    }

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                                   .orElseThrow(EntityNotFoundException::new);
    }

    private void validateChangeEmpty(Long orderTableId, OrderTable orderTable) {
        if (Objects.nonNull(orderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }

        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
            orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }

    private void validateChangeNumberOfGuests(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}
