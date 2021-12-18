package kitchenpos.application.table;

import kitchenpos.application.order.OrderService;
import kitchenpos.domain.order.Orders;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.table.OrderTables;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.domain.table.TableGroupRepository;
import kitchenpos.dto.table.OrderTableDto;
import kitchenpos.dto.table.TableGroupDto;
import kitchenpos.exception.table.NotRegistedMenuOrderTableException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final OrderService orderService;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
        final OrderService orderService, 
        final OrderTableRepository orderTableRepository, 
        final TableGroupRepository tableGroupRepository
    ) {
        this.orderService = orderService;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupDto create(final TableGroupDto tableGroup) {
        final List<Long> orderTableIds = tableGroup.getOrderTables().stream()
                                                    .map(OrderTableDto::getId)
                                                    .collect(Collectors.toList());

        final OrderTables savedOrderTables = OrderTables.of(orderTableRepository.findAllByIdIn(orderTableIds));

        checkAllExistOfOrderTables(tableGroup.getOrderTables(), savedOrderTables);

        return TableGroupDto.of(tableGroupRepository.save(TableGroup.of(savedOrderTables)));
    }

    private void checkAllExistOfOrderTables(final List<OrderTableDto> orderTables, final OrderTables savedOrderTables) {
        if (orderTables.size() != savedOrderTables.size()) {
            throw new NotRegistedMenuOrderTableException();
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final OrderTables orderTables = OrderTables.of(orderTableRepository.findAllByTableGroupId(tableGroupId));
        final List<Orders> order = orderService.findAllByOrderTableIdIn(orderTables.getOrderTableIds());

        for (int index = 0; index < order.size(); index++) {
            order.get(index).getOrderTable().unGroupTable(order.get(index));
        }
    }
}
