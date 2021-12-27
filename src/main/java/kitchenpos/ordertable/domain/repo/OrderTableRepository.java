package kitchenpos.ordertable.domain.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.ordertable.domain.domain.OrderTable;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
	List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}
