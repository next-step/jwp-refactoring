package kitchenpos.domain.tablegroup.infra;

import kitchenpos.domain.tablegroup.domain.TableGroup;
import kitchenpos.domain.tablegroup.domain.TableGroupRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TableGroupRepositoryImpl implements TableGroupRepository {

    private final JpaTableGroupRepository jpaTableGroupRepository;

    public TableGroupRepositoryImpl(JpaTableGroupRepository jpaTableGroupRepository) {
        this.jpaTableGroupRepository = jpaTableGroupRepository;
    }

    @Override
    public TableGroup save(TableGroup tableGroup) {
        return jpaTableGroupRepository.save(tableGroup);
    }

    @Override
    public Optional<TableGroup> findById(Long id) {
        return jpaTableGroupRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        jpaTableGroupRepository.deleteById(id);
    }
}
