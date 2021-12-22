package kitchenpos.application;

import java.security.InvalidParameterException;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderTableRepository;
import kitchenpos.domain.order.TableGroupRepository;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.Orders;
import kitchenpos.domain.order.TableGroup;
import kitchenpos.dto.order.TableGroupRequest;
import kitchenpos.dto.order.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableGroupService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderDao,
        final OrderTableRepository orderTableDao,
        final TableGroupRepository tableGroupDao) {
        this.orderRepository = orderDao;
        this.orderTableRepository = orderTableDao;
        this.tableGroupRepository = tableGroupDao;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroup) {
        final List<OrderTable> orderTables = orderTableRepository.findAllById(
            tableGroup.getOrderTableIds());

        if (tableGroup.getOrderTableSize() != orderTables.size()) {
            throw new InvalidParameterException("단체 지정에 속하는 주문테이블은 모두 등록되어있어야합니다.");
        }

        return TableGroupResponse.of(
            tableGroupRepository.save(tableGroup.toTableGroup(orderTables)));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
            .orElseThrow(InvalidParameterException::new);

        Orders orders = Orders.of(
            orderRepository.findAllByOrderTableIn(tableGroup.getOrderTables()));
        tableGroup.ungroup(orders);
    }
}
