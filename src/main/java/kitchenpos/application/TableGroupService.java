package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final TableService tableService;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(TableService tableService, TableGroupRepository tableGroupRepository) {
        this.tableService = tableService;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final TableGroupRequest tableGroupRequest) {
        List<OrderTable> savedOrderTables = tableService.findAllByIdIn(tableGroupRequest.getOrderTableIds());

        TableGroup tableGroup = new TableGroup();
        tableGroup.validateOrderTableEmptyOrNonNull(savedOrderTables);
        tableGroup.addAllOrderTables(savedOrderTables);
        tableGroup.validateOrderTablesSize();

        return tableGroupRepository.save(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(IllegalArgumentException::new);

        tableGroup.validateOrderStatus(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());
        tableGroup.unGroup();
    }
}
