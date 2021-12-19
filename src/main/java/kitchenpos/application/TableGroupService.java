package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final TableGroupRequest tableGroupRequest) {

        final List<Long> orderTableIds = tableGroupRequest.getOrderTables();
        List<OrderTable> orderTables = new ArrayList<>();

        for (Long orderTableId : orderTableIds) {
            OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                                                            .orElseThrow(() -> new IllegalArgumentException("등록된 주문테이블이 아닙니다."));
            orderTables.add(savedOrderTable);
        }
        return tableGroupRepository.save(new TableGroup(orderTables));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {

        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId).orElseThrow(() -> new IllegalArgumentException("해당 단체지정이 등록되어 있지 않습니다."));
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroup(tableGroup);

        orderTables.stream()
                    .map(OrderTable::getOrders)
                    .forEach(orders -> orders.forEach(Order::checkOrderStatusCookingOrMeal));

        orderTables.forEach(OrderTable::unGroup);
    }
}
