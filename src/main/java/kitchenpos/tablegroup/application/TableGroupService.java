package kitchenpos.tablegroup.application;

import kitchenpos.common.exception.OrderStatusException;
import kitchenpos.order.application.OrderService;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.OrderTableIdRequests;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final TableService tableService;
    private final OrderService orderService;

    public TableGroupService(
            TableGroupRepository tableGroupRepository
            , TableService tableService
            , OrderService orderService
    ) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableService = tableService;
        this.orderService = orderService;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        OrderTableIdRequests orderTables = OrderTableIdRequests.of(request.getOrderTables());
        List<Long> orderTableIds = orderTables.getOrderTableIds();
        OrderTables persistOrderTables = OrderTables.of(tableService.findAllByIdIn(orderTableIds));
        persistOrderTables.validateGroup(orderTableIds);

        TableGroup tableGroup = TableGroup.of(LocalDateTime.now());
        tableGroup.addOrderTable(persistOrderTables.getOrderTables());
        TableGroup persistTableGroup = tableGroupRepository.save(tableGroup);
        return TableGroupResponse.of(persistTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = findById(tableGroupId);
        OrderTables orderTables = OrderTables.of(tableService.findAllByTableGroup(tableGroup));

        if (orderService.isCookingOrMealExists(orderTables)) {
            throw new OrderStatusException();
        }
        orderTables.ungroup();
    }

    public TableGroup findById(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
                .orElseThrow(NoSuchElementException::new);
    }
}
