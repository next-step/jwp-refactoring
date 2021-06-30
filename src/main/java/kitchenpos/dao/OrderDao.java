package kitchenpos.dao;

import kitchenpos.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderDao extends JpaRepository<Order, Long> {
}
