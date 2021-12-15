package kitchenpos.application;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.domain.tablegroup.TableGroupRepository;
import kitchenpos.dto.tablegroup.OrderTableIdRequest;
import kitchenpos.dto.tablegroup.TableGroupRequest;
import kitchenpos.dto.tablegroup.TableGroupResponse;
import kitchenpos.exception.NotCompletionOrderException;
import kitchenpos.utils.StreamUtils;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository,
                             final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<Long> orderTableIds = StreamUtils.mapToList(tableGroupRequest.getOrderTables(),
                                                         OrderTableIdRequest::getId);
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        validateExistOrderTables(orderTableIds, orderTables);

        TableGroup tableGroup = tableGroupRepository.save(TableGroup.from(orderTables));
        tableGroup.addOrderTables(orderTables);

        return TableGroupResponse.from(tableGroup);
    }

    private void validateExistOrderTables(List<Long> orderTableIds, List<OrderTable> orderTables) {
        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = findTableGroup(tableGroupId);
        List<Long> orderTableIds = StreamUtils.mapToList(tableGroup.getOrderTables().getValues(), OrderTable::getId);
        validateNotCompletionOrder(orderTableIds);

        tableGroup.ungroup();
    }

    private TableGroup findTableGroup(Long id) {
        return tableGroupRepository.findById(id)
                                   .orElseThrow(EntityNotFoundException::new);
    }

    private void validateNotCompletionOrder(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new NotCompletionOrderException();
        }
    }
}
