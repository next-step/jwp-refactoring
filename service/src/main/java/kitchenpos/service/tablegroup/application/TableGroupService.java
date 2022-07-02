package kitchenpos.service.tablegroup.application;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrdersRepository;
import kitchenpos.table.domain.OrderTable;
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
import java.util.stream.Collectors;

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
        TableGroup tableGroup = new TableGroup(savedOrderTables);
        tableGroupRepository.save(tableGroup);
        return new TableGroupResponse(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId).orElseThrow(NoSuchElementException::new);

        if (ordersRepository.existsByOrderTableIdInAndOrderStatusIn(tableGroup.getOrderTables().stream().map(
                OrderTable::getId).collect(Collectors.toList()), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("계산완료 상태가 아닌 테이블이 포함되어 있습니다.");
        }

        tableGroup.unGroup();
    }
}
