package kitchenpos.table.application;

import kitchenpos.common.exception.NotFoundEntityException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.exception.InvalidChangeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        OrderTable persistOrderTable = orderTableRepository.save(orderTableRequest.toOrderTable());
        return OrderTableResponse.of(persistOrderTable);
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll().stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable persistOrderTable = findOrderTableById(orderTableId);
        persistOrderTable.checkIsGroup();

        checkExistsByOrderTableIdAndOrderStatusIn(orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL));

        persistOrderTable.changeEmpty(orderTableRequest.isEmpty());
        return OrderTableResponse.of(persistOrderTable);
    }

    private void checkExistsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<OrderStatus> orderStatuses) {
        Order persistOrder = findOrderByOrderTableId(orderTableId);

        if (orderStatuses.contains(persistOrder.getOrderStatus())) {
            throw new InvalidChangeException("주문 상태가 조리 또는 식사이므로 변경할 수 없습니다.");
        }
    }

    private Order findOrderByOrderTableId(final Long orderTableId) {
        return orderRepository.findByOrderTableId(orderTableId)
                .orElseThrow(() -> new NotFoundEntityException("주문 테이블이 등록되어 있지 않습니다."));
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable persistOrderTable = findOrderTableById(orderTableId);
        persistOrderTable.checkIsEmpty();

        OrderTable orderTable = orderTableRequest.toOrderTable();
        persistOrderTable.changeNumberOfGuests(orderTable.getNumberOfGuests());
        return OrderTableResponse.of(persistOrderTable);
    }

    public OrderTables findOrderTablesById(List<Long> orderTableIds) {
        List<OrderTable> savedOrderTables = orderTableRepository.findAllById(orderTableIds);

        checkExistsOrderTables(orderTableIds);

        return new OrderTables(savedOrderTables);
    }

    private void checkExistsOrderTables(List<Long> orderTableIds) {
        if (orderTableIds.size() != orderTableRepository.countByIdIn(orderTableIds)) {
            throw new NotFoundEntityException("등록되어 있지 않은 주문 테이블이 있습니다.");
        }
    }

    public OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new NotFoundEntityException("해당 주문 테이블이 등록되어 있지 않습니다."));
    }

    public List<OrderTable> findAllByTableGroupId(Long tableGroupId) {
        return orderTableRepository.findAllByTableGroupId(tableGroupId);
    }
}
