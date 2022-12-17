package kitchenpos.infrastructure.jpa.adapter;

import kitchenpos.domain.TableGroup;
import kitchenpos.infrastructure.jpa.repository.TableGroupJpaRepository;
import kitchenpos.port.TableGroupPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TableGroupJpaAdapter implements TableGroupPort {

    private final TableGroupJpaRepository tableGroupJpaRepository;

    public TableGroupJpaAdapter(TableGroupJpaRepository tableGroupJpaRepository) {
        this.tableGroupJpaRepository = tableGroupJpaRepository;
    }

    @Override
    public TableGroup save(TableGroup entity) {
        return tableGroupJpaRepository.save(entity);
    }

    @Override
    public TableGroup findById(Long id) {
        return tableGroupJpaRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public List<TableGroup> findAll() {
        return tableGroupJpaRepository.findAll();
    }
}
