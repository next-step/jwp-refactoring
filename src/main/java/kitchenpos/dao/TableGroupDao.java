package kitchenpos.dao;

import kitchenpos.domain.TableGroupRequest;

import java.util.List;
import java.util.Optional;

public interface TableGroupDao {
    TableGroupRequest save(TableGroupRequest entity);

    Optional<TableGroupRequest> findById(Long id);

    List<TableGroupRequest> findAll();
}
