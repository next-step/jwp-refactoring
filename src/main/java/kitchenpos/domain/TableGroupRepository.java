package kitchenpos.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.TableGroup;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {
}
