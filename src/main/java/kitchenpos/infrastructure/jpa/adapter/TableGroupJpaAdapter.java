package kitchenpos.infrastructure.jpa.adapter;

import kitchenpos.domain.TableGroup;
import kitchenpos.port.TableGroupPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TableGroupJpaAdapter implements TableGroupPort {
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
