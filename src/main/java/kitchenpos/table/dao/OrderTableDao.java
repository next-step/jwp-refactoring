package kitchenpos.table.dao;

import kitchenpos.table.domain.OrderTable;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableDao extends JpaRepository<OrderTable, Long> {

    List<OrderTable> findAllByIdIn(List<Long> ids);
}