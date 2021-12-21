package kitchenpos.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * packageName : kitchenpos.domain
 * fileName : OrderTableRepository
 * author : haedoang
 * date : 2021-12-21
 * description :
 */
public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
    @Query(value = "select distinct ot from OrderTable ot" +
            " left join fetch ot.tableGroup tg")
    List<OrderTable> findAllJoinFetch();
}
