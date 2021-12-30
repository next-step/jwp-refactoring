package kitchenpos.tableGroup.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.tableGroup.domain.TableGroup;
import kitchenpos.tableGroup.domain.TableGroupRepository;
import kitchenpos.tableGroup.dto.TableGroupRequest;
import kitchenpos.tableGroup.dto.TableGroupResponse;
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
            throw new IllegalArgumentException("테이블이 식사중이거나 조리중이면 단체 지정을 해제할 수 없습니다.");
        }

        orderTables.ungroup();
    }
}
