package kitchenpos.core.domain;

import java.util.Optional;

public interface TableGroupRepository {
    TableGroup save(TableGroup tableGroup);

    Optional<TableGroup> findById(Long id);

    void deleteById(Long id);
}
