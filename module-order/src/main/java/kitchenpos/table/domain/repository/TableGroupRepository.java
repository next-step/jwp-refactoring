package kitchenpos.table.domain.repository;

import java.util.List;
import kitchenpos.table.domain.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {

    boolean existsByOrderTableId(Long orderTableId);

    boolean existsByOrderTableIdIn(List<Long> orderTableIds);

}
