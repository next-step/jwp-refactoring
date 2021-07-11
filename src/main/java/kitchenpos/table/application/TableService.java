package kitchenpos.table.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.exception.NonEmptyOrderTableNotFoundException;
import kitchenpos.table.exception.OrderTableNotFoundException;

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
        return OrderTableResponse.of(orderTableRepository.save(orderTableRequest.toOrderTable()));
    }

    public OrderTable findById(Long id) {
        return orderTableRepository.findById(id).orElseThrow(() -> new OrderTableNotFoundException("대상 주문테이블이 존재하지 않습니다. ID : " + id));
    }

    public Long countByIds(List<Long> ids) {
        return orderTableRepository.countByIdIn(ids);
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll()
                .stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId).orElseThrow(IllegalArgumentException::new);

        if (savedOrderTable.hasTableGroup()) {
            throw new IllegalArgumentException();
        }
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
        savedOrderTable.changeEmpty(orderTableRequest.isEmpty());

        return OrderTableResponse.of(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        OrderTable savedOrderTable = orderTableRepository.findByIdAndEmptyIsFalse(orderTableId)
                .orElseThrow(NonEmptyOrderTableNotFoundException::new);
        savedOrderTable.changeNumberOfGuests(orderTableRequest.getNumberOfGuests());
        return OrderTableResponse.of(savedOrderTable);
    }

    public List<OrderTable> findOrderTablesByIds(List<Long> ids) {
        return orderTableRepository.findByIdIn(ids);
    }

    private void validateParametersIsEmpty(List<Long> ids) {
        if (ids.isEmpty()) {
            throw new IllegalArgumentException("입력된 ID가 없습니다.");
        }
    }

    private void validateOrderTablesIsEmpty(List<OrderTable> orderTables) {
        if (orderTables.isEmpty()) {
            throw new OrderTableNotFoundException();
        }
    }
}
