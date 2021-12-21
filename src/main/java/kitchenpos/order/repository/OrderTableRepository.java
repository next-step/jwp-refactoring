package kitchenpos.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.order.domain.OrderTable;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
    List<OrderTable> findAllByIdIn(List<Long> orderTableIds);
}
