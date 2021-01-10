package kitchenpos.application;

import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.orderTable.OrderTable;
import kitchenpos.domain.tableGroup.*;
import kitchenpos.domain.orderTable.OrderTableRepository;
import kitchenpos.ui.dto.tableGroup.OrderTableInTableGroupRequest;
import kitchenpos.ui.dto.tableGroup.TableGroupRequest;
import kitchenpos.ui.dto.tableGroup.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final SafeOrderTableInTableGroup safeOrderTableInTableGroup;

    public TableGroupService(
            final OrderRepository orderRepository, final OrderTableRepository orderTableRepository,
            final TableGroupRepository tableGroupRepository, final SafeOrderTableInTableGroup safeOrderTableInTableGroup
    ) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.safeOrderTableInTableGroup = safeOrderTableInTableGroup;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<OrderTableInTableGroupRequest> orderTables = tableGroupRequest.getOrderTables();

        List<OrderTableInTableGroup> orderTablesInTableGroup = orderTables.stream()
                .map(it -> new OrderTableInTableGroup(it.getId()))
                .collect(Collectors.toList());
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTablesInTableGroup);
        TableGroup saved = tableGroupRepository.save(tableGroup);

        // TODO: 여기서 주문 테이블 정보 불러와서 보내도록 변경 필요
        return TableGroupResponse.of(saved);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
//        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
//
//        final List<Long> orderTableIds = orderTables.stream()
//                .map(OrderTable::getId)
//                .collect(Collectors.toList());
//
//        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
//                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
//            throw new IllegalArgumentException();
//        }
//
//        for (final OrderTable orderTable : orderTables) {
//            orderTable.ungroup();
//            orderTableRepository.save(orderTable);
//        }
    }
}
