package kitchenpos.ordertable.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;

@Repository(value = "ToBeJpaOrderTableRepository")
public interface JpaOrderTableRepository extends OrderTableRepository, JpaRepository<OrderTable, Long> {
}
