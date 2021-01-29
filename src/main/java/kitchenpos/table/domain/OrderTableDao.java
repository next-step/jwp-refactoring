package kitchenpos.table.domain;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableDao extends JpaRepository<OrderTable, Long> {

	Set<OrderTable> findAllBy();

	Set<OrderTable> findAllByTableGroupId(Long tableGroupId);
}
