package kitchenpos.application.table;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.order.OrderDao;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.domain.table.TableGroupRepository;
import kitchenpos.dto.table.CreateTableGroupRequest;
import kitchenpos.dto.table.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private final OrderDao orderDao;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderDao orderDao, OrderTableRepository orderTableRepository,
        TableGroupRepository tableGroupRepository) {
        this.orderDao = orderDao;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final CreateTableGroupRequest createTableGroupRequest) {
        List<OrderTable> groupTargetOrderTables = findGroupingTargetOrderTables(createTableGroupRequest);
        TableGroup tableGroup = createTableGroupRequest.toTableGroup(groupTargetOrderTables);
        return TableGroupResponse.from(tableGroupRepository.save(tableGroup));
    }

    public List<OrderTable> findGroupingTargetOrderTables(final CreateTableGroupRequest createTableGroupRequest) {
        return orderTableRepository.findAllByIdIn(createTableGroupRequest.getOrderTables());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());

        // TODO 오류 파악 및 디버깅이 용이하도록 예외 처리 시 오류 문구 설정
        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
            orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        // TODO : 일급 컬렉션 + 변경 감지 + 영속성 전이
        for (final OrderTable orderTable : orderTables) {
            orderTable.deallocateTableGroup();
            orderTableRepository.save(orderTable);
        }
    }
}
