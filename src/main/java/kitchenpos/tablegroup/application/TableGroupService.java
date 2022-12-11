package kitchenpos.tablegroup.application;

import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.order.application.OrderValidator;
import kitchenpos.tablegroup.domain.OrderTable;
import kitchenpos.tablegroup.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.exception.OrderTableExceptionCode;
import kitchenpos.tablegroup.exception.TableGroupExceptionCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableValidator tableValidator;

    public TableGroupService(TableGroupRepository tableGroupRepository, OrderTableRepository orderTableRepository,
            TableValidator tableValidator) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableValidator = tableValidator;
    }

    @Transactional
    public TableGroupResponse create(TableGroupRequest request) {
        List<OrderTable> orderTables = findAllOrderTableByIds(request.getOrderTables());
        return TableGroupResponse.of(tableGroupRepository.save(TableGroupRequest.toTableGroup(orderTables)));
    }

    private List<OrderTable> findAllOrderTableByIds(List<Long> orderTableIds) {
        List<OrderTable> orderTables = orderTableRepository.findAllById(orderTableIds);
        if (orderTableIds.size() != orderTables.size()) {
            throw new EntityNotFoundException(OrderTableExceptionCode.NOT_FOUND_BY_ID.getMessage());
        }

        return orderTables;
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        TableGroup saveTableGroup = findTableGroupById(tableGroupId);
        tableValidator.validateToUngroup(saveTableGroup.getOrderTableIds());

        saveTableGroup.ungroup();

        tableGroupRepository.save(saveTableGroup);
    }

    private TableGroup findTableGroupById(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new EntityNotFoundException(TableGroupExceptionCode.NOT_FOUND_BY_ID.getMessage()));
    }
}
