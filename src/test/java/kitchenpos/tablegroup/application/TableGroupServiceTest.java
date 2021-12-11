package kitchenpos.tablegroup.application;

import kitchenpos.ServiceTest;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("테이블 그룹 서비스 테스트")
class TableGroupServiceTest extends ServiceTest {

    @Test
    @DisplayName("테이블 그룹을 등록한다.")
    void create() {
        // given
        OrderTable orderTable1 = 테이블_저장(true);
        OrderTable orderTable2 = 테이블_저장(true);
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), Arrays.asList(orderTable1, orderTable2));

        // when
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // then
        assertAll(
                () -> assertThat(savedTableGroup.getId()).isNotNull(),
                () -> assertThat(savedTableGroup.getCreatedDate()).isNotNull(),
                () -> assertThat(savedTableGroup.getOrderTables().size()).isEqualTo(2)
        );
    }

    @Test
    @DisplayName("1개 이하인 테이블 수로 테이블 그룹을 등록하면 예외가 발생한다.")
    void createThrowException1() {
        // given
        OrderTable orderTable = 테이블_저장(true);
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), Collections.singletonList(orderTable));

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @Test
    @DisplayName("존재하지 않는 테이블로 테이블 그룹을 등록하면 예외가 발생한다.")
    void createThrowException2() {
        // given
        OrderTable orderTable1 = 테이블_저장(true);
        OrderTable orderTable2 = new OrderTable(2, true);
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), Arrays.asList(orderTable1, orderTable2));

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @Test
    @DisplayName("비어있는 테이블로 테이블 그룹을 등록하면 예외가 발생한다.")
    void createThrowException3() {
        // given
        OrderTable orderTable1 = 테이블_저장(true);
        OrderTable orderTable2 = 테이블_저장(false);
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), Arrays.asList(orderTable1, orderTable2));

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @Test
    @DisplayName("테이블 그룹에서 테이블을 제거한다.")
    void ungroup() {
        // given
        TableGroup savedTableGroup = 테이블_그룹_저장();

        // when
        tableGroupService.ungroup(savedTableGroup.getId());

        // then
        assertAll(
                () -> assertThat(테이블_조회(savedTableGroup.getOrderTables().get(0).getId()).getTableGroupId()).isNull(),
                () -> assertThat(테이블_조회(savedTableGroup.getOrderTables().get(1).getId()).getTableGroupId()).isNull()
        );
    }
}
