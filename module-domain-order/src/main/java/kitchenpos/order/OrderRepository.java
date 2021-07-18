package kitchenpos.order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Order save(Order entity);

    Optional<Order> findById(Long id);

    List<Order> findAll();

    List<Order> findAllByOrderTableId(Long orderTableId);

    List<Order> findAllByOrderTableIdIn(List<Long> orderTableIds);
}
