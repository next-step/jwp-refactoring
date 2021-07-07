package kitchenpos.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByOrderTableAndOrderStatusIn(long anyLong, List<String> anyList);

    boolean existsByOrderTableInAndOrderStatusIn(List<Long> orderTableIds, List<String> asList);
}
