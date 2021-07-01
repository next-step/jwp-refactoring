package kitchenpos.dao;

import kitchenpos.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderTableDao extends JpaRepository<OrderTable, Long> {
}
