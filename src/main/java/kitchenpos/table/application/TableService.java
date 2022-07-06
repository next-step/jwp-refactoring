package kitchenpos.table.application;

import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest request) {
        OrderTable orderTable = OrderTable.of(null, request.getNumberOfGuests(), request.isEmpty());
        orderTable = orderTableRepository.save(orderTable);
        return OrderTableResponse.of(orderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll().stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(Long id) {
        OrderTable orderTable = orderTableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 값이 존재하지 않습니다."));
        validChangeEmpty(orderTable);
        orderTable.changeEmpty();
        orderTable = orderTableRepository.save(orderTable);
        return OrderTableResponse.of(orderTable);
    }

    private void validChangeEmpty(OrderTable orderTable) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTable.getId(), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("요리 또는 식사 중인 상태일 땐 변경할 수 없습니다.");
        }
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long id, OrderTableRequest request) {
        final int numberOfGuests = request.getNumberOfGuests();

        OrderTable orderTable = orderTableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 값이 존재하지 않습니다."));
        orderTable.changeNumberOfGuests(numberOfGuests);
        orderTable = orderTableRepository.save(orderTable);

        return OrderTableResponse.of(orderTable);
    }

    public OrderTable findById(Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 테이블입니다."));
    }
}
