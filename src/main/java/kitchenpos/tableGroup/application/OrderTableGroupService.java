package kitchenpos.tableGroup.application;

import java.util.List;


public interface OrderTableGroupService {
    void validateComplete(List<Long> orderTableIds);
}
