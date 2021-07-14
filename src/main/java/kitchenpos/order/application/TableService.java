package kitchenpos.order.application;

import kitchenpos.order.domain.*;
import kitchenpos.order.dto.TableEmptyRequest;
import kitchenpos.order.dto.TableNumberOfGuestsRequest;
import kitchenpos.order.dto.TableRequest;
import kitchenpos.order.dto.TableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Transactional
@Service
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableService(final OrderTableRepository orderTableRepository, OrderRepository orderRepository, TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
        this.tableGroupRepository = tableGroupRepository;
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
        final List<Order> orders = orderRepository.findAllByOrderTableId(orderTableId);
        verifyChangeableEmpty(orders);
        final OrderTable orderTable = findOrderTableById(orderTableId);
        verifyExistsTableGroup(orderTable);
        orderTable.changeEmpty(tableEmptyRequest.isEmpty());
        return TableResponse.of(orderTable);
    }

    private void verifyExistsTableGroup(OrderTable orderTable) {
        if (Objects.nonNull(orderTable.getTableGroup())) {
            throw new IllegalArgumentException("단체지정이 되어있으면 안됩니다.");
        }
    }

    private void verifyChangeableEmpty(List<Order> orders) {
        if (orders.stream()
                .anyMatch(order -> !order.isCompletion())) {
            throw new IllegalArgumentException("주문테이블의 주문상태가 조리나 식사입니다.");
        }
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("주문테이블이 존재하지 않습니다."));
    }

    public TableResponse changeNumberOfGuests(final Long orderTableId,
                                           final TableNumberOfGuestsRequest tableNumberOfGuestsRequest) {
        OrderTable orderTable = findOrderTableById(orderTableId);
        orderTable.changeNumberOfGuests(tableNumberOfGuestsRequest.getNumberOfGuests());
        return TableResponse.of(orderTable);
    }
}
