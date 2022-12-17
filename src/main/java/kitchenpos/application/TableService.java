package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.dto.ChaneNumberOfGuestRequest;
import kitchenpos.dto.ChangeEmptyRequest;
import kitchenpos.dto.TableRequest;
import kitchenpos.dto.TableResponse;
import kitchenpos.port.OrderPort;
import kitchenpos.port.OrderTablePort;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
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
        final OrderTable orderTable =
                OrderTable.ofByTableGroupNull(request.getNumberOfGuests(), request.isEmpty());

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
        final List<Order> order = orderPort.findAllByOrderTableIdIn(Arrays.asList(orderTableId));

        savedOrderTable.changeEmpty(request.isEmpty(), order);

        return TableResponse.from(savedOrderTable);
    }

    @Transactional
    public TableResponse changeNumberOfGuests(final Long orderTableId, final ChaneNumberOfGuestRequest request) {
        final OrderTable orderTable = orderTablePort.findById(orderTableId);
        orderTable.changeNumberOfGuests(request.getNumberOfRequest());

        return TableResponse.from(orderTable);
    }
}
