package kitchenpos.table.domain;

import kitchenpos.common.exception.NotFoundOrderTableException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
    List<OrderTable> findAllByIdIn(List<Long> orderTableIds);

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);

    default OrderTable findByIdElseThrow(Long id){
        return this.findById(id)
                .orElseThrow(NotFoundOrderTableException::new);
    }
}
