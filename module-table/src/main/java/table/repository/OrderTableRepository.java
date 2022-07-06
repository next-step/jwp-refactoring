package table.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import table.domain.OrderTable;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    List<OrderTable> findByIdIn(List<Long> ids);

    List<OrderTable> findByTableGroupId(Long tableGroupId);
}
