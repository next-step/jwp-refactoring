package kitchenpos.domain.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    Orders findByOrderTableId(Long orderTableId);

    List<Orders> findAllByOrderTableIdIn(List<Long> orderTableIds);
}
