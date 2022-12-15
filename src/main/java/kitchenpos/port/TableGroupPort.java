package kitchenpos.port;

import kitchenpos.domain.TableGroup;

import java.util.List;
import java.util.Optional;

public interface TableGroupPort {
    TableGroup save(TableGroup entity);

    Optional<TableGroup> findById(Long id);

    List<TableGroup> findAll();
}
