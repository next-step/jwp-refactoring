package kitchenpos.ordertablegroup.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.ordertablegroup.domain.OrderTableGroup;
import kitchenpos.ordertablegroup.domain.OrderTableGroupRepository;

public interface JpaOrderTableGroupRepository extends OrderTableGroupRepository, JpaRepository<OrderTableGroup, Long> {
}
