package kitchenpos.tablegroup.application;

import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.exception.EntityNotFoundExceptionConstants;

import kitchenpos.tablegroup.domain.OrderTableEmpty;
import kitchenpos.tablegroup.domain.OrderTableGuests;
import kitchenpos.tablegroup.domain.OrderTable;
import kitchenpos.tablegroup.dto.OrderTableRequest;
import kitchenpos.tablegroup.dto.OrderTableResponse;
import kitchenpos.tablegroup.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableService {
    private final OrderTableValidator orderTableValidator;
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderTableValidator orderTableValidator, OrderTableRepository orderTableRepository) {
        this.orderTableValidator = orderTableValidator;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(OrderTableRequest request) {
        return OrderTableResponse.of(orderTableRepository.save(request.toOrderTable()));
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return OrderTableResponse.list(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(Long orderTableId, OrderTableEmpty request) {
        OrderTable savedOrderTable = findOrderTableById(orderTableId);
        orderTableValidator.validateToChangeEmpty(savedOrderTable);

        savedOrderTable.changeEmpty(request.isEmpty());

        return OrderTableResponse.of(orderTableRepository.save(savedOrderTable));
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundExceptionConstants.NOT_FOUND_BY_ID));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(Long orderTableId, OrderTableGuests request) {
        OrderTable orderTable = findOrderTableById(orderTableId);
        orderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return OrderTableResponse.of(orderTableRepository.save(orderTable));
    }
}
