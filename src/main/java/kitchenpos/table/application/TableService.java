package kitchenpos.table.application;

import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderDao orderDao, final OrderTableDao orderTableDao, OrderTableRepository orderTableRepository) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        kitchenpos.table.domain.OrderTable orderTable = orderTableRequest.toOrderTable();
        kitchenpos.table.domain.OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        return OrderTableResponse.from(savedOrderTable);
    }

    public List<OrderTable> list() {
        return orderTableDao.findAll();
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final kitchenpos.table.domain.OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrderTable.checkPossibleChangeEmpty();
        savedOrderTable.updateEmpty(orderTableRequest.getEmpty());
        return OrderTableResponse.from(savedOrderTable);
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
