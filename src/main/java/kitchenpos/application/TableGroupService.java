package kitchenpos.application;

import java.util.NoSuchElementException;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    public TableGroupResponse create(final TableGroupRequest request) {
        final List<Long> orderTableIds = request.toOrderTableIds();

        final List<OrderTable> savedOrderTables = findSavedOrderTables(orderTableIds);
        final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup());
        savedTableGroup.addOrderTables(savedOrderTables);
        return TableGroupResponse.of(savedTableGroup);
    }

    private List<OrderTable> findSavedOrderTables(List<Long> orderTableIds) {
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        validateSavedOrderTables(orderTableIds, savedOrderTables);
        return savedOrderTables;
    }

    private void validateSavedOrderTables(List<Long> orderTableIds, List<OrderTable> savedOrderTables) {
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId).orElseThrow(NoSuchElementException::new);
        validateOrderTablesStatus(tableGroupId);
        tableGroup.ungroup();
    }

    private void validateOrderTablesStatus(Long tableGroupId) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("조리중, 식사중 상태에서는 단체를 해제 할 수 없습니다.");
        }
    }

}
