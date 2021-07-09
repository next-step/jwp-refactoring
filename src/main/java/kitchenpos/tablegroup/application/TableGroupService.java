package kitchenpos.tablegroup.application;

import static java.time.LocalDateTime.*;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderTableId;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroupId;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.UngroupValidator;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
        final OrderRepository orderRepository,
        final OrderTableRepository orderTableRepository,
        final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<OrderTable> orderTables = findOrderTables(tableGroupRequest);
        final TableGroup persistTableGroup = tableGroupRepository.save(new TableGroup(now()));
        persistTableGroup.group(orderTables);
        return TableGroupResponse.of(persistTableGroup, orderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
            .orElseThrow(() -> new IllegalArgumentException("등록된 테이블 그룹만 그룹해제 가능합니다."));
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(new TableGroupId(tableGroupId));
        final List<Order> orders = orderRepository.findAllByOrderTableIdIn(extractIds(orderTables));
        final UngroupValidator ungroupValidator = new UngroupValidator(orders);
        tableGroup.ungroup(orderTables, ungroupValidator);
    }

    private List<OrderTable> findOrderTables(final TableGroupRequest tableGroupRequest) {
        List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();
        List<OrderTable> orderTables = orderTableRepository.findAllById(orderTableIds);
        if (orderTables.size() != orderTableIds.size()) {
            throw new IllegalArgumentException("등록이 되지 않은 주문테이블은 그룹화 할 수 없습니다.");
        }
        return orderTables;
    }

    private List<OrderTableId> extractIds(List<OrderTable> orderTables) {
        return orderTables.stream()
            .map(OrderTable::getId)
            .map(OrderTableId::new)
            .collect(Collectors.toList());
    }
}
