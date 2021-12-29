package kitchenpos.table.application;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import kitchenpos.common.exception.NoResultDataException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableDao;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.dto.ChangeEmptyRequest;
import kitchenpos.table.dto.ChangeNumberOfGuestRequest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.domain.TableGroupRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderTableDao orderTableDao;

    public TableService(OrderTableDao orderTableDao) {
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
    public OrderTableResponse changeEmpty(final Long orderTableId, final ChangeEmptyRequest changeEmptyRequest) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
            .orElseThrow(NoResultDataException::new);

        savedOrderTable.changeOrderTableStatus(changeEmptyRequest.isEmpty());
        return OrderTableResponse.of(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
        final ChangeNumberOfGuestRequest changeEmptyRequest) {

        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        savedOrderTable.changeNumberOfGuest(changeEmptyRequest.getNumberOfGuest());
        return OrderTableResponse.of(savedOrderTable);
    }

    @Transactional
    public List<OrderTableResponse> group(final TableGroupRequest tableGroupRequest) {
        List<OrderTable> orderTables = orderTableDao.findAllByIdIn(tableGroupRequest.getOrderTables());
        final OrderTables savedOrderTables = OrderTables.of(orderTables);
        tableGroupRequest.validIsSizeEquals(savedOrderTables);
        savedOrderTables.group();

        orderTableDao.flush();
        return OrderTableResponse.ofList(savedOrderTables.getList());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        List<OrderTable> savedOrderTables = orderTableDao.findByTableGroupId(tableGroupId);
        OrderTables orderTables = OrderTables.of(savedOrderTables);
        orderTables.unGroup();
    }
}
