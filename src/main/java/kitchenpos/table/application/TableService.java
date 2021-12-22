package kitchenpos.table.application;

import static com.google.common.primitives.Longs.asList;

import java.util.List;
import kitchenpos.table.domain.OrderTableDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.ChangeEmptyRequest;
import kitchenpos.table.dto.ChangeNumberOfGuestRequest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final TableValidation tableValidator;
    private final OrderTableDao orderTableDao;

    public TableService(final TableValidation tableValidator, final OrderTableDao orderTableDao) {
        this.tableValidator = tableValidator;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        return OrderTableResponse.of(orderTableDao.save(orderTableRequest.toOrderTable()));
    }

    @Transactional(readOnly = true)
    public List<OrderTable> list() {
        return orderTableDao.findAll();
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId,
        final ChangeEmptyRequest changeEmptyRequest) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        tableValidator.validInCookingOrMeal(asList(orderTableId));
        savedOrderTable.changeOrderTableStatus(changeEmptyRequest.isEmpty());

        return OrderTableResponse.of(orderTableDao.save(savedOrderTable));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
        final ChangeNumberOfGuestRequest changeEmptyRequest) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        savedOrderTable.changeNumberOfGuest(changeEmptyRequest.getNumberOfGuest());

        return OrderTableResponse.of(orderTableDao.save(savedOrderTable));
    }
}
