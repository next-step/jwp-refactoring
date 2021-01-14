package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderStatus;
import kitchenpos.dto.OrderTableDto;
import kitchenpos.repository.OrderDao;
import kitchenpos.repository.OrderTableDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class TableService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public TableService(final OrderDao orderDao, final OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    public OrderTableDto create(final OrderTableDto orderTable) {
        OrderTable savedTable = orderTableDao.save(orderTable.toEntity());
        return OrderTableDto.of(savedTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableDto> list() {
        return orderTableDao.findAll().stream()
                .map(OrderTableDto::of)
                .collect(Collectors.toList());
    }

    public OrderTableDto changeEmpty(final Long orderTableId, final OrderTableDto orderTable) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.changeEmpty(orderTable.isEmpty());

        OrderTable savedTable = orderTableDao.save(savedOrderTable);

        return OrderTableDto.of(savedTable);
    }

    @Transactional
    public OrderTableDto changeNumberOfGuests(final Long orderTableId, final OrderTableDto orderTable) {
        final int numberOfGuests = orderTable.getNumberOfGuests();

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
        savedOrderTable.changeNumberOfGuests(numberOfGuests);

        OrderTable savedTable = orderTableDao.save(savedOrderTable);

        return OrderTableDto.of(savedTable);
    }
}
