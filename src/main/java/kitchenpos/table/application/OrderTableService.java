package kitchenpos.table.application;

import java.util.List;
import javax.persistence.EntityNotFoundException;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderTableService {

    private final OrderTableDao orderTableDao;

    public OrderTableService(OrderTableDao orderTableDao) {
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = orderTableRequest.toOrderTable();
        return OrderTableResponse.of(orderTableDao.save(orderTable));
    }

    public List<OrderTableResponse> list() {
        return OrderTableResponse.listOf(orderTableDao.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
            .orElseThrow(EntityNotFoundException::new);
        savedOrderTable.changeEmpty(orderTableRequest.isEmpty());
        return OrderTableResponse.of(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
            .orElseThrow(EntityNotFoundException::new);
        savedOrderTable.changeNumberOfGuests(orderTableRequest.getNumberOfGuests());
        return OrderTableResponse.of(savedOrderTable);
    }
}
