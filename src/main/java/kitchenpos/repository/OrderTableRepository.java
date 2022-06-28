package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
    public List<OrderTable> findAllByIdIn(List<Long> ids);

    public List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}
