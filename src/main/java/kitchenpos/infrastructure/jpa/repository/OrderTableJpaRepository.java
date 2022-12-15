package kitchenpos.infrastructure.jpa.repository;

import kitchenpos.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderTableJpaRepository extends JpaRepository<OrderTable, Long> {
}
