package kitchenpos.table.domain;

import kitchenpos.common.exception.NotFoundOrderTableException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
    List<OrderTable> findAllByIdIn(final List<Long> orderTableIds);

    default OrderTable findByIdElseThrow(final Long id) {
        return this.findById(id)
                .orElseThrow(NotFoundOrderTableException::new);
    }

    List<OrderTable> findAllByTableGroupId(final Long tableGroupId);

    boolean existsByIdAndEmptyTrue(Long id);
}
