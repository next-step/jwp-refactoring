package kitchenpos.table.repository;

import java.util.List;
import kitchenpos.table.domain.OrderTableV2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderTableRepository extends JpaRepository<OrderTableV2, Long> {
    List<OrderTableV2> findAllByTableGroupId(Long tableGroupId);
}
