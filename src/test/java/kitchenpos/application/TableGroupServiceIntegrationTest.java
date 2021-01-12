package kitchenpos.application;

import kitchenpos.IntegrationTest;
import kitchenpos.dao.JdbcTemplateOrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class TableGroupServiceIntegrationTest extends IntegrationTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableService tableService;

    @Autowired
    private JdbcTemplateOrderTableDao jdbcTemplateOrderTableDao;

    private TableGroup tableGroup;

    @BeforeEach
    void setUp() {
        OrderTable table1 = tableService.create(new OrderTable(4, true));
        OrderTable table2 = tableService.create(new OrderTable(4, true));

        tableGroup = getTableGroup(table1, table2);
    }

    private TableGroup getTableGroup(OrderTable... table) {
        return new TableGroup(Arrays.asList(table));
    }

    @DisplayName("주문 테이블을 그룹화 할수 있다.")
    @Test
    void createTableGroup() {
        TableGroup createGroup = tableGroupService.create(tableGroup);

        assertThat(createGroup.getOrderTables()).hasSize(2);
    }

    @DisplayName("테이블 그룹을 2개 이상이어야 한다.")
    @Test
    void tableGroupSizeGreaterThanOne() {
        OrderTable table1 = tableService.create(new OrderTable(4, true));

        assertThatThrownBy(() -> tableGroupService.create(new TableGroup(Arrays.asList(table1))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹은 빈 테이블 상태어야 한다.")
    @Test
    void tableGroupIsNotEmpty() {
        OrderTable table1 = tableService.create(new OrderTable(4, true));
        OrderTable table2 = tableService.create(new OrderTable(4, false));

        assertThatThrownBy(() -> tableGroupService.create(getTableGroup(table1, table2)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 삭제 할 수 있다.")
    @Test
    void deleteTableGroup() {
        // given
        TableGroup createdGroup = tableGroupService.create(tableGroup);

        // when
        tableGroupService.ungroup(createdGroup.getId());

        // then
        assertThat(jdbcTemplateOrderTableDao.findAllByTableGroupId(createdGroup.getId())).isEmpty();
    }
}