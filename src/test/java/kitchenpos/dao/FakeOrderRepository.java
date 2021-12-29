package kitchenpos.dao;

import kitchenpos.domain.Order;

import java.util.*;

public class FakeOrderRepository implements OrderRepository {
    private Map<Long, Order> map = new HashMap<>();
    private Long key = 1L;

    @Override
    public Order save(Order order) {
        if (map.containsKey(order.getId())) {
            map.put(order.getId(), order);
            return order;
        }

        order.createId(key);
        map.put(key, order);
        key++;
        return order;
    }

    @Override
    public Optional<Order> findById(Long id) {
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    public boolean existsByOrderTableAndOrderStatusIn(Long orderTableId, List<String> orderStatuses) {
        return map.values().stream()
                .filter(order -> order.getOrderTable().equals(orderTableId))
                .anyMatch(order -> orderStatuses.contains(order.getOrderStatus()));
    }

    @Override
    public boolean existsByOrderTableInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatuses) {
        return map.values().stream()
                .filter(order -> orderTableIds.contains(order.getOrderTable()))
                .anyMatch(order -> orderStatuses.contains(order.getOrderStatus()));
    }
}
