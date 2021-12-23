package kitchenpos.tablegroup.application;

import kitchenpos.common.exception.CommonErrorCode;
import kitchenpos.common.exception.InvalidParameterException;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.order.domain.Orders;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableGroupService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderDao,
        final OrderTableRepository orderTableDao, final TableGroupRepository tableGroupDao) {
        this.orderRepository = orderDao;
        this.orderTableRepository = orderTableDao;
        this.tableGroupRepository = tableGroupDao;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroup) {
        final List<OrderTable> orderTables = orderTableRepository.findAllById(
            tableGroup.getOrderTableIds());

        if (tableGroup.getOrderTableSize() != orderTables.size()) {
            throw new InvalidParameterException(CommonErrorCode.TABLE_NOT_CREATED_EXCEPTION);
        }

        return TableGroupResponse.of(
            tableGroupRepository.save(tableGroup.toTableGroup(orderTables)));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
            .orElseThrow(
                () -> new NotFoundException(CommonErrorCode.TABLE_GROUP_NOT_FOUND_EXCEPTION));

        Orders orders = Orders.of(
            orderRepository.findAllByOrderTableIn(tableGroup.getOrderTables()));
        tableGroup.ungroup(orders);
    }
}
