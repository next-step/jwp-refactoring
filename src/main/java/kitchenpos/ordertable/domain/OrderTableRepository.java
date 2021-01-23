package kitchenpos.ordertable.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.tablegroup.domain.TableGroup;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
	List<OrderTable> findAllByTableGroup(TableGroup tableGroup);
}
