package kitchenpos.table.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TableRepository extends JpaRepository<OrderTableEntity, Long> {

  List<OrderTableEntity> findAllByIdIn(Iterable<Long> tableIds);
  List<OrderTableEntity> findAllByTableGroupId(Long tableGroupId);
}
