package kitchenpos.order.repository;

import kitchenpos.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> asList);

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> asList);
}
