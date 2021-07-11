package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderDao;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableDao;
import kitchenpos.order.domain.Orders;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;

@Service
public class TableService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public TableService(final OrderDao orderDao, final OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        final OrderTable orderTable = new OrderTable(orderTableRequest.getNumberOfGuests());
        final OrderTable saved = orderTableDao.save(orderTable);

        return OrderTableResponse.of(saved);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return orderTableDao.findAll()
            .stream()
            .map(OrderTableResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        validateOrdersStatus(new Orders(findAllOrder(orderTableId)));
        final OrderTable savedOrderTable = findOrderTable(orderTableId);
        savedOrderTable.changeEmpty(orderTableRequest.isEmpty());
        final OrderTable saved = orderTableDao.save(savedOrderTable);

        return OrderTableResponse.of(saved);
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
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        validateNumberOfGuests(orderTableRequest);
        final OrderTable savedOrderTable = findOrderTable(orderTableId);
        savedOrderTable.changeNumberOfGuests(orderTableRequest.getNumberOfGuests());
        final OrderTable saved = orderTableDao.save(savedOrderTable);

        return OrderTableResponse.of(saved);
    }

    private void validateNumberOfGuests(OrderTableRequest orderTableRequest) {
        if (orderTableRequest.isNotValidNumberOfGuests()) {
            throw new IllegalArgumentException();
        }
    }
}
