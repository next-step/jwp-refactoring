package kitchenpos.tablegroup.infrastructure.adapter;

import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.infrastructure.respository.TableGroupJpaRepository;
import kitchenpos.tablegroup.port.TableGroupPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kitchenpos.tablegroup.exceptions.TableGroupErrorCode.TABLE_GROUP_NOT_FOUND;

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
