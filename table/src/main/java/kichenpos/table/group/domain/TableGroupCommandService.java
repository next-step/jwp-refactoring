package kichenpos.table.group.domain;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableGroupCommandService {

    private final TableGroupRepository tableGroupRepository;

    public TableGroupCommandService(
        TableGroupRepository tableGroupRepository) {
        this.tableGroupRepository = tableGroupRepository;
    }

    public TableGroup save(TableGroup tableGroup) {
        return tableGroupRepository.save(tableGroup);
    }

    public void ungroup(long id) {
        tableGroupRepository.tableGroup(id)
            .ungroup();
    }
}
