package kitchenpos.infra.ordertable;

import kitchenpos.core.ordertable.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaOrderTableRepository extends JpaRepository<OrderTable, Long> {

    @Query("select o from OrderTable o where o.id in (:ids)")
    List<OrderTable> findAllByIdIn(@Param("ids") List<Long> orderTableIds);

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);

}
