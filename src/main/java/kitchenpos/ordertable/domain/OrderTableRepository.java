package kitchenpos.ordertable.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
    List<OrderTable> findByIdIn(List<Long> orderTableIds);

    List<OrderTable> findByTableGroupId(Long tableGroupId);
}
