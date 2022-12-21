package kitchenpos.order.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.validator.OrderTableValidator;
import kitchenpos.tablegroup.dto.ChaneNumberOfGuestRequest;
import kitchenpos.order.dto.ChangeEmptyRequest;
import kitchenpos.order.dto.TableRequest;
import kitchenpos.order.dto.TableResponse;
import kitchenpos.order.port.OrderPort;
import kitchenpos.order.port.OrderTablePort;
import kitchenpos.order.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TableService {
    private final OrderTablePort orderTablePort;
    private final OrderTableValidator orderTableValidator;

    public TableService(final OrderTablePort orderTablePort, final OrderTableValidator orderTableValidator) {
        this.orderTablePort = orderTablePort;
        this.orderTableValidator = orderTableValidator;
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
        orderTableValidator.validChangeEmpty(savedOrderTable);

        savedOrderTable.changeEmpty(request.isEmpty());

        return TableResponse.from(savedOrderTable);
    }

    @Transactional
    public TableResponse changeNumberOfGuests(Long orderTableId, ChaneNumberOfGuestRequest request) {
        final OrderTable orderTable = orderTablePort.findById(orderTableId);
        orderTableValidator.validChangeNumberOfGuest(request.getNumberOfRequest());

        orderTable.changeNumberOfGuests(request.getNumberOfRequest());

        return TableResponse.from(orderTable);
    }
}
