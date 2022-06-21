package kitchenpos.table.dao;

import static kitchenpos.ServiceTestFactory.createOrderTableBy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;

public class FakeOrderTableDao implements OrderTableDao {
    public static OrderTable ORDER_TABLE = createOrderTableBy(1L, 4, false, 1L);
    public static OrderTable OTHER_ORDER_TABLE = createOrderTableBy(2L, 3, false, null);
    public static OrderTable THIRD_ORDER_TABLE = createOrderTableBy(3L, 2, false, null);
    public static OrderTable EMPTY_ORDER_TABLE = createOrderTableBy(4L, 0, true, null);
    public static OrderTable TEST_GROUP_TABLE = createOrderTableBy(1L, 4, true, null);
    public static OrderTable TEST_GROUP_SECOND_TABLE = createOrderTableBy(2L, 3, true, null);
    public static OrderTable TEST_GROUP_THIRD_TABLE = createOrderTableBy(2L, 3, true, 2L);
    public static OrderTable TEST_GROUP_FOURTH_TABLE = createOrderTableBy(3L, 3, true, 2L);

    @Override
    public OrderTable save(OrderTable entity) {
        return entity;
    }

    @Override
    public Optional<OrderTable> findById(Long id) {
        return Arrays.asList(ORDER_TABLE, OTHER_ORDER_TABLE, THIRD_ORDER_TABLE, EMPTY_ORDER_TABLE).stream()
                .filter(orderTable -> orderTable.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<OrderTable> findAll() {
        return Collections.singletonList(ORDER_TABLE);
    }

    @Override
    public List<OrderTable> findAllByIdIn(List<Long> ids) {
        return Arrays.asList(TEST_GROUP_TABLE, TEST_GROUP_SECOND_TABLE);
    }

    @Override
    public List<OrderTable> findAllByTableGroupId(Long tableGroupId) {
        return Arrays.asList(ORDER_TABLE, TEST_GROUP_THIRD_TABLE, TEST_GROUP_FOURTH_TABLE)
                .stream()
                .filter(orderTable -> orderTable.getTableGroupId().equals(tableGroupId))
                .collect(Collectors.toList());
    }
}
