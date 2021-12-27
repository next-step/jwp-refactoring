package kitchenpos.table.domain;

import kitchenpos.common.exception.NotFoundOrderTableException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
    List<OrderTable> findAllByIdIn(List<Long> orderTableIds);

    default OrderTable findByIdElseThrow(Long id) {
        return this.findById(id)
                .orElseThrow(NotFoundOrderTableException::new);
    }

    boolean existsByIdAndEmptyTrue(Long id);
}
