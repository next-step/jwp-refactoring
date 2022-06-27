package kitchenpos.tableGroup.application;

import kitchenpos.core.exception.ExceptionType;
import kitchenpos.core.exception.NotFoundException;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tableGroup.domain.TableGroupValidator;
import kitchenpos.tableGroup.domain.TableGroup;
import kitchenpos.tableGroup.domain.TableGroupRepository;
import kitchenpos.tableGroup.dto.request.TableGroupRequest;
import kitchenpos.tableGroup.dto.response.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableGroupService {
    private final TableService tableService;
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupValidator tableGroupValidator;

    public TableGroupService(TableService tableService, TableGroupRepository tableGroupRepository, TableGroupValidator tableGroupValidator) {
        this.tableService = tableService;
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupValidator = tableGroupValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        final List<OrderTable> orderTables = tableService.findOrderTablesByIdIn(request.getOrderTableIds());
        tableGroupValidator.validate(request.getOrderTableIds(), orderTables);
        final TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.generate());
        savedTableGroup.mapIntoTable(orderTables);

        return TableGroupResponse.toResponse(savedTableGroup, orderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
            .orElseThrow(() -> new NotFoundException(ExceptionType.NOT_EXIST_TABLE_GROUP));

        tableGroupValidator.validateOrderTablesStatus(tableGroup);
        tableGroup.unGroup();
    }
}
