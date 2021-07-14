package kitchenpos.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kitchenpos.order.domain.OrderTable;

@Repository
public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

	List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}
