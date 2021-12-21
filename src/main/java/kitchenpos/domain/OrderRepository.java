package kitchenpos.domain;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * packageName : kitchenpos.domain
 * fileName : OrderRepository
 * author : haedoang
 * date : 2021-12-21
 * description :
 */
public interface OrderRepository extends JpaRepository<Order, Long> {
}
