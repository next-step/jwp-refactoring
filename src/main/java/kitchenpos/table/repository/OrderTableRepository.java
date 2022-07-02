package kitchenpos.table.repository;

import kitchenpos.table.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
    List<OrderTable> findByIdIn(List<Long> ids);
}
