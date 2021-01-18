package kitchenpos.ordertable.domain;

import java.util.List;
import kitchenpos.tablegroup.domain.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

	List<OrderTable> findAllByTableGroup(TableGroup tableGroupId);
}
