package kitchenpos.application;

import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTableResponse create(OrderTableRequest request) {
        return OrderTableResponse.from(orderTableRepository.save(new OrderTable(request.getNumberOfGuests(), request.isEmpty())));
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll()
                .stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    public OrderTableResponse changeEmpty(Long orderTableId, OrderTableRequest request) {
        OrderTable orderTable = request.toOrderTable();
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.nonNull(savedOrderTable.getTableGroup())) {
            throw new IllegalArgumentException();
        }

        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.setEmpty(orderTable.isEmpty());

        return OrderTableResponse.from(orderTableRepository.save(savedOrderTable));
    }
    
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, OrderTableRequest request) {
        OrderTable orderTable = request.toOrderTable();
        final int numberOfGuests = orderTable.getNumberOfGuests();

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.setNumberOfGuests(numberOfGuests);

        return OrderTableResponse.from(orderTableRepository.save(savedOrderTable));
    }

    public OrderTableResponse findResponseById(Long id) {
        return OrderTableResponse.from(findById(id));
    }

    public OrderTable findById(Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("OrderTable id:" + id + "가 존재하지 않습니다."));
    }
}
