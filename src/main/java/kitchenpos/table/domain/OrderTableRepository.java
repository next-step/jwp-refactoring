package kitchenpos.table.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
    int countByIdIn(List<Long> orderTableIds);
    List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}
