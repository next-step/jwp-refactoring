package kitchenpos.ordertable.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long>, OrderTableDao {

    @Override
    List<OrderTable> findAllByIdIn(List<Long> ids);

    @Override
    List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}
