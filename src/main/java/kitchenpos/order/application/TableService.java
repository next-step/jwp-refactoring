package kitchenpos.order.application;

import kitchenpos.order.domain.Order;
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
    private final OrderPort orderPort;
    private final OrderTablePort orderTablePort;

    public TableService(final OrderPort orderPort, final OrderTablePort orderTablePort) {
        this.orderPort = orderPort;
        this.orderTablePort = orderTablePort;
    }

    public TableResponse create(final TableRequest request) {
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

    public TableResponse changeEmpty(final Long orderTableId, final ChangeEmptyRequest request) {
        final OrderTable savedOrderTable = orderTablePort.findById(orderTableId);
        final List<Order> order = orderPort.findByOrderTableId(orderTableId);
        order.forEach(Order::validUngroupable);

        savedOrderTable.changeEmpty(request.isEmpty());

        return TableResponse.from(savedOrderTable);
    }

    @Transactional
    public TableResponse changeNumberOfGuests(final Long orderTableId, final ChaneNumberOfGuestRequest request) {
        final OrderTable orderTable = orderTablePort.findById(orderTableId);
        orderTable.changeNumberOfGuests(request.getNumberOfRequest());

        return TableResponse.from(orderTable);
    }
}
