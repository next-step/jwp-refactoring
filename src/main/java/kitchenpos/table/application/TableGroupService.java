package kitchenpos.table.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.persistence.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.persistence.OrderTableRepository;
import kitchenpos.table.persistence.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderRepository orderRepository;


    public TableGroupService(final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository,
                             final OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<OrderTable> orderTables = findAllOrderTableByIds(tableGroupRequest.getOrderTableIds());
        return TableGroupResponse.of(tableGroupRepository.save(tableGroupRequest.toTableGroup(orderTables)));
    }

    private List<OrderTable> findAllOrderTableByIds(List<Long> ids) {
        List<OrderTable> orderTables = orderTableRepository.findAllById(ids);
        if (ids.size() != orderTables.size()) {
            throw new IllegalArgumentException();
        }
        return orderTables;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId).orElseThrow(IllegalArgumentException::new);
        List<Order> orders = orderRepository.findAllByOrderTableIn(tableGroup.getOrderTables());
        tableGroup.ungroup(orders);
    }
}
