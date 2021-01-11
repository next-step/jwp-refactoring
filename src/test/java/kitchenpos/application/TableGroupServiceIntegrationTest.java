package kitchenpos.application;

import kitchenpos.dao.JdbcTemplateOrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class TableGroupServiceIntegrationTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableService tableService;

    @Autowired
    private JdbcTemplateOrderTableDao jdbcTemplateOrderTableDao;


    @DisplayName("주문 테이블을 그룹화 할수 있다.")
    @Test
    void createTableGroup() {
        OrderTable table1 = tableService.create(new OrderTable(4, true));
        OrderTable table2 = tableService.create(new OrderTable(4, true));

        TableGroup tableGroup = tableGroupService.create(new TableGroup(Arrays.asList(table1, table2)));

        assertThat(tableGroup.getOrderTables()).hasSize(2);
    }

    @DisplayName("테이블 그룹을 2개 이상이어야 한다.")
    @Test
    void tableGroupSizeGreaterThanOne() {
        OrderTable table1 = tableService.create(new OrderTable(4, true));

        assertThatThrownBy(() -> tableGroupService.create(new TableGroup(Arrays.asList(table1))))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("주문 상태가 조리, 식사중일때 그룹을 삭제 할 수 없다.")
    @Test
    void deleteTableGroup() {
        // given
        OrderTable table1 = tableService.create(new OrderTable(4, true));
        OrderTable table2 = tableService.create(new OrderTable(4, true));
        TableGroup tableGroup = tableGroupService.create(new TableGroup(Arrays.asList(table1, table2)));

        // when
        tableGroupService.ungroup(tableGroup.getId());

        // then
        assertThat(jdbcTemplateOrderTableDao.findAllByTableGroupId(tableGroup.getId())).isEmpty();
    }
}