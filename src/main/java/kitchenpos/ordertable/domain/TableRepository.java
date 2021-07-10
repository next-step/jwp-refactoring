package kitchenpos.ordertable.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TableRepository extends JpaRepository<OrderTable, Long> {
	List<OrderTable> findAllByIdIn(List<Long> ids);
}
