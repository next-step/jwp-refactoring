package kitchenpos.application;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.TableEmptyRequest;
import kitchenpos.dto.TableNumberOfGuestsRequest;
import kitchenpos.dto.TableRequest;
import kitchenpos.dto.TableResponse;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public TableService(final OrderTableRepository orderTableRepository, OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    public TableResponse create(final TableRequest tableRequest) {
        OrderTable orderTable = new OrderTable(tableRequest.getNumberOfGuests(), tableRequest.isEmpty());
        OrderTable saveOrderTable = orderTableRepository.save(orderTable);
        return TableResponse.of(saveOrderTable);
    }

    @Transactional(readOnly = true)
    public List<TableResponse> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTables.stream()
                .map(TableResponse::of)
                .collect(Collectors.toList());
    }

    public TableResponse changeEmpty(final Long orderTableId, final TableEmptyRequest tableEmptyRequest) {
        final OrderTable orderTable = findOrderTableById(orderTableId);
        verifyAvailableOrderStatus(orderTableId);
        orderTable.changeEmpty(tableEmptyRequest.isEmpty());
        return TableResponse.of(orderTable);
    }

    private void verifyAvailableOrderStatus(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId,
                Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId).orElseThrow(IllegalArgumentException::new);
    }

    public TableResponse changeNumberOfGuests(final Long orderTableId,
                                           final TableNumberOfGuestsRequest tableNumberOfGuestsRequest) {
        OrderTable orderTable = findOrderTableById(orderTableId);
        orderTable.changeNumberOfGuests(tableNumberOfGuestsRequest.getNumberOfGuests());
        return TableResponse.of(orderTable);
    }
}
