package kitchenpos.tablegroup.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.exception.KitchenposNotFoundException;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final TableGroupRepository tableGroupRepository) {
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        request.checkValidSize();
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup());

        final List<Long> orderTableIds = request.getOrderTableIds();
        tableGroup.groupTables(orderTableIds);

        tableGroupRepository.save(tableGroup);
        return TableGroupResponse.from(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
            .orElseThrow(KitchenposNotFoundException::new);

        tableGroup.ungroup();
        tableGroupRepository.save(tableGroup);
    }
}
