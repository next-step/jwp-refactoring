package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.dao.OrderTableDao;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderTableDao orderTableDao;

    public TableService(final OrderTableDao orderTableDao) {
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest request) {
        OrderTable orderTable = request.toOrderTable();
        OrderTable savedOrderTable = orderTableDao.save(orderTable);
        return OrderTableResponse.from(savedOrderTable);
    }

    public List<OrderTableResponse> list() {
        return orderTableDao.findAll()
            .stream()
            .map(OrderTableResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final boolean isEmpty) {
        final OrderTable orderTable = getOrderTable(orderTableId);
        orderTable.changeEmpty(isEmpty);
        return OrderTableResponse.from(orderTableDao.save(orderTable));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
        final int numberOfGuests) {
        final OrderTable orderTable = getOrderTable(orderTableId);
        orderTable.changeNumberOfGuest(numberOfGuests);
        return OrderTableResponse.from(orderTableDao.save(orderTable));
    }

    private OrderTable getOrderTable(Long orderTableId) {
        return orderTableDao.findById(orderTableId)
            .orElseThrow(() -> new IllegalArgumentException("변경하고자 하는 테이블 정보가 없습니다."));
    }
}
