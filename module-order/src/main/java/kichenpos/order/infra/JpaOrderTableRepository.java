package kichenpos.order.infra;

import kichenpos.order.domain.OrderTable;
import kichenpos.order.domain.OrderTableRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderTableRepository extends JpaRepository<OrderTable, Long>, OrderTableRepository {
}
