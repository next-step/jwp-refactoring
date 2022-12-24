package kitchenpos.tablegroup.application;

import kitchenpos.common.ErrorCode;
import kitchenpos.tablegroup.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class TableGroupService {
    private static final int NUMBER_OF_MINIMUM_ORDER_TABLES = 2;

    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderTableValidator orderTableValidator;

    public TableGroupService(TableGroupRepository tableGroupRepository,
                             OrderTableRepository orderTableRepository, OrderTableValidator orderTableValidator) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderTableValidator = orderTableValidator;
    }

    @Transactional
    public TableGroupResponse create(TableGroupRequest request) {
        List<OrderTable> orderTables = findAllOrderTableByIds(request.getOrderTables());
        return TableGroupResponse.of(tableGroupRepository.save(TableGroupRequest.toTableGroup(orderTables)));
    }

    private List<OrderTable> findAllOrderTableByIds(List<Long> orderTableIds) {
        if (orderTableIds.size() < NUMBER_OF_MINIMUM_ORDER_TABLES) {
            throw new IllegalArgumentException(ErrorCode.INVALID_NUMBER_OF_ORDER_TABLES.getErrorMessage());
        }
        List<OrderTable> orderTables = orderTableRepository.findAllById(orderTableIds);
        if (orderTableIds.size() != orderTables.size()) {
            throw new EntityNotFoundException(ErrorCode.NOT_FOUND_BY_ID.getErrorMessage());
        }
        return orderTables;
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        TableGroup tableGroup = findTableGroupById(tableGroupId);
        orderTableValidator.validateToUngroup(tableGroup.getOrderTableIds());

        tableGroup.ungroup();

        tableGroupRepository.save(tableGroup);
    }

    private TableGroup findTableGroupById(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_BY_ID.getErrorMessage()));
    }
}
