package kitchenpos.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableGroupDao extends JpaRepository<TableGroup, Long> {
}
