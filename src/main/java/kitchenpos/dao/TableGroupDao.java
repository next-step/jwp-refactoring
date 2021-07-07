package kitchenpos.dao;

import kitchenpos.domain.TableGroup3;

import java.util.List;
import java.util.Optional;

public interface TableGroupDao {
    TableGroup3 save(TableGroup3 entity);

    Optional<TableGroup3> findById(Long id);

    List<TableGroup3> findAll();
}
