package kitchenpos.table.persistence;

import kitchenpos.table.domain.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {
}
