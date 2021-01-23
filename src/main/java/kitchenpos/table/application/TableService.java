package kitchenpos.table.application;

import kitchenpos.order.application.OrderService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.tablegroup.application.TableGroupService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupService tableGroupService;
    private final OrderService ordersService;

    public TableService(
            OrderTableRepository orderTableRepository,
            TableGroupService tableGroupService,
            OrderService ordersService
    ) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupService = tableGroupService;
        this.ordersService =  ordersService;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = new OrderTable(
                orderTableRequest.getNumberOfGuests(),
                orderTableRequest.isEmpty()
        );

        Long tableGroupId = orderTableRequest.getTableGroupId();
        if (tableGroupId != null) {
            orderTable.changeTableGroup(tableGroupService.findById(tableGroupId));
        }

        return OrderTableResponse.of(orderTableRepository.save(orderTable));
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll().stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        ordersService.validateChangeEmpty(savedOrderTable);

        savedOrderTable.changeEmpty(orderTableRequest.isEmpty());

        return OrderTableResponse.of(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrderTable.updateNumberOfGuests(orderTableRequest.getNumberOfGuests());

        return OrderTableResponse.of(savedOrderTable);

    }
}
