package kitchenpos.table.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.table.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    Optional<OrderTable> findByIdAndEmptyIsFalse(Long id);

    List<OrderTable> findAllByIdIn(List<Long> ids);

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}
