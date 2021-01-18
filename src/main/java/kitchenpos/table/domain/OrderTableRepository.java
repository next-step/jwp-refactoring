package kitchenpos.table.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
    List<OrderTable> findAllByIdIn(@Param("ids") List<Long> ids);

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}
