package table.domain;

import java.util.*;

public class FakeTableGroupRepository implements TableGroupRepository {
    private FakeOrderTableRepository fakeOrderTableRepository;
    private Map<Long, TableGroup> map = new HashMap<>();
    private Long key = 1L;

    public FakeTableGroupRepository(FakeOrderTableRepository fakeOrderTableRepository) {
        this.fakeOrderTableRepository = fakeOrderTableRepository;
    }

    @Override
    public TableGroup save(TableGroup inputTableGroup) {
        OrderTables newOrderTables = initOrderTables(inputTableGroup);

        for (OrderTable orderTable : newOrderTables.getOrderTables()) {
            fakeOrderTableRepository.save(orderTable);
        }

        TableGroup savedTableGroup = new TableGroup(key, inputTableGroup.getCreatedDate(), newOrderTables);
        map.put(key, savedTableGroup);
        key++;
        return savedTableGroup;
    }

    private OrderTables initOrderTables(TableGroup inputTableGroup) {
        OrderTables orderTables = inputTableGroup.getOrderTables();
        List<OrderTable> initOrderTableList = new ArrayList<>();
        for (OrderTable orderTable : orderTables.getOrderTables()) {
            OrderTable orderTable1 = new OrderTable(orderTable.getId(), null, orderTable.getNumberOfGuests(), true);
            initOrderTableList.add(orderTable1);
        }
        OrderTables newOrderTables = new OrderTables(initOrderTableList);
        TableGroup tableGroup = new TableGroup(key, inputTableGroup.getCreatedDate(), inputTableGroup.getOrderTables());
        newOrderTables.assignTable(tableGroup);
        return newOrderTables;
    }

    @Override
    public Optional<TableGroup> findById(Long id) {
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public List<TableGroup> findAll() {
        return new ArrayList<>(map.values());
    }
}
