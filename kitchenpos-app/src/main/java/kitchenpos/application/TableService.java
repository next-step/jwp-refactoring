package kitchenpos.application;

import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.OrderTableRepository;
import kitchenpos.dto.ChangeEmptyTableRequest;
import kitchenpos.dto.NumberOfGuestsRequest;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.exception.AlreadyGroupedTableException;
import kitchenpos.exception.NotEmptyTableException;
import kitchenpos.exception.OrderTableNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public TableService(OrderTableRepository orderTableRepository,
                        OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderTable create(final OrderTableRequest orderTableRequest) {
        return orderTableRepository.save(orderTableRequest.toOrderTable());
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final ChangeEmptyTableRequest changeEmptyTableRequest) {
        final OrderTable savedOrderTable = findOrderTableById(orderTableId);

        if (savedOrderTable.hasTableGroup()) {
            throw new AlreadyGroupedTableException(orderTableId);
        }

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                Collections.singletonList(orderTableId),
                Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalStateException("Cannot change empty state on cooking or meal");
        }

        savedOrderTable.setEmpty(changeEmptyTableRequest.isEmpty());

        return orderTableRepository.save(savedOrderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId,
                                           final NumberOfGuestsRequest numberOfGuestsRequest) {
        if (numberOfGuestsRequest.getNumberOfGuests() < 0) {
            throw new IllegalArgumentException("number of guests was negative value");
        }

        final OrderTable savedOrderTable = findOrderTableById(orderTableId);

        if (savedOrderTable.isEmpty()) {
            throw new NotEmptyTableException(orderTableId);
        }

        savedOrderTable.setNumberOfGuests(numberOfGuestsRequest.getNumberOfGuests());

        return orderTableRepository.save(savedOrderTable);
    }

    public OrderTable findOrderTableById(Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(() -> new OrderTableNotFoundException(id));
    }

    public List<OrderTable> findAllByTableGroupId(Long id) {
        return orderTableRepository.findAllByTableGroupId(id);
    }
}
