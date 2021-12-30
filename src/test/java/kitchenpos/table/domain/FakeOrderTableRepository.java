package kitchenpos.table.domain;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;

import java.util.*;
import java.util.stream.Collectors;

public class FakeOrderTableRepository implements OrderTableRepository {
    private Map<Long, OrderTable> map = new HashMap<>();
    private Long key = 1L;

    @Override
    public OrderTable save(OrderTable orderTable) {
        if (map.containsKey(orderTable.getId())) {
            map.put(orderTable.getId(), orderTable);
            return orderTable;
        }
        orderTable.createId(key);
        map.put(key, orderTable);
        key++;
        return orderTable;
    }

    @Override
    public Optional<OrderTable> findById(Long id) {
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public List<OrderTable> findAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    public List<OrderTable> findAllByIdIn(List<Long> ids) {
        return map.entrySet().stream()
                .filter(entry -> ids.contains(entry.getKey()))
                .map(entry -> entry.getValue())
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderTable> findAllByTableGroup(Long tableGroupId) {
        return map.values().stream()
                .filter(orderTable -> tableGroupId.equals(orderTable.getTableGroup().getId()))
                .collect(Collectors.toList());
    }
}
