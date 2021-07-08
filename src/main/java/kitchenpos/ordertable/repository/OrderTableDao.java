package kitchenpos.ordertable.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.ordertable.domain.OrderTable;

public interface OrderTableDao extends JpaRepository<OrderTable, Long> {
    List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}
