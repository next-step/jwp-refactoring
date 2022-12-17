package kitchenpos.application;

import kitchenpos.dto.TableRequest;
import kitchenpos.port.OrderPort;
import kitchenpos.port.OrderTablePort;
import kitchenpos.domain.type.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class TableService {
    private final OrderPort orderPort;
    private final OrderTablePort orderTablePort;

    public TableService(final OrderPort orderPort, final OrderTablePort orderTablePort) {
        this.orderPort = orderPort;
        this.orderTablePort = orderTablePort;
    }

    @Transactional
    public OrderTable create(final TableRequest request) {
        orderTable.setTableGroupId(null);

        return orderTablePort.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTablePort.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTable orderTable) {
        final OrderTable savedOrderTable = orderTablePort.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }

        if (orderPort.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.setEmpty(orderTable.isEmpty());

        return orderTablePort.save(savedOrderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTable orderTable) {
        final int numberOfGuests = orderTable.getNumberOfGuests();

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        final OrderTable savedOrderTable = orderTablePort.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.setNumberOfGuests(numberOfGuests);

        return orderTablePort.save(savedOrderTable);
    }
}
