package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTableStatusValidator;
import kitchenpos.table.domain.TableValidator;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final TableValidator tableValidator;
    private final OrderTableStatusValidator orderTableStatusValidator;

    public TableService(OrderTableRepository orderTableRepository, TableValidator tableValidator, OrderTableStatusValidator orderTableStatusValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableValidator = tableValidator;
        this.orderTableStatusValidator = orderTableStatusValidator;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = orderTableRequest.toOrderTable();
        return OrderTableResponse.of(orderTableRepository.save(orderTable));
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        List<OrderTable> orderTableEntities = orderTableRepository.findAll();
        return orderTableEntities.stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId) {
        OrderTable orderTable = getOrderTableById(orderTableId);
        orderTable.validateHasTableGroupId();
        orderTableStatusValidator.validateOrderStatus(orderTable.getId());

        orderTable.updateEmpty();
        return OrderTableResponse.of(orderTableRepository.save(orderTable));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final int numberOfGuests = orderTableRequest.getNumberOfGuests();
        tableValidator.validateNumberOfGuests(numberOfGuests);

        OrderTable savedOrderTable = getOrderTableById(orderTableId);
        savedOrderTable.validateIsEmpty();
        savedOrderTable.changeNumberOfGuests(numberOfGuests);

        return OrderTableResponse.of(orderTableRepository.save(savedOrderTable));
    }

    private OrderTable getOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(NoSuchElementException::new);
    }
}
