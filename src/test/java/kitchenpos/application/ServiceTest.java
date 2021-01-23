package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ServiceTest {

    protected int 게스트수;
    protected final boolean 비어있음 = true;
    protected final boolean 비어있지않음 = false;

    @Autowired
    protected TableService tableService;

    @Autowired
    protected TableGroupService tableGroupService;


    protected OrderTable 테이블을_생성한다(Long id, int numberOfGuest, boolean empty) {
        return tableService.create(new OrderTable(id, numberOfGuest, empty));
    }

    protected TableGroup 테이블_그룹을_생성한다(TableGroup tableGroup) {
        return tableGroupService.create(tableGroup);
    }

    protected void 테이블_그룹을_비운다(Long id) {
        tableGroupService.ungroup(id);
    }

}
