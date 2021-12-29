package api.table.application;

import api.exception.table.OrderTableNotFoundException;
import api.table.domain.OrderTable;
import api.table.domain.OrderTableRepository;
import api.table.dto.ChangeEmptyRequest;
import api.table.dto.ChangeGuestNumberRequest;
import api.table.dto.OrderTableResponse;
import api.table.dto.OrderTableSaveRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final TableValidator tableValidator;

    public TableService(OrderTableRepository orderTableRepository, TableValidator tableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableValidator = tableValidator;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableSaveRequest request) {
        OrderTable orderTable = orderTableRepository.save(request.toEntity());
        return OrderTableResponse.of(orderTable);
    }

    public List<OrderTableResponse> list() {
        return OrderTableResponse.ofList(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final ChangeEmptyRequest request) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(OrderTableNotFoundException::new);
        tableValidator.validateChangeEmpty(orderTable);
        orderTable.changeEmpty(request.isEmpty());
        return OrderTableResponse.of(orderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final ChangeGuestNumberRequest request) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(OrderTableNotFoundException::new);
        orderTable.updateNumberOfGuests(request.getNumberOfGuests());
        return OrderTableResponse.of(orderTable);
    }
}
