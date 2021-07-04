package kitchenpos.table.application;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.dao.OrderDao;
import kitchenpos.product.constant.OrderStatus;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;

@Service
public class TableService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private OrderTableRepository orderTableRepository;

    public TableService(final OrderDao orderDao, final OrderTableDao orderTableDao,
            final OrderTableRepository orderTableRepository) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTable) {
        OrderTable savedOrderTable = orderTableRepository.save(OrderTableRequest.of(orderTable));
        return OrderTableResponse.of(savedOrderTable);
    }

    public List<OrderTableResponse> list() {
        List<OrderTable> list = orderTableRepository.findAll();
        List<OrderTableResponse> listResponse = list.stream()
            .map(OrderTableResponse::of)
            .collect(Collectors.toList());

        return listResponse;
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTable orderTable) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId,
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.setEmpty(orderTable.isEmpty());

        return orderTableDao.save(savedOrderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTable orderTable) {
        final int numberOfGuests = orderTable.getNumberOfGuests();

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.setNumberOfGuests(numberOfGuests);

        return orderTableDao.save(savedOrderTable);
    }
}
