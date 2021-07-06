package kitchenpos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kitchenpos.domain.OrderTable;

@Repository
public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
}
