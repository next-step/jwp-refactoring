package kitchenpos.table.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.NoSuchElementException;

public interface TableRepository extends JpaRepository<OrderTable, Long> {
    default OrderTable getById(Long id) {
        return findById(id).orElseThrow(() -> new NoSuchElementException("주문 테이블을 찾을 수 없습니다. id: " + id));
    }
}
