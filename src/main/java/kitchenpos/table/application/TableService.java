package kitchenpos.table.application;

import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.TableChangeEmptyRequest;
import kitchenpos.table.dto.TableChangeNumberOfGuestRequest;
import kitchenpos.table.dto.TableCreateRequest;
import kitchenpos.table.dto.TableResponse;
import kitchenpos.global.exception.EntityNotFoundException;
import kitchenpos.table.mapper.TableMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TableService {
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public TableResponse create(final TableCreateRequest request) {
        final OrderTable orderTable = new OrderTable(request.getNumberOfGuest(), request.isEmpty());
        orderTable.initTableGroup();

        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        return TableMapper.toOrderTable(savedOrderTable);
    }

    @Transactional(readOnly = true)
    public List<TableResponse> list() {
        final List<OrderTable> orderTables = orderTableRepository.findAll();

        return TableMapper.toOrderTables(orderTables);
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
        return orderTableRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("order not found. find order id is %d", id)));
    }

    @Transactional(readOnly = true)
    public List<OrderTable> findOrderTables(List<Long> ids) {
        return orderTableRepository.findAllByIdIn(ids);
    }

    @Transactional(readOnly = true)
    public List<OrderTable> findAllByTableGroupId(final Long tableGroupId) {
        return orderTableRepository.findAllByTableGroupId(tableGroupId);
    }
}
