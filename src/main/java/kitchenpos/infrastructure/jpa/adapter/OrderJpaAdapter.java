package kitchenpos.infrastructure.jpa.adapter;

import kitchenpos.domain.Order;
import kitchenpos.infrastructure.jpa.repository.OrderJpaRepository;
import kitchenpos.port.OrderPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OrderJpaAdapter implements OrderPort {

    private final OrderJpaRepository orderJpaRepository;

    public OrderJpaAdapter(OrderJpaRepository orderJpaRepository) {
        this.orderJpaRepository = orderJpaRepository;
    }

    @Override
    public Order save(Order entity) {
        return orderJpaRepository.save(entity);
    }

    @Override
    public Order findById(Long id) {
        return orderJpaRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public List<Order> findAll() {
        return orderJpaRepository.findAll();
    }

    @Override
    public List<Order> findAllByOrderTableIdIn(List<Long> orderTablesId) {
        return orderJpaRepository.findAllByOrderTableIdIn(orderTablesId);
    }
}
