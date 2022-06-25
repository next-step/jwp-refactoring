package kitchenpos.table.application;

import kitchenpos.order.infrastructure.OrderDao;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.infrastructure.OrderTableDao;
import kitchenpos.table.infrastructure.OrderTableRepository;
import kitchenpos.table.infrastructure.TableGroupDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.infrastructure.TableGroupRepository;
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

    public TableGroupService(final OrderDao orderDao, final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
        this.orderDao = orderDao;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<OrderTable> orderTables = tableGroupRequest.getOrderTables().stream()
                .map(OrderTableRequest::toEntity)
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("단체 지정은 최소 2개 테이블 이상이어야 합니다.");
        }

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("주문 테이블이 존재하지 않습니다.");
        }

        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup());

        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.registerGroupTable(tableGroup);
        }

        return TableGroupResponse.of(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        //to-do
//        final List<Long> orderTableIds = orderTables.stream()
//                .map(OrderTable::getId)
//                .collect(Collectors.toList());
//
//        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
//                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
//            throw new IllegalArgumentException();
//        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.unGroupTable();
        }
    }
}
