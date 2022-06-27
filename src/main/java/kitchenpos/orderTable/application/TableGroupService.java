package kitchenpos.orderTable.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.application.OrderService;
import kitchenpos.orderTable.domain.OrderTable;
import kitchenpos.orderTable.domain.OrderTableRepository;
import kitchenpos.orderTable.domain.OrderTables;
import kitchenpos.orderTable.domain.TableGroup;
import kitchenpos.orderTable.domain.TableGroupRepository;
import kitchenpos.orderTable.dto.TableGroupRequest;
import kitchenpos.orderTable.dto.TableGroupResponse;
import kitchenpos.orderTable.validator.TableGroupValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final OrderService orderService;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupValidator tableGroupValidator;

    public TableGroupService(final OrderService orderService, final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository, TableGroupValidator tableGroupValidator) {
        this.orderService = orderService;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupValidator = tableGroupValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<Long> orderTablesId = tableGroupRequest.getOrderTables();
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTablesId);
        TableGroup tableGroup = TableGroup.from(OrderTables.from(savedOrderTables), orderTablesId);
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        return TableGroupResponse.from(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        tableGroupValidator.validateComplete(orderTables.stream()
                .map(OrderTable::id)
                .collect(Collectors.toList()));
        for (final OrderTable orderTable : orderTables) {
            orderTable.unGroup();
            orderTableRepository.save(orderTable);
        }
    }
}
