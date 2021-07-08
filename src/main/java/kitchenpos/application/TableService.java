package kitchenpos.application;

import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.dto.order.OrderTableRequest;
import kitchenpos.exception.InvalidEntityException;
import kitchenpos.exception.InvalidOrderStatusException;
import kitchenpos.exception.InvalidOrderTableException;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTable create(final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = orderTableRequest.toEntity();
        return orderTableRepository.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new InvalidEntityException("Not Found OrderTable " + orderTableId));

        if (savedOrderTable.isTableGroupEmpty()) {
            throw new IllegalArgumentException("Already setting another TableGroup");
        }

        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new InvalidOrderStatusException("Invalid orderTable Status");
        }

        savedOrderTable.changeEmptyTable();

        return orderTableRepository.save(savedOrderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final Integer numberOfGuests) {

        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new InvalidEntityException("Not found OrderTableId " + orderTableId));

        if (savedOrderTable.isEmpty()) {
            throw new InvalidOrderTableException("orderTable id " + savedOrderTable.getId());
        }

        savedOrderTable.changeNumberOfGuest(numberOfGuests);

        return orderTableRepository.save(savedOrderTable);
    }
}
