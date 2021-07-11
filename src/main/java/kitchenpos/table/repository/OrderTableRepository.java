package kitchenpos.table.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kitchenpos.table.domain.OrderTable;

@Repository
public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

	List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}
