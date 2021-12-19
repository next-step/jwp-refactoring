package kichenpos.order.order.domain;

import kichenpos.common.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    default Order order(long id) {
        return findById(id).orElseThrow(
            () -> new NotFoundException(String.format("주문 id(%d)를 찾을 수 없습니다.", id)));
    }
}
