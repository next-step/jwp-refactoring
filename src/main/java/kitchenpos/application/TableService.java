package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Orders;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public TableService(final OrderDao orderDao, final OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderTable create(final OrderTableRequest orderTableRequest) {
        final OrderTable orderTable = new OrderTable(orderTableRequest.getNumberOfGuests());

        return orderTableDao.save(orderTable);
    }

    public List<OrderTableResponse> list() {
        return orderTableDao.findAll()
            .stream()
            .map(OrderTableResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        validateOrdersStatus(new Orders(findAllOrder(orderTableId)));
        final OrderTable savedOrderTable = findOrderTable(orderTableId);
        savedOrderTable.changeEmpty(orderTableRequest.isEmpty());

        return orderTableDao.save(savedOrderTable);
    }

    private void validateOrdersStatus(Orders orders) {
        if (orders.isNotCompleted()) {
            throw new IllegalArgumentException();
        }
    }

    private List<Order> findAllOrder(Long orderTableId) {
        return orderDao.findAllByOrderTable_Id(orderTableId);
    }

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableDao.findById(orderTableId)
            .filter(orderTable -> orderTable.getTableGroupId() == null)
            .orElseThrow(IllegalArgumentException::new);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        validateNumberOfGuests(orderTableRequest);
        final OrderTable savedOrderTable = findOrderTable(orderTableId);
        savedOrderTable.changeNumberOfGuests(orderTableRequest.getNumberOfGuests());

        return orderTableDao.save(savedOrderTable);
    }

    private void validateNumberOfGuests(OrderTableRequest orderTableRequest) {
        if (orderTableRequest.isNotValidNumberOfGuests()) {
            throw new IllegalArgumentException();
        }
    }
}
