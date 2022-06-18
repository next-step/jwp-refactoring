package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderRepository orderRepository,
                             OrderTableRepository orderTableRepository,
                             TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(TableGroupRequest request) {
        List<OrderTableEntity> orderTables = validateCreate(request);
        TableGroupEntity tableGroup = tableGroupRepository.save(new TableGroupEntity());
        tableGroup.addOrderTables(orderTables);
        return TableGroupResponse.of(tableGroup);
    }

    private List<OrderTableEntity> validateCreate(TableGroupRequest request) {
        List<Long> orderTableIds = validateNotEmptyIds(request);
        List<OrderTableEntity> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        if (orderTableIds.size() != orderTables.size()) {
            throw new InvalidTableGroupException("존재하지 않는 테이블이 있습니다.");
        }
        return orderTables;
    }

    private List<Long> validateNotEmptyIds(TableGroupRequest request) {
        List<Long> orderTableIds = request.toOrderTableId();
        if (CollectionUtils.isEmpty(orderTableIds)) {
            throw new InvalidTableGroupException("단체 지정할 테이블이 없습니다.");
        }
        return orderTableIds;
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        TableGroupEntity tableGroup = tableGroupRepository.findById(tableGroupId)
                                                          .orElseThrow(NotFoundTableGroupException::new);
        validateUngroup(tableGroupId);
        tableGroup.ungroup();
    }

    private void validateUngroup(Long tableGroupId) {
        List<Long> orderTableIds = findOrderTableIdsByTableGroupId(tableGroupId);
        if (hasUncompletedOrder(orderTableIds)) {
            throw new CannotUngroupException();
        }
    }

    private boolean hasUncompletedOrder(List<Long> orderTableIds) {
        return orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL));
    }

    private List<Long> findOrderTableIdsByTableGroupId(Long tableGroupId) {
        List<OrderTableEntity> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        return orderTables.stream()
                          .map(OrderTableEntity::getId)
                          .collect(Collectors.toList());
    }
}
