package kitchenposNew.table.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
    Optional<List<OrderTable>> findAllByIdIn(List<Long> orderTableIds);

    Optional<List<OrderTable>> findAllByTableGroupId(Long tableGroupId);
}
