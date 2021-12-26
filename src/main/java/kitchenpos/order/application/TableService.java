package kitchenpos.order.application;

import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final TableValidator tableValidator;

    public TableService(OrderTableRepository orderTableRepository, TableValidator tableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableValidator = tableValidator;
    }

    @Transactional
    public OrderTableResponse create(OrderTableRequest orderTableRequest) {
        OrderTable orderTable = orderTableRepository.save(orderTableRequest.toOrderTable());
        return OrderTableResponse.from(orderTable);
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll()
                .stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, OrderTableRequest request) {
        final OrderTable orderTable = findOrderTable(orderTableId);
        tableValidator.validateCompletion(orderTable);
        orderTable.changeEmpty(request.isEmpty());

        return OrderTableResponse.from(orderTableRepository.save(orderTable));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest request) {
        final OrderTable savedOrderTable = findOrderTable(orderTableId);

        savedOrderTable.updateNumberOfGuests(request.getNumberOfGuests());

        return OrderTableResponse.from(orderTableRepository.save(savedOrderTable));
    }

    public OrderTable findOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }

    @Transactional
    public void grouped(Long tableGroupId, List<OrderTable> orderTables) {
        orderTables.forEach(orderTable -> {
            orderTable.group(tableGroupId);
        });

        orderTableRepository.saveAll(orderTables);
    }

    @Transactional
    public void ungrouped(Long tableGroupId) {
        List<OrderTable> orderTables = findByTableGroupId(tableGroupId);
        tableValidator.validateCompletion(orderTables);
        orderTables.forEach(OrderTable::ungroup);

        orderTableRepository.saveAll(orderTables);
    }

    public List<OrderTable> findAllByIdIn(List<Long> orderTableIds) {
        return orderTableRepository.findAllByIdIn(orderTableIds);
    }

    public List<OrderTable> findByTableGroupId(Long tableGroupId) {
        return orderTableRepository.findByTableGroupId(tableGroupId);
    }
}
