package kitchenpos.table.application;

import java.util.stream.Collectors;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableChangeEmptyRequest;
import kitchenpos.table.dto.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
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

    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        return OrderTableResponse.of(orderTableDao.save(orderTableRequest.toOrderTable()));
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return orderTableDao.findAll()
                            .stream()
                            .map(OrderTableResponse::of)
                            .collect(Collectors.toList());
    }

    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableChangeEmptyRequest request) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.setEmpty(request.isEmpty());
        return OrderTableResponse.of(orderTableDao.save(savedOrderTable));
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableChangeNumberOfGuestsRequest request) {
        final int numberOfGuests = request.getNumberOfGuests();
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.setNumberOfGuests(numberOfGuests);
        return OrderTableResponse.of(orderTableDao.save(savedOrderTable));
    }
}
