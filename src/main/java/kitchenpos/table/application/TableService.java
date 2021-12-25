package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTableValidatable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.tablegroup.domain.GroupingPair;
import kitchenpos.tablegroup.event.GroupEvent;
import kitchenpos.tablegroup.event.UngroupEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderTableValidatable orderTableValidator;

    public TableService(final OrderTableRepository orderTableRepository, final OrderTableValidatable orderTableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.orderTableValidator = orderTableValidator;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        OrderTable savedOrderTable = orderTableRepository.save(orderTableRequest.toEntity());
        return OrderTableResponse.of(savedOrderTable);
    }

    public List<OrderTableResponse> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();
        return OrderTableResponse.ofList(orderTables);
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (savedOrderTable.hasTableGroup()) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.changeEmptyStatus(orderTableRequest.isEmpty(), orderTableValidator);
        return OrderTableResponse.of(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.changeNumberOfGuests(orderTableRequest.getNumberOfGuests());
        return OrderTableResponse.of(savedOrderTable);
    }

    @EventListener
    public void group(GroupEvent groupEvent) {
        GroupingPair groupingPair = groupEvent.getGroupingPair();
        List<Long> orderTableIds = groupingPair.getOrderTableIds();
        List<OrderTable> savedOrderTables = orderTableRepository.findAllById(orderTableIds);
        OrderTables orderTables = OrderTables.of(savedOrderTables, orderTableIds.size());
        orderTables.group(groupingPair.getTableGroupId());
    }

    @EventListener
    public void ungroup(UngroupEvent ungroupEvent) {
        List<OrderTable> savedOrderTables = orderTableRepository.findByTableGroupId(ungroupEvent.getTableGroupId());
        OrderTables orderTables = new OrderTables(savedOrderTables);
        orderTables.ungroup(orderTableValidator);
    }
}
