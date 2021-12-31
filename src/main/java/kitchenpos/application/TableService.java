package kitchenpos.application;

import kitchenpos.common.exceptions.EmptyOrderTableException;
import kitchenpos.common.exceptions.NotEmptyOrderTableGroupException;
import kitchenpos.common.exceptions.NotFoundEntityException;
import kitchenpos.common.exceptions.OrderStatusNotProcessingException;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.dto.ordertable.OrderTableRequest;
import kitchenpos.dto.ordertable.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest request) {
        final OrderTable orderTable = orderTableRepository.save(request.toOrderTable());
        return OrderTableResponse.from(orderTable);
    }

    public List<OrderTableResponse> list() {
        final List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTables.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable orderTable = getNotGroupedOrderTableById(orderTableId);
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTable,
                Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new OrderStatusNotProcessingException();
        }
        orderTable.changeEmptyStatus(orderTableRequest.isEmpty());
        return OrderTableResponse.from(orderTable);
    }

    private OrderTable getNotGroupedOrderTableById(final Long id) {
        final OrderTable orderTable = findById(id);
        if (Objects.nonNull(orderTable.getTableGroup())) {
            throw new NotEmptyOrderTableGroupException();
        }
        return orderTable;
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest request) {
        final OrderTable orderTable = findById(orderTableId);
        orderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return OrderTableResponse.from(orderTable);
    }

    public OrderTable findOrderTableById(final Long orderTableId) {
        final OrderTable orderTable = findById(orderTableId);
        if (orderTable.isEmpty()) {
            throw new EmptyOrderTableException();
        }
        return orderTable;
    }

    public List<OrderTable> findAllById(final List<Long> ids) {
        return orderTableRepository.findAllById(ids);
    }

    private OrderTable findById(final Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(NotFoundEntityException::new);
    }
}
