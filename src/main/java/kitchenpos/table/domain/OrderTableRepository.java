package kitchenpos.table.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
    List<OrderTable> findAllByTableGroupId(Long tableGroupId);
	Optional<OrderTable> findByTableGroupId(Long tableGroupId);

	List<OrderTable> findAllByIdIn(List<Long> ids);

}
