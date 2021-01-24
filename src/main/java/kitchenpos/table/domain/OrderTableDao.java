package kitchenpos.table.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableDao extends JpaRepository<OrderTable, Long> {
	List<OrderTable> findAllByIdIn(List<Long> ids);

	List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}
