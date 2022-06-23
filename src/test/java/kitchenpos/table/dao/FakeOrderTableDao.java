package kitchenpos.table.dao;

import static kitchenpos.ServiceTestFactory.createOrderTableBy;

import java.util.*;
import java.util.stream.Collectors;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.infrastructure.OrderTableDao;

public class FakeOrderTableDao implements OrderTableDao {
    private final OrderTable firstTable = createOrderTableBy(1L, 4, false, 1L);
    private final OrderTable secondTable = createOrderTableBy(2L, 3, false, null);
    private final OrderTable thirdTable = createOrderTableBy(3L, 2, false, null);
    private final OrderTable emptyTable = createOrderTableBy(4L, 0, true, null);
    private final OrderTable testGroupTable = createOrderTableBy(1L, 4, true, null);
    private final OrderTable testGroupSecondTable = createOrderTableBy(2L, 3, true, null);
    private final OrderTable testGroupThirdTable = createOrderTableBy(3L, 3, true, null);
    private final OrderTable testGroupFourthTable = createOrderTableBy(4L, 3, true, null);
    private final OrderTable testTable = createOrderTableBy(1L, 4, true, 1L);
    private final OrderTable testSecondTable = createOrderTableBy(2L, 3, true, 1L);
    private final OrderTable testThirdTable = createOrderTableBy(3L, 3, true, 2L);
    private final OrderTable testFourthTable = createOrderTableBy(4L, 3, true, 2L);

    @Override
    public OrderTable save(OrderTable entity) {
        return entity;
    }

    @Override
    public Optional<OrderTable> findById(Long id) {
        return Arrays.asList(firstTable, secondTable, thirdTable, emptyTable).stream()
                .filter(orderTable -> orderTable.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<OrderTable> findAll() {
        return Collections.singletonList(firstTable);
    }

    @Override
    public List<OrderTable> findAllByIdIn(List<Long> ids) {
        List<OrderTable> orderTables = new ArrayList<>();
        for (Long id : ids) {
            Optional<OrderTable> table = find(id);
            table.ifPresent(orderTables::add);
        }
        return orderTables;
    }

    @Override
    public List<OrderTable> findAllByTableGroupId(Long tableGroupId) {
        return Arrays.asList(testTable, testSecondTable, testThirdTable, testFourthTable)
                .stream()
                .filter(orderTable -> orderTable.getTableGroupId().equals(tableGroupId))
                .collect(Collectors.toList());
    }

    private Optional<OrderTable> find(Long id) {
        return Arrays.asList(testGroupTable, testGroupSecondTable, testGroupThirdTable, testGroupFourthTable).stream()
                .filter(orderTable -> orderTable.getId().equals(id))
                .findFirst();
    }
}
