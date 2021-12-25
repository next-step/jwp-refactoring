package kitchenpos.application;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.TableChangeEmptyRequest;
import kitchenpos.dto.TableChangeNumberOfGuestRequest;
import kitchenpos.dto.TableCreateRequest;
import kitchenpos.dto.TableResponse;
import kitchenpos.global.exception.EntityNotFoundException;
import kitchenpos.mapper.TableMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TableService {
    private final OrderTableDao orderTableDao;

    public TableService(final OrderTableDao orderTableDao) {
        this.orderTableDao = orderTableDao;
    }

    public TableResponse create(final TableCreateRequest request) {
        final OrderTable orderTable = OrderTable.builder()
                .numberOfGuests(request.getNumberOfGuest())
                .empty(request.isEmpty())
                .build();
        orderTable.initTableGroup();

        return TableMapper.toOrderTable(orderTableDao.save(orderTable));
    }

    @Transactional(readOnly = true)
    public List<TableResponse> list() {
        return TableMapper.toOrderTables(orderTableDao.findAll());
    }

    public OrderTable changeEmpty(final Long orderTableId, final TableChangeEmptyRequest request) {
        final OrderTable savedOrderTable = findOrderTable(orderTableId);

        savedOrderTable.changeEmpty(request.isEmpty());

        return savedOrderTable;
    }

    public OrderTable changeNumberOfGuests(final Long orderTableId, final TableChangeNumberOfGuestRequest request) {
        final OrderTable savedOrderTable = findOrderTable(orderTableId);

        savedOrderTable.checkAvailability();
        savedOrderTable.changeNumberOfGuest(request.getNumberOfGuests());

        return savedOrderTable;
    }

    @Transactional(readOnly = true)
    public OrderTable findOrderTable(Long id) {
        return orderTableDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("order not found. find order id is %d", id)));
    }

    @Transactional(readOnly = true)
    public List<OrderTable> findOrderTables(List<Long> ids) {
        return orderTableDao.findAllByIdIn(ids);
    }

    @Transactional(readOnly = true)
    public List<OrderTable> findAllByTableGroupId(final Long tableGroupId) {
        return orderTableDao.findAllByTableGroupId(tableGroupId);
    }
}
