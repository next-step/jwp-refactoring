package kitchenpos.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.orders.domain.Orders;

public interface OrderDao extends JpaRepository<Orders, Long> {
    Orders save(Orders entity);

    Optional<Orders> findById(Long id);

    List<Orders> findAll();

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatuses);

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatuses);
}
