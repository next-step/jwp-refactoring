package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupValidator tableGroupValidator;

    public TableGroupService(
        final OrderRepository orderRepository,
        final OrderTableRepository orderTableRepository,
        final TableGroupRepository tableGroupRepository,
        final TableGroupValidator tableGroupValidator
    ) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupValidator = tableGroupValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();
        final OrderTables savedOrderTables = new OrderTables(orderTableRepository.findAllByIdIn(orderTableIds));
        tableGroupValidator.validateCreate(orderTableIds, savedOrderTables);

        final TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.generate());
        savedOrderTables.group(savedTableGroup.getId());

        return TableGroupResponse.of(savedTableGroup, savedOrderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final OrderTables orderTables = new OrderTables(orderTableRepository.findAllByTableGroupId(tableGroupId));

        orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTables.toIds(), OrderStatus.cantUngroup());
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTables.toIds(), OrderStatus.cantUngroup())) {
            throw new IllegalArgumentException("변경할 수 없는 주문 상태입니다.");
        }
        orderTables.ungroup();
    }
}
