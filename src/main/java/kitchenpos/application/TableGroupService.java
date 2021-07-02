package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.dao.TableGroupRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final OrderDao orderDao;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderDao orderDao,
                            final OrderTableRepository orderTableRepository,
                            final TableGroupRepository tableGroupRepository) {
        this.orderDao = orderDao;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final OrderTables orderTables = findOrderTables(tableGroupRequest);
        final TableGroup persistTableGroup = tableGroupRepository.save(new TableGroup(orderTables, LocalDateTime.now()));
        return TableGroupResponse.of(persistTableGroup);
    }

    private OrderTables findOrderTables(final TableGroupRequest tableGroupRequest) {
        List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();
        List<OrderTable> orderTables = orderTableRepository.findAllById(orderTableIds);
        if (orderTables.size() != orderTableIds.size()) {
            throw new IllegalArgumentException("등록이 되지 않은 주문테이블은 그룹화 할 수 없습니다.");
        }
        return OrderTables.of(orderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("조리상태이거나 식사상태인 주문이 있는 주문테이블은 그룹해제를 할 수 없습니다.");
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
            orderTableRepository.save(orderTable);
        }
    }
}
