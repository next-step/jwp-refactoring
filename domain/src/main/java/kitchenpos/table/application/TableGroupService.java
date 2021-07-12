package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.exception.NotCreateTableGroupException;
import kitchenpos.exception.NotFoundEntityException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.CreateTableGroupDto;
import kitchenpos.table.dto.OrderTableIdDto;
import kitchenpos.table.dto.TableGroupDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupDto create(CreateTableGroupDto createTableGroupDto) {

        List<OrderTableIdDto> orderTables = createTableGroupDto.getOrderTables();
        List<Long> orderTableIds = orderTables.stream()
                                              .map(OrderTableIdDto::getId)
                                              .collect(Collectors.toList());

        List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        if (orderTables.size() != savedOrderTables.size()) {
            throw new NotCreateTableGroupException("입력한 주문 테이블에 중복이 있습니다.");
        }

        return TableGroupDto.of(tableGroupRepository.save(new TableGroup(savedOrderTables)));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                                                    .orElseThrow(NotFoundEntityException::new);

        tableGroup.ungroup();
    }
}
