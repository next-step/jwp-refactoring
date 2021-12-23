package kitchenpos.ordertable.application;

import kitchenpos.common.exception.CommonErrorCode;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.order.domain.Orders;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderDao, final OrderTableRepository orderTableDao) {
        this.orderRepository = orderDao;
        this.orderTableRepository = orderTableDao;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTable) {
        return OrderTableResponse.of(orderTableRepository.save(orderTable.toOrderTable()));
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId,
        final OrderTableRequest orderTable) {
        final OrderTable savedOrderTable = findOrderTableById(orderTableId);

        List<Order> orders = orderRepository.findAllByOrderTable(savedOrderTable);

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
        return OrderTableResponse.toList(orderTableRepository.findAll());
    }

    @Transactional(readOnly = true)
    public OrderTable findOrderTableById(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new NotFoundException(CommonErrorCode.ORDER_NOT_FOUND_EXCEPTION));
    }
}
