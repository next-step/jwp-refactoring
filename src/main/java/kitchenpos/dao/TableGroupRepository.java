package kitchenpos.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.TableGroup;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {
}
