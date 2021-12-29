package kitchenpos.table.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * packageName : kitchenpos.domain
 * fileName : OrderTableRepository
 * author : haedoang
 * date : 2021-12-21
 * description :
 */
public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    List<OrderTable> findAllByTableGroupId(Long id);
}
