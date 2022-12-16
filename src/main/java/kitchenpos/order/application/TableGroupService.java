package kitchenpos.order.application;

import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.repository.OrderTableRepository;
import kitchenpos.order.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final OrderDao orderDao;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderDao orderDao, final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
        this.orderDao = orderDao;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final TableGroup tableGroup) {
//        final List<OrderTable> orderTables = tableGroup.getOrderTables();
//
//        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
//            throw new IllegalArgumentException();
//        }
//
//        final List<Long> orderTableIds = orderTables.stream()
//                .map(OrderTable::getId)
//                .collect(Collectors.toList());
//
//        final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);
//
//        if (orderTables.size() != savedOrderTables.size()) {
//            throw new IllegalArgumentException();
//        }
//
//        for (final OrderTable savedOrderTable : savedOrderTables) {
//            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) {
//                throw new IllegalArgumentException();
//            }
//        }
//
//        tableGroup.setCreatedDate(LocalDateTime.now());
//
//        final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);
//
//        final Long tableGroupId = savedTableGroup.getId();
//        for (final OrderTable savedOrderTable : savedOrderTables) {
//            savedOrderTable.setTableGroupId(tableGroupId);
//            savedOrderTable.setEmpty(false);
//            orderTableDao.save(savedOrderTable);
//        }
//        savedTableGroup.setOrderTables(savedOrderTables);
//
//        return savedTableGroup;
        return null;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        // TODO : ungroup 도메인으로 로직 이전
//        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);
//
//        final List<Long> orderTableIds = orderTables.stream()
//                .map(OrderTable::getId)
//                .collect(Collectors.toList());
//
//        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
//                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
//            throw new IllegalArgumentException();
//        }
//
//        for (final OrderTable orderTable : orderTables) {
//            orderTable.setTableGroupId(null);
//            orderTableDao.save(orderTable);
//        }
    }
}
