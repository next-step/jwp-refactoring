package kitchenpos.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderTableRepository extends JpaRepository<OrderTableEntity, Long> {
    List<OrderTableEntity> findAllByIdIn(List<Long> ids);

    List<OrderTableEntity> findAllByTableGroupId(Long tableGroupId);
}
