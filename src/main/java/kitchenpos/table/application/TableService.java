package kitchenpos.table.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.Empty;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.ChangeEmptyRequest;
import kitchenpos.table.dto.ChangeNumberOfGuestsRequest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.tablegroup.domain.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class TableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(
            OrderRepository orderRepository
            , OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest request) {
        NumberOfGuests numberOfGuests = NumberOfGuests.of(request.getNumberOfGuests());
        Empty empty = Empty.of(request.isEmpty());
        OrderTable orderTable = OrderTable.of(null, numberOfGuests, empty);

        return OrderTableResponse.of(orderTableRepository.save(orderTable));
    }

    public List<OrderTableResponse> list() {
        final List<OrderTable> orderTables = orderTableRepository.findAll();

        return orderTables.stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final ChangeEmptyRequest request) {
        OrderTable persistOrderTable = findById(orderTableId);
        if (isCookingOrMealExists(persistOrderTable)) {
            throw new IllegalArgumentException();
        }

        Empty empty = Empty.of(request.isEmpty());
        persistOrderTable.changeEmpty(empty);
        return OrderTableResponse.of(persistOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final ChangeNumberOfGuestsRequest request) {
        NumberOfGuests numberOfGuests = NumberOfGuests.of(request.getNumberOfGuests());
        OrderTable persistOrderTable = findById(orderTableId);

        persistOrderTable.changeNumberOfGuests(numberOfGuests);
        return OrderTableResponse.of(persistOrderTable);
    }

    public OrderTable findById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(NoSuchElementException::new);
    }

    public boolean isCookingOrMealExists(OrderTable orderTable) {
        List<OrderStatus> orderStatuses = Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);
        return orderRepository.existsByOrderTableAndOrderStatusIn(orderTable, orderStatuses);
    }

    public List<OrderTable> findAllByIdIn(List<Long> orderTableIds) {
        return orderTableRepository.findAllByIdIn(orderTableIds);
    }

    public List<OrderTable> findAllByTableGroup(TableGroup tableGroup) {
        return orderTableRepository.findAllByTableGroup(tableGroup);
    }
}
