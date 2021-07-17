package kitchenpos.table.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.table.domain.OrderTable;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
    List<OrderTable> findAllByTableGroupId(Long tableGroupId);
	Optional<OrderTable> findByTableGroupId(Long tableGroupId);
}
