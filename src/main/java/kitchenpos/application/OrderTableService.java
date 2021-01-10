package kitchenpos.application;

import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.orderTable.OrderTable;
import kitchenpos.domain.orderTable.OrderTableRepository;
import kitchenpos.domain.orderTable.exceptions.InvalidTryChangeEmptyException;
import kitchenpos.domain.orderTable.exceptions.InvalidTryChangeGuestsException;
import kitchenpos.domain.orderTable.exceptions.OrderTableEntityNotFoundException;
import kitchenpos.ui.dto.orderTable.ChangeEmptyRequest;
import kitchenpos.ui.dto.orderTable.ChangeNumberOfGuestsRequest;
import kitchenpos.ui.dto.orderTable.OrderTableRequest;
import kitchenpos.ui.dto.orderTable.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderTableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderTableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest request) {
        OrderTable orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());
        OrderTable saved = orderTableRepository.save(orderTable);

        return OrderTableResponse.of(saved);
    }

    @Transactional(readOnly = true)
    public OrderTable findOrderTable(final Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(() -> new OrderTableEntityNotFoundException("해당 OrderTable Entity가 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();

        return orderTables.stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final ChangeEmptyRequest changeEmptyRequest) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new OrderTableEntityNotFoundException("존재하지 않는 주문 테이블의 비움 상태를 바꿀 수 없습니다."));

        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new InvalidTryChangeEmptyException("단체 지정된 주문 테이블의 비움 상태를 바꿀 수 없습니다.");
        }

        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new InvalidTryChangeEmptyException("조리중이거나 식사중인 주문 테이블의 비움 상태를 바꿀 수 없습니다.");
        }

        savedOrderTable.changeEmpty(changeEmptyRequest.isEmpty());

        OrderTable changed = orderTableRepository.save(savedOrderTable);

        return OrderTableResponse.of(changed);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(
            final Long orderTableId, final ChangeNumberOfGuestsRequest changeNumberOfGuestsRequest
    ) {
        final int numberOfGuests = changeNumberOfGuestsRequest.getNumberOfGuests();

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new OrderTableEntityNotFoundException(
                        "존재하지 않는 주문 테이블의 방문한 손님 수를 바꿀 수 없습니다."));

        if (savedOrderTable.isEmpty()) {
            throw new InvalidTryChangeGuestsException("비어있는 주문 테이블의 방문한 손님 수를 바꿀 수 없습니다.");
        }

        savedOrderTable.changeNumberOfGuests(numberOfGuests);

        OrderTable changed = orderTableRepository.save(savedOrderTable);

        return OrderTableResponse.of(changed);
    }
}
