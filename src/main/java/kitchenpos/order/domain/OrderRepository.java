package kitchenpos.order.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByOrderTable_Id(Long orderTableId);

    List<Order> findAllByOrderTable_IdIn(List<Long> orderTableIds);
}
