package table.domain;


import order.domain.FakeOrderRepository;
import order.domain.Order;

import java.util.*;
import java.util.stream.Collectors;

public class FakeOrderTableRepository implements OrderTableRepository {
    private final FakeOrderRepository fakeOrderRepository;
    private Map<Long, OrderTable> map = new HashMap<>();
    private Long key = 1L;

    public FakeOrderTableRepository(FakeOrderRepository fakeOrderRepository) {
        this.fakeOrderRepository = fakeOrderRepository;
    }

    @Override
    public OrderTable save(OrderTable inputOrderTable) {
        if (map.containsKey(inputOrderTable.getId())) {
            map.put(inputOrderTable.getId(), inputOrderTable);
            return inputOrderTable;
        }
        OrderTable orderTable = new OrderTable(key, inputOrderTable.getTableGroup(), inputOrderTable.getNumberOfGuests(), inputOrderTable.isEmpty());
        map.put(key, orderTable);
        key++;
        return orderTable;
    }

    @Override
    public Optional<OrderTable> findById(Long id) {
        List<Order> orders = fakeOrderRepository.findByOrderTableId(id);
        Optional<OrderTable> optionalOrderTable = Optional.ofNullable(map.get(id));
        if (!optionalOrderTable.isPresent()) {
            return Optional.empty();
        }
        OrderTable newOrderTable = OrderTable.of(optionalOrderTable.get(), orders);
        map.put(newOrderTable.getId(), newOrderTable);
        return Optional.ofNullable(newOrderTable);
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
        List<OrderTable> collect = map.values().stream()
                .filter(orderTable -> tableGroupId.equals(orderTable.getTableGroup().getId()))
                .map(orderTable -> findById(orderTable.getId()).get())
                .collect(Collectors.toList());
        return collect;
    }
}
