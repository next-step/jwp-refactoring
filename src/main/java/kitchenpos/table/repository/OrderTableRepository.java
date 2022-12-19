package kitchenpos.table.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.table.domain.OrderTable;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {}
