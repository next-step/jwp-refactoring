package kitchenpos.order.application;

import kitchenpos.order.application.exception.TableNotFoundException;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableRepository;
import kitchenpos.order.domain.TableValidator;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.order.dto.TableRequest;
import kitchenpos.order.dto.TableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TableService {
    private final TableRepository tableRepository;
    private final TableValidator tableValidator;

    public TableService(final TableRepository tableRepository, final TableValidator tableValidator) {
        this.tableRepository = tableRepository;
        this.tableValidator = tableValidator;
    }

    @Transactional
    public TableResponse create(final TableRequest request) {
        OrderTable save = tableRepository.save(request.toEntity());
        return TableResponse.of(save);
    }

    public List<TableResponse> list() {
        List<OrderTable> orderTables = tableRepository.findAll();
        return TableResponse.ofList(orderTables);
    }

    @Transactional
    public TableResponse changeEmpty(final Long tableId) {
        final OrderTable orderTable = getTable(tableId);
        tableValidator.validate(orderTable);

        orderTable.changeEmpty();
        return TableResponse.of(orderTable);
    }

    @Transactional
    public TableResponse changeGuests(final Long tableId, final TableRequest request) {
        final OrderTable orderTable = getTable(tableId);
        tableValidator.validateTableState(orderTable);

        orderTable.changeGuests(request.getNumberOfGuests());
        return TableResponse.of(orderTable);
    }

    private OrderTable getTable(Long tableId) {
        return tableRepository.findById(tableId)
                .orElseThrow(TableNotFoundException::new);
    }

    @Transactional
    public TableResponse changeOrderStatus(Long tableId, OrderStatusRequest request) {
        final OrderTable orderTable = getTable(tableId);
        orderTable.changeStatus(request.getOrderStatus());
        return TableResponse.of(orderTable);
    }
}
