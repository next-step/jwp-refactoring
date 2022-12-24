package kitchenpos.table.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<OrderTable> orderTables = orderTableRepository.findAllById(tableGroupRequest.getOrderTables());
        if (tableGroupRequest.getOrderTables().size() != orderTables.size()) {
            throw new EntityNotFoundException();
        }
        return TableGroupResponse.of(tableGroupRepository.save(TableGroupRequest.toTableGroup(orderTables)));
    }

    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new EntityNotFoundException());
        List<Order> orders = orderRepository.findAllByOrderTableIdIn(tableGroup.getOrderTableIds());

        tableGroup.ungroup(orders);

        tableGroupRepository.save(tableGroup);
    }
}
