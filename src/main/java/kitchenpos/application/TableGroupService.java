package kitchenpos.application;

import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.dao.TableGroupRepository;
import kitchenpos.common.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.TableGroupRequest;
import kitchenpos.dto.response.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
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
    public TableGroupResponse create(TableGroupRequest request) {
        List<OrderTable> orderTables = findOrderTablesByIdsWithValidation(request.getOrderTableIds());
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        tableGroup.addOrderTables(orderTables);
        return TableGroupResponse.of(tableGroup);
    }

    private List<OrderTable> findOrderTablesByIdsWithValidation(List<Long> orderTableIds) {
        if (CollectionUtils.isEmpty(orderTableIds)) {
            throw new IllegalArgumentException("테이블 아이디 목록이 비어있습니다.");
        }
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException("존재하지 않는 테이블이 있습니다.");
        }
        return orderTables;
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(NoSuchElementException::new);
        validateUngroup(tableGroupId);
        tableGroup.ungroup();
    }

    private void validateUngroup(Long tableGroupId) {
        final List<Long> orderTableIds = findOrderTableIdsByTableGroupId(tableGroupId);
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("조리중이거나 식사중인 테이블이 있습니다.");
        }
    }

    private List<Long> findOrderTableIdsByTableGroupId(Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }
}
