package kitchenpos.table.application;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.table.dao.OrderTableRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {
    private final OrderDao orderDao;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderDao orderDao, final OrderTableRepository orderTableRepository) {
        this.orderDao = orderDao;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = orderTableRepository.save(orderTableRequest.toTableGroup());
        return OrderTableResponse.of(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTable orderTable) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.nonNull(savedOrderTable.getTableGroup())) {
            throw new IllegalArgumentException();
        }

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.setEmpty(orderTable.isEmpty());

        return orderTableRepository.save(savedOrderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTable orderTable) {
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

        return orderTableRepository.save(savedOrderTable);
    }
}
