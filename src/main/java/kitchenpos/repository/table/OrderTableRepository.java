package kitchenpos.repository.table;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    public List<OrderTable> findAllByTableGroup(TableGroup tableGroup);

}
