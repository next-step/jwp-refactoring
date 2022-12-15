package kitchenpos.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    Optional<List<OrderTable>> findAllByIdIn(List<Long> orderTableIds);
}
