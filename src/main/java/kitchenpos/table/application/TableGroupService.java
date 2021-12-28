package kitchenpos.table.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final OrderTables orderTables = OrderTables.of(tableGroupRequest.orderTablesSize(), orderTableRepository.findAllById(tableGroupRequest.toOrderTableIds()));

        final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup());
        orderTables.group(savedTableGroup.getId());

        return TableGroupResponse.of(savedTableGroup, OrderTableResponse.of(orderTables));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("그룹 정보가 존재하지 않습니다."));

        final OrderTables orderTables = new OrderTables(orderTableRepository.findAllByTableGroupId(tableGroupId));

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTables.getOrderTableIds(), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }

        orderTables.ungroup();
    }
}
