package kitchenpos.application;

import java.util.ArrayList;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableRequest;
import kitchenpos.validate.TableGroupValidator;
import kitchenpos.validate.TableValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class TableGroupService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    private final TableGroupValidator tableGroupValidator;
    private final TableValidator tableValidator;

    public TableGroupService(
            final OrderDao orderDao,
            final OrderTableDao orderTableDao,
            final TableGroupDao tableGroupDao,
            final TableGroupValidator tableGroupValidator,
            final TableValidator tableValidator) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
        this.tableGroupValidator = tableGroupValidator;
        this.tableValidator = tableValidator;
    }

    @Transactional
    public TableGroup create(final TableGroupRequest tableGroupRequest) {
        List<TableRequest> tableRequests = tableGroupRequest.getOrderTables();
        tableGroupValidator.validateRequest(tableRequests);

        List<OrderTable> orderTables = getOrderTables(tableRequests);
        tableGroupValidator.validateCreate(tableRequests, orderTables);

        final TableGroup savedTableGroup = tableGroupDao.save(new TableGroup(orderTables));
        for (final OrderTable savedOrderTable : orderTables) {
            savedOrderTable.grouping(savedTableGroup);
        }

        return savedTableGroup;
    }

    private List<OrderTable> getOrderTables(List<TableRequest> tableRequests) {
        List<OrderTable> orderTables = new ArrayList<>();
        for (TableRequest orderTable : tableRequests) {
            orderTables.add(getOrderTable(orderTable));
        }
        return orderTables;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = getTableGroup(tableGroupId);
        final List<OrderTable> orderTables = tableGroup.getOrderTables();

        for (OrderTable orderTable : orderTables) {
            tableValidator.validateDinning(orderDao.findAllByOrderTable(orderTable));
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.upGroup();
        }
    }

    private TableGroup getTableGroup(Long tableGroupId) {
        return tableGroupDao.findById(tableGroupId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private OrderTable getOrderTable(TableRequest orderTable) {
        return orderTableDao.findById(orderTable.getId())
                .orElseThrow(IllegalArgumentException::new);
    }
}
