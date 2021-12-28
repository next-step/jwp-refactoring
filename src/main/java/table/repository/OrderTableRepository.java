package table.repository;

import org.springframework.data.jpa.repository.*;

import table.domain.*;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
}