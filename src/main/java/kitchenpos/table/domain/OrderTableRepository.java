package kitchenpos.table.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
    OrderTable save(OrderTable entity);

    Optional<OrderTable> findById(Long id);

    List<OrderTable> findAll();

    List<OrderTable> findAllByIdIn(List<Long> ids);

    @Query(value = "SELECT t FROM OrderTable t WHERE t.tableGroup.id = :tableGroupId")
    List<OrderTable> findAllByTableGroupId(@Param("tableGroupId") Long tableGroupId);
}
