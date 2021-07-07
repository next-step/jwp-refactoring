package kitchenpos.table.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TableRepository extends JpaRepository<OrderTableEntity, Long> {
}
