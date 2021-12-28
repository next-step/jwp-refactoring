package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    List<OrderTable> findAllByIdIn(List<Long> orderTableIds);

    List<OrderTable> findByTableGroupId(Long tableGroupId);
}
