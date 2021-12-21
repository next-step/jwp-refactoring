package kitchenpos.application.tablegroup;

import kitchenpos.application.table.TableService;
import kitchenpos.domain.table.OrderTables;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.domain.tablegroup.TableGroupRepository;
import kitchenpos.dto.table.OrderTableDto;
import kitchenpos.dto.tablegroup.TableGroupDto;
import kitchenpos.exception.table.NotRegistedMenuOrderTableException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final TableService tableService;
    private final TableGroupValidator tableGroupValidator;
    public TableGroupService(
        final TableGroupRepository tableGroupRepository,
        final TableService tableService,
        final TableGroupValidator tableGroupValidator
    ) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableService = tableService;
        this.tableGroupValidator = tableGroupValidator;
    }

    @Transactional
    public TableGroupDto create(final TableGroupDto tableGroup) {
        final List<Long> orderTableIds = tableGroup.getOrderTables().stream()
                                                    .map(OrderTableDto::getId)
                                                    .collect(Collectors.toList());

        final OrderTables savedOrderTables = OrderTables.of(tableService.findAllByIdIn(orderTableIds));

        checkAllExistOfOrderTables(tableGroup.getOrderTables(), savedOrderTables);

        return TableGroupDto.of(tableGroupRepository.save(TableGroup.of(savedOrderTables)), savedOrderTables);
    }

    private void checkAllExistOfOrderTables(final List<OrderTableDto> orderTables, final OrderTables savedOrderTables) {
        if (orderTables.size() != savedOrderTables.size()) {
            throw new NotRegistedMenuOrderTableException();
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        OrderTables orderTables = tableGroupValidator.getComplateOrderTable(tableGroupId);
        
        for (int index = 0; index < orderTables.size(); index++){
            orderTables.get(index).unGroupTable();
        }            
    }
}
