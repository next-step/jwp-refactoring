package kitchenpos.table.application;

import kitchenpos.ExceptionMessage;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private final OrderService orderService;
    private final TableService tableService;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderService orderService, final TableService tableService,
                             final TableGroupRepository tableGroupRepository) {
        this.orderService = orderService;
        this.tableService = tableService;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<OrderTable> savedOrderTables = findAllOrderTablesByIds(tableGroupRequest.getOrderTableIds());
        final TableGroup tableGroup = new TableGroup(new OrderTables(savedOrderTables));
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        savedTableGroup.group();
        return TableGroupResponse.of(savedTableGroup);
    }

    private List<OrderTable> findAllOrderTablesByIds(List<Long> ids) {
        return ids.stream()
                .map(id -> tableService.findById(id))
                .collect(Collectors.toList());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = findById(tableGroupId);
        final List<Order> orders = orderService.findOrderByOrderTableIds(tableGroup.getOrderTableIds());
        tableGroup.ungroup(orders);
        tableGroupRepository.save(tableGroup);
    }

    private TableGroup findById(Long id) {
        return tableGroupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.NOT_EXIST_TABLE_GROUP.getMessage()));
    }
}
