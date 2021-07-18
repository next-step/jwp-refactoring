package kitchenpos.tablegroup.application;

import kitchenpos.table.domain.OrderTableLinker;
import kitchenpos.tablegroup.domain.TableGroupLinker;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TestLinkerImplementation implements OrderTableLinker, TableGroupLinker {

    @Override
    public void validateOrderStatusByTableIds(List<Long> orderTableIds) {
        System.out.println("validateOrderStatusByTableIds...work");
    }

    @Override
    public void validateOrderStatusByOrderTableId(Long orderTableId) {
        System.out.println("validateOrderStatusByOrderTableId...work");
    }
}
