package kitchenpos.ordertable.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.ordertable.domain.TableGroup;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {
}
