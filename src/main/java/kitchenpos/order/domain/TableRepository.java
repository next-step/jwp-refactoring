package kitchenpos.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TableRepository extends JpaRepository<OrderTable, Long> {

    List<OrderTable> findByTableGroupId(Long tableGroupId);

}
