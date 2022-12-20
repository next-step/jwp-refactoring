package kitchenpos.infrastructure.jpa.adapter;

import kitchenpos.domain.TableGroup;
import kitchenpos.infrastructure.jpa.repository.TableGroupJpaRepository;
import kitchenpos.port.TableGroupPort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static kitchenpos.constants.ErrorCodeType.TABLE_GROUP_NOT_FOUND;

@Service
@Transactional
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
    @Transactional(readOnly = true)
    public TableGroup findById(Long id) {
        return tableGroupJpaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(TABLE_GROUP_NOT_FOUND.getMessage()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TableGroup> findAll() {
        return tableGroupJpaRepository.findAll();
    }
}
