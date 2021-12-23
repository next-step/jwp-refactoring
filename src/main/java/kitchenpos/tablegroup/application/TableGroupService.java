package kitchenpos.tablegroup.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupDao;
import kitchenpos.tablegroup.dto.OrderTableIdRequests;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TableGroupService {

    private final OrderDao orderDao;
    private final TableGroupDao tableGroupDao;
    private final TableService tableService;

    public TableGroupService(
            OrderDao orderDao
            , TableGroupDao tableGroupDao
            , TableService tableService) {
        this.orderDao = orderDao;
        this.tableGroupDao = tableGroupDao;
        this.tableService = tableService;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        OrderTableIdRequests orderTables = OrderTableIdRequests.of(request.getOrderTables());
        List<Long> orderTableIds = orderTables.getOrderTableIds();
        OrderTables persistOrderTables = OrderTables.of(tableService.findAllByIdIn(orderTableIds));
        persistOrderTables.validateCreate(orderTableIds);

        TableGroup tableGroup = TableGroup.of(LocalDateTime.now());
        tableGroup.addOrderTable(persistOrderTables.getOrderTables());
        TableGroup persistTableGroup = tableGroupDao.save(tableGroup);
        return TableGroupResponse.of(persistTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = findById(tableGroupId);
        OrderTables orderTables = OrderTables.of(tableService.findAllByTableGroup(tableGroup));
        List<Long> orderTableIds = orderTables.getIds();

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
        orderTables.ungroup();
    }

    public TableGroup findById(Long tableGroupId) {
        return tableGroupDao.findById(tableGroupId)
                .orElseThrow(NoSuchElementException::new);
    }
}
