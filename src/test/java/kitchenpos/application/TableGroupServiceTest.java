package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Arrays;
import java.util.Objects;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("주문 테이블 그룹을 관리한다.")
@SpringBootTest
class TableGroupServiceTest {

    @Autowired
    TableGroupService tableGroupService;

    @Autowired
    TableService tableService;

    @Autowired
    OrderService orderService;

    @DisplayName("주문 테이블 그룹을 [등록]과 [삭제]가 가능하다.")
    @Test
    void createAndUngroupTest() {
        // given
        OrderTable orderTable1 = tableService.create(
            new OrderTable(1, true)
        );

        OrderTable orderTable2 = tableService.create(
            new OrderTable(1, true)
        );

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));

        //주문 테이블 등록
        // when
        TableGroup actualTableGroup = tableGroupService.create(tableGroup);

        // then
        assertThat(actualTableGroup).isNotNull();
        assertThat(tableService.list().stream()
                .noneMatch(x -> Objects.equals(x.getTableGroupId(), actualTableGroup.getId()))
        ).isFalse();

        //주문 테이블 삭제
        // when
        tableGroupService.ungroup(actualTableGroup.getId());

        // then
        assertThat(tableService.list().stream()
            .noneMatch(x -> Objects.equals(x.getTableGroupId(), actualTableGroup.getId()))
        ).isTrue();

    }

    @DisplayName("주문 테이블 그룹을 등록시, 2개 이상의 주문 테이블만 등록할수 있다.")
    @Test
    void createExceptionTest1() {
        //given
        OrderTable orderTable1 = tableService.create(
            new OrderTable(1, true)
        );

        // when
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(orderTable1));

        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("2개").hasMessageContaining("등록");
    }

    @DisplayName("주문 테이블 그룹을 등록시, 주문테이블들은 사전에 등록되어 있어야 한다.")
    @Test
    void createExceptionTest2() {
        // when
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(new OrderTable(1, true), new OrderTable(1, true)));

        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("사전에").hasMessageContaining("등록");
    }

    @DisplayName("주문 테이블 그룹을 등록시, 테이블이 비어있어야 등록할수 있다.")
    @Test
    void createExceptionTest3() {
        // given
        OrderTable orderTable1 = tableService.create(
            new OrderTable(1, false)
        );

        OrderTable orderTable2 = tableService.create(
            new OrderTable(1, true)
        );

        // when
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));

        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("테이블이 비어있지 않습니다.");
    }
}