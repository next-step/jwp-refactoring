package kitchenpos.ordertable.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.ordertable.domain.OrderTable;

import java.util.List;

public interface OrderTableDao extends JpaRepository<OrderTable, Long> {
}
