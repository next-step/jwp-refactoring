package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTable create(final OrderTable orderTable) {
        orderTable.setTableGroup(null);

        return orderTableRepository.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTable orderTable) {
        final OrderTable savedOrderTable = findOrderTableById(orderTableId);

        savedOrderTable.checkOrderTableIsGrouped();
        checkOrderStatusInCookingOrMeal(orderTableId);

        savedOrderTable.changeEmpty(orderTable.isEmpty());

        return orderTableRepository.save(savedOrderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTable orderTable) {
        final int numberOfGuests = orderTable.getNumberOfGuests();
        orderTable.checkNumberOfGuestsNotExists();

        final OrderTable savedOrderTable = findOrderTableById(orderTableId);
        savedOrderTable.checkOrderTableIsEmpty();
        savedOrderTable.changeNumberOfGuests(numberOfGuests);

        return orderTableRepository.save(savedOrderTable);
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private void checkOrderStatusInCookingOrMeal(final Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }
}
