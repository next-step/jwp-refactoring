package kitchenpos.table.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.common.exception.NotFoundException;
import kitchenpos.order.domain.OrderTable;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
	default OrderTable orderTable(long id) {
		return findById(id).orElseThrow(
			() -> new NotFoundException(String.format("주문 테이블 id(%d)를 찾을 수 없습니다.", id)));
	}
}
