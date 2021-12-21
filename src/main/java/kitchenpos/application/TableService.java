package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.Orders;
import kitchenpos.dto.order.OrderTableRequest;
import kitchenpos.dto.order.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableService {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public TableService(final OrderDao orderDao, final OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTable) {
        return OrderTableResponse.of(orderTableDao.save(orderTable.toOrderTable()));
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId,
        final OrderTableRequest orderTable) {
        final OrderTable savedOrderTable = findOrderTableById(orderTableId);

        List<Order> orders = orderDao.findAllByOrderTable(savedOrderTable);

        savedOrderTable.changeEmpty(Orders.of(orders), orderTable.isEmpty());

        return OrderTableResponse.of(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
        final OrderTableRequest orderTable) {
        final OrderTable savedOrderTable = findOrderTableById(orderTableId);

        savedOrderTable.changeNumberOfGuests(orderTable.getNumberOfGuests());
        return OrderTableResponse.of(savedOrderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return OrderTableResponse.toList(orderTableDao.findAll());
    }

    @Transactional(readOnly = true)
    public OrderTable findOrderTableById(Long orderTableId) {
        return orderTableDao.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);
    }
}
