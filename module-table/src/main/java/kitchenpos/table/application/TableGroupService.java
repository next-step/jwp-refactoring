package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.dao.TableGroupDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableRequest;
import kitchenpos.table.validator.TableGroupOrderValidator;
import kitchenpos.table.validator.TableGroupValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    private final TableGroupValidator tableGroupValidator;

    private final TableGroupOrderValidator tableGroupOrderValidator;

    public TableGroupService(
            final OrderTableDao orderTableDao,
            final TableGroupDao tableGroupDao,
            final TableGroupValidator tableGroupValidator,
            final TableGroupOrderValidator tableGroupOrderValidator) {
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
        this.tableGroupValidator = tableGroupValidator;
        this.tableGroupOrderValidator = tableGroupOrderValidator;
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
        List<Long> orderTableIds = tableRequests.stream()
                .map(TableRequest::getId)
                .collect(Collectors.toList());
        return orderTableDao.findAllByIdIn(orderTableIds);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = getTableGroup(tableGroupId);
        final List<OrderTable> orderTables = tableGroup.getOrderTables();
        List<Long> tableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if(tableGroupOrderValidator.existsDinningTable(tableIds)){
            throw new IllegalArgumentException();
        }


        for (final OrderTable orderTable : orderTables) {
            orderTable.unGroup();
        }
    }

    private TableGroup getTableGroup(Long tableGroupId) {
        return tableGroupDao.findById(tableGroupId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
