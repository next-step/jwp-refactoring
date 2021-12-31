package kitchenpos.application;

import kitchenpos.common.exceptions.NotFoundEntityException;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import kitchenpos.dto.tablegroup.TableGroupRequest;
import kitchenpos.dto.tablegroup.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final OrderService orderService;
    private final TableService tableService;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderService orderService, final TableService tableService, final TableGroupRepository tableGroupRepository) {
        this.orderService = orderService;
        this.tableService = tableService;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        final List<OrderTable> tables = getOrderTables(request.getOrderTableIds());
        final TableGroup tableGroup = TableGroup.from(tables);
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        return TableGroupResponse.from(savedTableGroup);
    }

    private List<OrderTable> getOrderTables(final List<Long> tableIds) {
        return tableService.findAllById(tableIds);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = findByTableGroupId(tableGroupId);
        orderService.validateOrderStatus(tableGroup.getOrderTables().toList());
        tableGroup.unGroup();
    }

    private TableGroup findByTableGroupId(final Long id) {
        return tableGroupRepository.findById(id)
                .orElseThrow(NotFoundEntityException::new);
    }
}
