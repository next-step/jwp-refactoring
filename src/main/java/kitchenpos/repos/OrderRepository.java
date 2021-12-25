package kitchenpos.repos;

import kitchenpos.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean findByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> asList);

    boolean findByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> asList);
}
