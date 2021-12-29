package kitchenpos.dao;

import kitchenpos.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends Repository<Order, Long> {
    Order save(Order entity);

    Optional<Order> findById(Long id);

    List<Order> findAll();

    boolean existsByOrderTableAndOrderStatusIn(Long orderTable, List<String> orderStatuses);

    boolean existsByOrderTableInAndOrderStatusIn(List<Long> orderTables, List<String> orderStatuses);
}
