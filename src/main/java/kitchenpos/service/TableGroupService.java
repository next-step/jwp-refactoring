package kitchenpos.service;

import kitchenpos.domain.table.OrderTableGroup;
import kitchenpos.domain.table.TableGroupRepository;
import kitchenpos.domain.table.TableRepository;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.dto.TableRequest;

import static java.util.stream.Collectors.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final TableRepository tableRepository;

    public TableGroupService(final TableGroupRepository tableGroupRepository, final TableRepository tableRepository) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableRepository = tableRepository;
    }

    public TableGroupResponse applyGroup(TableGroupRequest tableGroupRequest) {
        List<Long> ids = tableGroupRequest.getOrderTables().stream().map(TableRequest::getTableId).collect(toList());
        OrderTableGroup orderTableGroup = new OrderTableGroup(tableRepository.findByIdIn(ids));
        orderTableGroup.applyGroup(ids.size());
        return TableGroupResponse.of(tableGroupRepository.save(orderTableGroup));
    }

    public void applyUnGroup(Long id) {
        OrderTableGroup orderTableGroup = tableGroupRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        orderTableGroup.applyUnGroup();
    }
}
