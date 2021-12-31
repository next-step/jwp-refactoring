package kitchenpos.application;

import kitchenpos.common.exceptions.EmptyOrderTableException;
import kitchenpos.common.exceptions.NotEmptyOrderTableGroupException;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.ordertable.OrderTableRequest;
import kitchenpos.dto.ordertable.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private  final TableValidator tableValidator;

    public TableService(final OrderTableRepository orderTableRepository, final TableValidator tableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableValidator = tableValidator;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest request) {
        final OrderTable orderTable = orderTableRepository.save(request.toOrderTable());
        return OrderTableResponse.from(orderTable);
    }

    public List<OrderTableResponse> list() {
        final List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTables.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable orderTable = getNotGroupedOrderTableById(orderTableId);
        tableValidator.validateEmpty(orderTable.getId());

        orderTable.changeEmptyStatus(orderTableRequest.isEmpty());
        return OrderTableResponse.from(orderTable);
    }

    private OrderTable getNotGroupedOrderTableById(final Long id) {
        final OrderTable orderTable = findById(id);
        if (Objects.nonNull(orderTable.getTableGroup())) {
            throw new NotEmptyOrderTableGroupException();
        }
        return orderTable;
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest request) {
        final OrderTable orderTable = findById(orderTableId);
        orderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return OrderTableResponse.from(orderTable);
    }

    public OrderTable findOrderTableById(final Long orderTableId) {
        final OrderTable orderTable = findById(orderTableId);
        return orderTable;
    }

    public List<OrderTable> findAllById(final List<Long> ids) {
        return orderTableRepository.findAllById(ids);
    }

    public void grouped(final TableGroup tableGroup, final List<OrderTable> tables) {
        for (OrderTable orderTable : tables) {
            orderTable.group(tableGroup);
        }
        orderTableRepository.saveAll(tables);
    }

    public void ungrouped(final Long tableGroupId) {
        final List<OrderTable> tables = orderTableRepository.findByTableGroupId(tableGroupId);
        tableValidator.validateUnGroup(tables);
        for (OrderTable table : tables) {
            table.unGroup();
        }
        orderTableRepository.saveAll(tables);
    }

    private OrderTable findById(final Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(EmptyOrderTableException::new);
    }
}
