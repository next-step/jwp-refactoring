package kitchenpos.api.application.tablegroup;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.domain.tablegroup.TableGroup;
import kitchenpos.common.domain.tablegroup.TableGroupRepository;
import kitchenpos.common.dto.tablegroup.OrderTableIdRequest;
import kitchenpos.common.dto.tablegroup.TableGroupRequest;
import kitchenpos.common.dto.tablegroup.TableGroupResponse;
import kitchenpos.common.utils.StreamUtils;

@Service
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final TableGroupRepository tableGroupRepository) {
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<Long> orderTableIds = StreamUtils.mapToList(tableGroupRequest.getOrderTables(), OrderTableIdRequest::getId);
        TableGroup tableGroup = tableGroupRepository.save(TableGroup.create());
        tableGroup.group(orderTableIds);

        return TableGroupResponse.from(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = findTableGroup(tableGroupId);
        tableGroup.ungroup();
    }

    private TableGroup findTableGroup(Long id) {
        return tableGroupRepository.findById(id)
                                   .orElseThrow(EntityNotFoundException::new);
    }
}
