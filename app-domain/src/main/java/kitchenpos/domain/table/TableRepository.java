package kitchenpos.domain.table;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TableRepository extends JpaRepository<OrderTable, Long> {
    List<OrderTable> findByIdIn(Collection<Long> ids);
    List<OrderTable> findByOrderTableGroup(OrderTableGroup orderTableGroup);
}
