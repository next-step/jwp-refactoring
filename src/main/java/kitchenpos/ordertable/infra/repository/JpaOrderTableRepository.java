package kitchenpos.ordertable.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;

public interface JpaOrderTableRepository extends OrderTableRepository, JpaRepository<OrderTable, Long> {
}
