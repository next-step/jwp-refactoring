package kitchenpos.tablegroup.port;

import kitchenpos.tablegroup.domain.TableGroup;

import java.util.List;

public interface TableGroupPort {
    TableGroup save(TableGroup entity);

    TableGroup findById(Long id);

    List<TableGroup> findAll();
}
