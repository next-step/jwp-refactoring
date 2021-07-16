package kitchenpos.tablegroup.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.exception.IllegalOrderTableException;
import kitchenpos.table.exception.NotInitOrderTablesException;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupLinker;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Transactional
@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupLinker tableGroupLinker;

    public TableGroupService(final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository,
                             final TableGroupLinker tableGroupLinker) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupLinker = tableGroupLinker;
    }

    public TableGroupResponse create(final TableGroupRequest request) {
        OrderTables orderTables = new OrderTables(request.getOrderTables());
        List<OrderTable> savedTables = checkInitOrderTables(orderTables);
        final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup());
        savedTables.forEach(table -> table.grouped(savedTableGroup.getId()));
        orderTableRepository.saveAll(savedTables);

        return TableGroupResponse.from(savedTableGroup, savedTables);
    }

    private List<OrderTable> checkInitOrderTables(OrderTables orderTables) {
        List<OrderTable> savedOrderTables = orderTableRepository.findAllById(orderTables.toOrderTableIds());

        if (orderTables.size() != savedOrderTables.size()) {
            throw new NotInitOrderTablesException();
        }

        for (final OrderTable savedOrderTable : savedOrderTables) {
            checkOrderTableEmptyOrGroupNull(savedOrderTable);
        }

        return savedOrderTables;
    }

    private void checkOrderTableEmptyOrGroupNull(OrderTable savedOrderTable) {
        if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalOrderTableException();
        }
    }

    public void ungroup(final Long tableGroupId) {
        OrderTables orderTables = new OrderTables(orderTableRepository.findAllByTableGroupId(tableGroupId));
        tableGroupLinker.validateOrderStatusByTableIds(orderTables.toOrderTableIds());
        orderTables.ungroup();
    }
}
