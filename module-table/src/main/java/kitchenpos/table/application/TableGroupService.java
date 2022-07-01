package kitchenpos.table.application;

import kitchenpos.common.exception.ExceptionType;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.request.TableGroupRequest;
import kitchenpos.table.dto.response.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableGroupService {
    private final OrderTableService orderTableService;
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupValidator tableGroupValidator;

    public TableGroupService(OrderTableService orderTableService, TableGroupRepository tableGroupRepository) {
        this.orderTableService = orderTableService;
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupValidator = new TableGroupValidator();
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        final List<OrderTable> orderTables = orderTableService.findOrderTablesByIdIn(request.getOrderTableIds());
        tableGroupValidator.validate(request.getOrderTableIds(), orderTables);
        final TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.generate());
        savedTableGroup.mapIntoTable(orderTables);

        return TableGroupResponse.toResponse(savedTableGroup, orderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
            .orElseThrow(() -> new NotFoundException(ExceptionType.NOT_EXIST_TABLE_GROUP));

        tableGroup.unGroup();
        tableGroupRepository.save(tableGroup);
    }
}
