package kichenpos.table.infra;

import kichenpos.table.domain.OrderTable;
import kichenpos.table.domain.OrderTableRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderTableRepository extends JpaRepository<OrderTable, Long>, OrderTableRepository {
}
