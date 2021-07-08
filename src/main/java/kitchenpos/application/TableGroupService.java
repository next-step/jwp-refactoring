package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.OrderTableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final OrderDao orderDao;
    private final OrderTableRepository orderTableRepository;
    private final OrderTableGroupRepository orderTableGroupRepository;

    public TableGroupService(final OrderDao orderDao, final OrderTableRepository orderTableRepository, final OrderTableGroupRepository orderTableGroupRepository) {
        this.orderDao = orderDao;
        this.orderTableRepository = orderTableRepository;
        this.orderTableGroupRepository = orderTableGroupRepository;
    }

    @Transactional
    public TableGroup create(final TableGroup tableGroup) {
        final List<OrderTable> orderTables = tableGroup.getOrderTables();

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("주문 테이블들은 사전에 등록되어 있어야 한다.");
        }

        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroup())) {
                throw new IllegalArgumentException("테이블이 비어있지 않습니다.");
            }
        }

        final TableGroup savedTableGroup = orderTableGroupRepository.save(tableGroup);
        savedTableGroup.changeEmpty(false);

        return savedTableGroup;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException(String.format("%s 또는 %s 상태일때는 삭제할수 없습니다.", OrderStatus.COOKING, OrderStatus.MEAL));
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.clearTableGroup();
            orderTableRepository.save(orderTable);
        }
    }
}
