package kitchenpos.ordertablegroup.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kitchenpos.ordertablegroup.domain.OrderTableGroup;
import kitchenpos.ordertablegroup.domain.OrderTableGroupRepository;

@Repository(value = "ToBeJpaOrderTableGroupRepository")
public interface JpaOrderTableGroupRepository extends OrderTableGroupRepository, JpaRepository<OrderTableGroup, Long> {
}
