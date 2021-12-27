package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableIdRequest;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(final OrderDao orderDao, final OrderTableDao orderTableDao,
        final TableGroupDao tableGroupDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        request.checkValidSize();
        final List<Long> orderTableIds = request.getOrderTables().stream()
            .map(OrderTableIdRequest::getId)
            .collect(Collectors.toList());

        TableGroup tableGroup = makeTableGroup(orderTableIds);
        return TableGroupResponse.from(tableGroupDao.save(tableGroup));
    }

    private TableGroup makeTableGroup(List<Long> orderTableIds) {
        final OrderTables savedOrderTables = new OrderTables(orderTableDao.findAllByIdIn(orderTableIds));
        savedOrderTables.checkSameSize(orderTableIds.size());
        savedOrderTables.checkNotContainsUsedTable();

        TableGroup tableGroup = new TableGroup(LocalDateTime.now());
        tableGroup.addOrderTables(savedOrderTables);

        return tableGroup;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        OrderTables orderTables = new OrderTables(orderTableDao.findAllByTableGroup_Id(tableGroupId));

        final List<Long> orderTableIds = orderTables.getIds();

        if (orderDao.existsByOrderTable_IdInAndOrderStatusIn(
            orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }

        orderTables.unGroup();
    }
}
