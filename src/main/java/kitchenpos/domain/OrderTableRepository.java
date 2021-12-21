package kitchenpos.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

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


    @Query(value = "select distinct ot from OrderTable ot" +
            " left join fetch ot.tableGroup tg " +
            " left join fetch ot.order o " +
            " where ot.id = ?1")
    Optional<OrderTable> findOneWithOrderByIdJoinFetch(Long id);
}
