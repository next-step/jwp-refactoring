package kitchenpos.ordering.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Ordering, Long> {

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatusList);

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatusList);
}
