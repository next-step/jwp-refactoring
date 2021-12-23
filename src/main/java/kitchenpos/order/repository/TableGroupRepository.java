package kitchenpos.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.order.domain.TableGroup;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {
}
