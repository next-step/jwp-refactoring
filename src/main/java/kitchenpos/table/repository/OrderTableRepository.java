package kitchenpos.table.repository;

import kitchenpos.table.domain.OrderTableV2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderTableRepository extends JpaRepository<OrderTableV2, Long> {
}
