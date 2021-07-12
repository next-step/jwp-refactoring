package kitchenpos.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
    List<OrderTable> findAllByTableGroupId(TableGroup tableGroup);
    List<OrderTable> findAllByTableGroupId(Long tableGroupId);

    List<OrderTable> findAllByIdIn(List<Long> orderTableIds);
}
