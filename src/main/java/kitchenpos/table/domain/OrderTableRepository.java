package kitchenpos.table.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
    @Query(value = " SELECT table_group_id FROM order_table "
            + " "+ " WHERE id =:orderTableId"
            , nativeQuery = true)
    Long findTableGroupId(long orderTableId);

}
