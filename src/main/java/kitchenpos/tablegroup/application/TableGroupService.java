package kitchenpos.tablegroup.application;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrdersRepository;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.NoSuchElementException;

@Service
public class TableGroupService {
    private final OrdersRepository ordersRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
            final OrdersRepository ordersRepository, final OrderTableRepository orderTableRepository,
            final TableGroupRepository tableGroupRepository) {
        this.ordersRepository = ordersRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        final OrderTables savedOrderTables = new OrderTables(request.getOrderTableIds().size(),
                orderTableRepository.findAllByIdIn(request.getOrderTableIds()));
        TableGroup tableGroup = TableGroup.group(savedOrderTables);
        tableGroupRepository.save(tableGroup);
        return new TableGroupResponse(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId).orElseThrow(NoSuchElementException::new);

        if (ordersRepository.existsByOrderTableInAndOrderStatusIn(tableGroup.getOrderTables(),
                Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }

        tableGroup.unGroup();
    }
}
