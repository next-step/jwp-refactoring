package kitchenpos.application;

import kitchenpos.advice.exception.OrderTableException;
import kitchenpos.advice.exception.TableGroupException;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.TableGroupRequest;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final OrderService orderService;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderService orderService,
                             final TableGroupRepository tableGroupRepository) {
        this.orderService = orderService;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final TableGroupRequest tableGroupRequest) {
        tableGroupRequest.validateOrderTableSize();

        final List<OrderTable> savedOrderTables = orderService.findAllByIdIn(tableGroupRequest.getOrderTableIds());
        tableGroupRequest.validateEqualOrderTableSize(savedOrderTables.size());

        validateOrderTableEmpty(savedOrderTables);

        return saveTableGroup(savedOrderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = findTableGroupById(tableGroupId);
        final List<OrderTable> orderTables = orderService.findAllByTableGroup(tableGroup);

        orderService.validateOrderStatusNotIn(orderTables, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL));

        tableGroup.ungroup();
        tableGroupRepository.delete(tableGroup);
    }

    public TableGroup findTableGroupById(Long id) {
        return tableGroupRepository.findById(id).orElseThrow(() -> new TableGroupException("테이블 그룹이 존재하지 않습니다", id));
    }

    private TableGroup saveTableGroup(List<OrderTable> savedOrderTables) {
        final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(savedOrderTables));

        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.updateTableGroup(savedTableGroup);
            savedOrderTable.updateEmpty(false);
        }
        savedTableGroup.updateOrderTables(savedOrderTables);
        return savedTableGroup;
    }

    private void validateOrderTableEmpty(List<OrderTable> savedOrderTables) {
        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroup())) {
                throw new OrderTableException("주문 테이블이 비어있지 않습니다");
            }
        }
    }
}
