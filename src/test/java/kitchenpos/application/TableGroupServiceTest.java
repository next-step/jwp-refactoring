package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author : leesangbae
 * @project : kitchenpos
 * @since : 2021-01-10
 */
@SpringBootTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableService tableService;

    @DisplayName("단체 지정 생성시 중복되는 주문 테이블이 있는 경우")
    @Test
    void tableGroupCreateWithDuplicateOrderTablesTest() {
        OrderTable table01 = new OrderTable();
        table01.setId(1L);
        OrderTable table02 = new OrderTable();
        table02.setId(1L);

        TableGroup group = new TableGroup();
        group.setCreatedDate(LocalDateTime.now());
        group.setOrderTables(Arrays.asList(table01, table02));

        assertThatThrownBy(() -> tableGroupService.create(group))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 생성시 주문 테이블이 없는 경우")
    @Test
    void tableGroupCreateWithEmptyOrderTablesTest() {
        TableGroup group = new TableGroup();
        group.setCreatedDate(LocalDateTime.now());

        assertThatThrownBy(() -> tableGroupService.create(group))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 생성시 한개의 주문 테이블만 있는 경우")
    @Test
    void tableGroupCreateWithSingleOrderTablesTest() {
        OrderTable table = new OrderTable();
        table.setId(1L);

        TableGroup group = new TableGroup();
        group.setCreatedDate(LocalDateTime.now());
        group.setOrderTables(Collections.singletonList(table));

        assertThatThrownBy(() -> tableGroupService.create(group))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 생성시 테이블이 공석이 아닌 경우")
    @Test
    void tableGroupCreateWhenTableIsNotEmptyTest() {
        OrderTable table01 = new OrderTable();
        table01.setId(1L);
        table01.setEmpty(false);
        OrderTable table02 = new OrderTable();
        table02.setId(2L);
        table01.setEmpty(false);
        tableService.changeEmpty(1L, table01);
        tableService.changeEmpty(2L, table02);

        TableGroup group = new TableGroup();
        group.setCreatedDate(LocalDateTime.now());
        group.setOrderTables(Arrays.asList(table01, table02));

        assertThatThrownBy(() -> tableGroupService.create(group))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("단체 지정 해제시 주문 테이블중 하나라도 COOKING, MEAL 상태인 경우")
    @Test
    void tableGroupCreateWhenTableNotCompleteStateTest() {
        OrderTable table01 = new OrderTable();
        table01.setId(1L);
        OrderTable table02 = new OrderTable();
        table02.setId(2L);
        TableGroup group = new TableGroup();
        group.setCreatedDate(LocalDateTime.now());
        group.setOrderTables(Arrays.asList(table01, table02));

        TableGroup savedGroup = tableGroupService.create(group);

        assertThatThrownBy(() -> tableGroupService.ungroup(savedGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }


}
