package kitchenpos.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.Order;

public interface OrderDao extends JpaRepository<Order, Long> {
    boolean existsByOrderTable_IdAndOrderStatusIn(Long orderTableId, List<String> orderStatuses);
    boolean existsByOrderTable_IdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatuses);
}
