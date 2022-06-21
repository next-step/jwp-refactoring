package kitchenpos.table.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTableEntity, Long> {
    List<OrderTableEntity> findAllByIdIn(List<Long> orderTableIds);
}
