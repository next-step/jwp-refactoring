package kitchenpos.order.application;

import kitchenpos.exception.OrderTableError;
import kitchenpos.exception.TableGroupError;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

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
        List<OrderTable> orderTables = orderTableRepository.findAllById(tableGroupRequest.getOrderTables());
        if (tableGroupRequest.getOrderTables().size() != orderTables.size()) {
            throw new EntityNotFoundException(OrderTableError.NOT_FOUND);
        }
        return TableGroupResponse.of(tableGroupRepository.save(TableGroupRequest.toTableGroup(orderTables)));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new EntityNotFoundException(TableGroupError.NOT_FOUND));
        List<Order> orders = orderRepository.findAllByOrderTableIdIn(tableGroup.getOrderTableIds());

        tableGroup.ungroup(orders);

        tableGroupRepository.save(tableGroup);
    }
}
