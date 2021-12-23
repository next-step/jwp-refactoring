package kitchenpos.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderTableIdAndOrderStatusIn(Long id, List<OrderStatus> status);

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> ids, List<OrderStatus> status);

}
