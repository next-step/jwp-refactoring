package kitchenpos.dao;

import kitchenpos.tablegroup.domain.TableGroup;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public class TableGroupDtoImpl implements TableGroupDao {

    @Override
    public TableGroup save(TableGroup entity) {
        return null;
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
