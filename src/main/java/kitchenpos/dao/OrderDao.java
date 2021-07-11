package kitchenpos.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.Order;

public interface OrderDao extends JpaRepository<Order, Long> {
    Order save(Order entity);

    Optional<Order> findById(Long id);

    List<Order> findAll();

    List<Order> findAllByOrderTable_Id(Long orderTableId);

    List<Order> findAllByOrderTable_IdIn(List<Long> orderTableIds);
}
