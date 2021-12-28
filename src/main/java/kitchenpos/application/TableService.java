package kitchenpos.application;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.NumberOfGuests;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.dto.OrderTableResponses;
import kitchenpos.exception.KitchenposErrorCode;
import kitchenpos.exception.KitchenposException;
import kitchenpos.exception.KitchenposNotFoundException;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;

@Service
@Transactional(readOnly = true)
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = orderTableRequest.toEntity();

        return OrderTableResponse.from(orderTableRepository.save(orderTable));
    }

    public OrderTableResponses list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();

        return OrderTableResponses.from(orderTables);
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(KitchenposNotFoundException::new);

        orderTable.checkNotGrouped();

        checkTableCookingOrMeal(orderTable);

        orderTable.updateEmpty(request.isEmpty());

        return OrderTableResponse.from(orderTableRepository.save(orderTable));
    }

    private void checkTableCookingOrMeal(OrderTable orderTable) {
        if (orderRepository.existsByOrderTableAndOrderStatusIn(orderTable,
            Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new KitchenposException(KitchenposErrorCode.CONTAINS_USED_TABLE);
        }
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest request) {
        NumberOfGuests numberOfGuests = new NumberOfGuests(request.getNumberOfGuests());

        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(KitchenposNotFoundException::new);

        savedOrderTable.updateNumberOfGuests(numberOfGuests);

        return OrderTableResponse.from(orderTableRepository.save(savedOrderTable));
    }
}
