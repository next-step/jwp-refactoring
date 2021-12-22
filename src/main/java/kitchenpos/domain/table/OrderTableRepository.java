package kitchenpos.domain.table;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.vo.TableGroupId;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
    List<OrderTable> findAllByIdIn(List<Long> ids);

    List<OrderTable> findAllByTableGroupId(TableGroupId tableGroupId);

    List<OrderTable> findByIdIn(List<Long> orderTableIds);
}
