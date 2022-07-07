package kitchenpos.table.application;

import kitchenpos.table.domain.*;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final TableValidator tableValidator;
    private final OrderTableStatusValidator orderTableStatusValidator;

    public TableGroupService(OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository,
                             TableValidator tableValidator, OrderTableStatusValidator orderTableStatusValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.tableValidator = tableValidator;
        this.orderTableStatusValidator = orderTableStatusValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        List<OrderTable> orderTables = getOrderTablesByIds(request.getOrderTableIds());
        TableGroup tableGroup = request.toTableGroup(orderTables);
        return TableGroupResponse.of(tableGroupRepository.save(tableGroup));
    }

    private List<OrderTable> getOrderTablesByIds(List<Long> orderTableIds) {
        List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        tableValidator.validateOrderTableEqualsSize(savedOrderTables, orderTableIds);
        return savedOrderTables;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = getTableGroupById(tableGroupId);
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroup(tableGroup);
        orderTableStatusValidator.validateOrderTablesStatus(orderTables);
        tableGroup.unGroup();
    }

    private TableGroup getTableGroupById(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
                .orElseThrow(NoSuchElementException::new);
    }
}
