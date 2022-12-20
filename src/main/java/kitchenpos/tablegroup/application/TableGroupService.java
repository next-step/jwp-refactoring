package kitchenpos.tablegroup.application;

import kitchenpos.order.domain.OrderValidator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupCreateRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.message.TableGroupMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderValidator orderValidator;

    public TableGroupService(final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository,
                             final OrderValidator orderValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        OrderTables orderTables = findOrderTablesByIds(request.getTableIds());
        validateOrderTableSize(request, orderTables);

        TableGroup tableGroup = tableGroupRepository.save(TableGroup.empty());
        orderTables.groupBy(tableGroup.getId());
        return TableGroupResponse.of(tableGroup, orderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(EntityNotFoundException::new);
        OrderTables orderTables = findAllOrderTablesByTableGroupId(tableGroup.getId());
        orderValidator.validateUnGroup(orderTables);
        orderTables.unGroup();
    }

    private OrderTables findOrderTablesByIds(List<Long> orderTableIds) {
        List<OrderTable> orderTableItems = orderTableRepository.findAllById(orderTableIds);
        return new OrderTables(orderTableItems);
    }

    private void validateOrderTableSize(TableGroupCreateRequest request, OrderTables orderTables) {
        if(!orderTables.isSameSize(request.getTableRequests().size())) {
            throw new IllegalArgumentException(TableGroupMessage.CREATE_ERROR_NOT_EQUAL_TABLE_SIZE.message());
        }
    }

    private OrderTables findAllOrderTablesByTableGroupId(Long tableGroupId) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        return new OrderTables(orderTables);
    }
}
