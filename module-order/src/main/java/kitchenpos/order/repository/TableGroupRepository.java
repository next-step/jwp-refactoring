package kitchenpos.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kitchenpos.order.domain.TableGroup;

@Repository
public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {
}
