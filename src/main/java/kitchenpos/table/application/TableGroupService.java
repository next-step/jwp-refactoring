package kitchenpos.table.application;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;
import kitchenpos.table.validator.TableValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final TableValidator tableValidator;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(TableValidator tableValidator, OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository) {
        this.tableValidator = tableValidator;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<OrderTable> orderTables = convertToOrderTable(tableGroupRequest.getOrderTables());
        TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.of(orderTables));
        return TableGroupResponse.from(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId).orElseThrow(() -> new IllegalArgumentException());
        tableValidator.validateOrderTableStatus(tableGroup);
        tableGroup.ungroup();
    }

    private List<OrderTable> convertToOrderTable(final List<Long> orderTableIds) {
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        tableValidator.validateOrderTablesExist(orderTableIds, savedOrderTables);
        return savedOrderTables;
    }
}
