package kitchenpos.dao;

import kitchenpos.domain.table.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableGroupDao extends JpaRepository<TableGroup, Long> {
}
