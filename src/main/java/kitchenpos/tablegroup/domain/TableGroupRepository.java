package kitchenpos.tablegroup.domain;

import kitchenpos.ordertable.domain.OrderTable;

import java.util.List;
import java.util.Optional;

public interface TableGroupRepository {
    TableGroup save(TableGroup tableGroup);

    Optional<TableGroup> findById(Long id);

    void deleteById(Long id);
}
