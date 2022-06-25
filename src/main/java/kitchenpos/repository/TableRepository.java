package kitchenpos.repository;

import kitchenpos.domain.OrderTableEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableRepository extends JpaRepository<OrderTableEntity, Long> {
}
