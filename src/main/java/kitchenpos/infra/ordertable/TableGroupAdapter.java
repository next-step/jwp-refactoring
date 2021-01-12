package kitchenpos.infra.ordertable;

import kitchenpos.domain.ordertable.SafeTableGroup;
import kitchenpos.domain.ordertable.exceptions.InvalidTryChangeEmptyException;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.domain.tablegroup.TableGroupRepository;
import org.springframework.stereotype.Component;

@Component
public class TableGroupAdapter implements SafeTableGroup {
    private final TableGroupRepository tableGroupRepository;

    public TableGroupAdapter(final TableGroupRepository tableGroupRepository) {
        this.tableGroupRepository = tableGroupRepository;
    }

    @Override
    public void canChangeEmptyStatus(final Long orderTableId) {
        if (tableGroupRepository.findTableGroupByOrderTableId(orderTableId).isPresent()) {
            throw new InvalidTryChangeEmptyException("단체 지정된 주문 테이블의 자리 비움 상태를 바꿀 수 없습니다.");
        }
    }

    @Override
    public Long getTableGroupId(final Long orderTableId) {
        TableGroup tableGroup = tableGroupRepository.findTableGroupByOrderTableId(orderTableId)
                .orElse(null);

        if (tableGroup == null) {
            return null;
        }

        return tableGroup.getId();
    }
}
