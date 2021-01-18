package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.TableGroupCreateRequest;
import kitchenpos.dto.TableGroupDto;
import kitchenpos.repository.OrderDao;
import kitchenpos.repository.OrderTableDao;
import kitchenpos.repository.TableGroupDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(final OrderDao orderDao, final OrderTableDao orderTableDao, final TableGroupDao tableGroupDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroupDto create(final TableGroupCreateRequest tableGroup) {

        final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(tableGroup.getOrderTableIds());

        validate(tableGroup, savedOrderTables);

        final TableGroup savedTableGroup = tableGroupDao.save(tableGroup.toEntity());

        final Long tableGroupId = savedTableGroup.getId();

        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.groupBy(tableGroupId);
            orderTableDao.save(savedOrderTable);
        }

        return TableGroupDto.of(savedTableGroup, savedOrderTables);
    }

    private void validate(TableGroupCreateRequest tableGroup, List<OrderTable> savedOrderTables) {
        final List<Long> orderTableIds = tableGroup.getOrderTableIds();

        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < 2) {
            throw new IllegalArgumentException("단체 지정에 주문 테이블은 2개 이상이여야 합니다.");
        }

        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("단체 지정에 중복되는 주문 테이블이 존재합니다.");
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("단체 지정 해제시 주문 테이블중 하나라도 조리, 식사 상태인 경우에는 해재 불가 합니다.");
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
            orderTableDao.save(orderTable);
        }
    }
}
