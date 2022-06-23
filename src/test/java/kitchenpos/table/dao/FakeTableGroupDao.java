package kitchenpos.table.dao;

import java.util.List;
import java.util.Optional;

import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.infrastructure.TableGroupDao;

public class FakeTableGroupDao implements TableGroupDao {
    @Override
    public TableGroup save(TableGroup entity) {
        return entity;
    }

    @Override
    public Optional<TableGroup> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<TableGroup> findAll() {
        return null;
    }
}
