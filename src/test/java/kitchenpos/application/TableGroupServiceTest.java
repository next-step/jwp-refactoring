package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private OrderTableDao orderTableDao;
    @Autowired
    private TableGroupDao tableGroupDao;
    @Autowired
    private TableGroupService tableGroupService;

    @Test
    @DisplayName("테이블 정보가 존재하지 않을 경우 예외를 던진다.")
    void createFail_orderTableNullOrEmpty() {
        OrderTable orderTable = new OrderTable(100L, null, 0, true);

        assertThatIllegalArgumentException()
            .isThrownBy(() -> this.tableGroupService.create(new TableGroup(Collections.emptyList())));
        assertThatIllegalArgumentException()
            .isThrownBy(() -> this.tableGroupService.create(new TableGroup(Collections.singletonList(orderTable))));
    }

    @Test
    @DisplayName("테이블 중 비어있지 않은 테이블이 존재할 경우 생성될 수 없다.")
    void createFail_orderTableEmptyStatus() {
        OrderTable orderTable = this.orderTableDao.save(new OrderTable(null, 4, false));

        assertThatIllegalArgumentException()
            .isThrownBy(() -> this.tableGroupService.create(new TableGroup(Collections.singletonList(orderTable))));
    }

    @Test
    @DisplayName("테이블 중 이미 테이블 그룹에 속한 테이블이 존재할 경우 생성될 수 없다.")
    void createFail_alreadyContainTableGroup() {
        OrderTable orderTable = this.orderTableDao.save(new OrderTable(null, 0, true));
        TableGroup tableGroup = this.tableGroupDao.save(new TableGroup(Collections.singletonList(orderTable)));

        assertThatIllegalArgumentException()
            .isThrownBy(() -> this.tableGroupService.create(new TableGroup(Collections.singletonList(orderTable))));
    }

    @Test
    @DisplayName("테이블 그룹이 정상적으로 생성된다.")
    void createTableGroup() {
        OrderTable orderTable1 = this.orderTableDao.save(new OrderTable(null, 0, true));
        OrderTable orderTable2 = this.orderTableDao.save(new OrderTable(null, 0, true));

        TableGroup tableGroup = this.tableGroupService.create(new TableGroup(Arrays.asList(orderTable1, orderTable2)));

        assertThat(tableGroup.getId()).isNotNull();
        assertThat(tableGroup.getOrderTables()).hasSize(2);
        assertTrue(tableGroup.getOrderTables().stream().anyMatch(orderTable -> !orderTable.isEmpty()));
    }

}
