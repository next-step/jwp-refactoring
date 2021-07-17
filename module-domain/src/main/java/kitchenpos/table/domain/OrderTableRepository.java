package kitchenpos.table.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
    Optional<OrderTable> findByIdAndEmptyIsFalse(long id);

    Long countByIdIn(List<Long> ids);

    List<OrderTable> findByIdIn(List<Long> ids);

    @Query("select o from OrderTable o where o.tableGroupId = :tableGroupId")
    List<OrderTable> findByTableGroupId(@Param("tableGroupId") long tableGroupId);
}
