package kitchenpos.order.application;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.dao.OrderRepository;
import kitchenpos.order.dao.OrderTableRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;

@Service
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest request) {
        OrderTable orderTable = request.toOrderTable();
        orderTable.updateTableGroup(null);

        return OrderTableResponse.from(orderTableRepository.save(orderTable));
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll().stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest request) {
        OrderTable orderTable = request.toOrderTable();
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 테이블만 빈 테이블로 변경할 수 있습니다"));

        if (Objects.nonNull(savedOrderTable.getTableGroup())) {
            throw new IllegalArgumentException("단체지정이 되어있는 테이블은 빈 테이블로 변경할 수 없습니다");
        }

        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("조리중이거나 식사중인 테이블은 빈 테이블로 변경할 수 없습니다");
        }

        savedOrderTable.changeEmpty(orderTable.isEmpty());

        return OrderTableResponse.from(orderTableRepository.save(savedOrderTable));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest request) {
        OrderTable orderTable = request.toOrderTable();
        final int numberOfGuests = orderTable.getNumberOfGuests();

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("테이블의 손님 수는 최소 0명 이상이어야합니다");
        }

        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 테이블만 방문 손님 수를 지정할 수 있습니다"));

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.changeNumberOfGuests(numberOfGuests);

        return OrderTableResponse.from(orderTableRepository.save(savedOrderTable));
    }
    
    @Transactional(readOnly = true)
    public OrderTable findById(Long orderTableId) {
        System.out.println("여기왜안들어오징..");
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 주문 테이블이 없습니다"));
    }
}
