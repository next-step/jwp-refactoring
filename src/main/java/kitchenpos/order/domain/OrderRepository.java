package kitchenpos.order.domain;

import kitchenpos.common.exception.NotFoundException;
import kitchenpos.table.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    default Order order(long id) {
        return findById(id).orElseThrow(
            () -> new NotFoundException(String.format("주문 id(%d)를 찾을 수 없습니다.", id)));
    }
}
