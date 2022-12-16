package kitchenpos.table.application;

import kitchenpos.exception.OrderTableError;
import kitchenpos.exception.TableGroupError;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.*;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

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
        OrderTables orderTables = OrderTables.of(orderTableById(tableGroupRequest.getOrderTables()));
        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        orderTables.group(tableGroup.getId());
        return TableGroupResponse.of(tableGroup, orderTables);
    }

    private List<OrderTable> orderTableById(List<Long> ids) {
        return ids.stream()
                .map(this::orderTableById)
                .collect(Collectors.toList());
    }

    private OrderTable orderTableById(Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(OrderTableError.NOT_FOUND));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new EntityNotFoundException(TableGroupError.NOT_FOUND));
        OrderTables orderTables = OrderTables.of(orderTableRepository.findAllByTableGroupId(tableGroupId));
        List<Order> orders = orderRepository.findAllByOrderTableIdIn(orderTables.getOrderTableIds());
        tableGroup.ungroup(orders);
        orderTables.ungroup();
    }
}
