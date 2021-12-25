package kitchenpos.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    Optional<OrderTable> findByOrderTableId(Long orderTableId);

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}
