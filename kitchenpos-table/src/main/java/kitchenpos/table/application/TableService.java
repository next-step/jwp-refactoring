package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.ChaneNumberOfGuestRequest;
import kitchenpos.table.dto.ChangeEmptyRequest;
import kitchenpos.table.dto.TableRequest;
import kitchenpos.table.dto.TableResponse;
import kitchenpos.table.port.OrderTablePort;
import kitchenpos.table.port.OrderTableValidatorPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TableService {
    private final OrderTablePort orderTablePort;
    private final OrderTableValidatorPort orderTableValidatorPort;

    public TableService(final OrderTablePort orderTablePort, final OrderTableValidatorPort orderTableValidatorPort) {
        this.orderTablePort = orderTablePort;
        this.orderTableValidatorPort = orderTableValidatorPort;
    }

    public TableResponse create(TableRequest request) {
        final OrderTable orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());

        final OrderTable saveOrderTable = orderTablePort.save(orderTable);

        return TableResponse.from(saveOrderTable);
    }

    @Transactional(readOnly = true)
    public List<TableResponse> list() {
        final List<OrderTable> orderTable = orderTablePort.findAll();

        return orderTable.stream()
                .map(TableResponse::from)
                .collect(Collectors.toList());
    }

    public TableResponse changeEmpty(Long orderTableId, ChangeEmptyRequest request) {
        final OrderTable savedOrderTable = orderTablePort.findById(orderTableId);
        orderTableValidatorPort.validChangeEmpty(savedOrderTable);

        savedOrderTable.changeEmpty(request.isEmpty());

        return TableResponse.from(savedOrderTable);
    }

    @Transactional
    public TableResponse changeNumberOfGuests(Long orderTableId, ChaneNumberOfGuestRequest request) {
        final OrderTable orderTable = orderTablePort.findById(orderTableId);
        orderTableValidatorPort.validChangeNumberOfGuest(request.getNumberOfRequest());

        orderTable.changeNumberOfGuests(request.getNumberOfRequest());

        return TableResponse.from(orderTable);
    }
}
