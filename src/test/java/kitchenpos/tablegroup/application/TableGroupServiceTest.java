package kitchenpos.tablegroup.application;

import kitchenpos.ServiceTest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        OrderTableResponse savedOrderTableResponse1 = 테이블_저장(true);
        OrderTableResponse savedOrderTableResponse2 = 테이블_저장(true);
        TableGroupRequest tableGroupRequest = new TableGroupRequest(
                Arrays.asList(savedOrderTableResponse1.getId(), savedOrderTableResponse2.getId()));

        // when
        TableGroupResponse savedTableGroupResponse = tableGroupService.create(tableGroupRequest);

        // then
        assertAll(
                () -> assertThat(savedTableGroupResponse.getId()).isNotNull(),
                () -> assertThat(savedTableGroupResponse.getCreatedDate()).isNotNull(),
                () -> assertThat(savedTableGroupResponse.getOrderTables().size()).isEqualTo(2)
        );
    }

    @Test
    @DisplayName("1개 이하인 테이블 수로 테이블 그룹을 등록하면 예외가 발생한다.")
    void createThrowException1() {
        // given
        OrderTableResponse savedOrderTableResponse = 테이블_저장(true);
        TableGroupRequest tableGroupRequest = new TableGroupRequest(
                Collections.singletonList(savedOrderTableResponse.getId()));

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> tableGroupService.create(tableGroupRequest));
    }

    @Test
    @DisplayName("존재하지 않는 테이블로 테이블 그룹을 등록하면 예외가 발생한다.")
    void createThrowException2() {
        // given
        OrderTableResponse savedOrderTableResponse = 테이블_저장(true);
        TableGroupRequest tableGroupRequest = new TableGroupRequest(
                Arrays.asList(savedOrderTableResponse.getId(), 0L));

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> tableGroupService.create(tableGroupRequest));
    }

    @Test
    @DisplayName("비어있지 않은 테이블로 테이블 그룹을 등록하면 예외가 발생한다.")
    void createThrowException3() {
        // given
        OrderTableResponse savedOrderTableResponse1 = 테이블_저장(false);
        OrderTableResponse savedOrderTableResponse2 = 테이블_저장(false);
        TableGroupRequest tableGroupRequest = new TableGroupRequest(
                Arrays.asList(savedOrderTableResponse1.getId(), savedOrderTableResponse2.getId()));

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> tableGroupService.create(tableGroupRequest));
    }

    @Test
    @DisplayName("테이블 그룹에서 테이블을 제거한다.")
    void ungroup() {
        // given
        TableGroupResponse savedTableGroupResponse = 테이블_그룹_저장();

        // when
        tableGroupService.ungroup(savedTableGroupResponse.getId());

        // then
        assertAll(
                () -> assertThat(테이블_조회(savedTableGroupResponse.getOrderTables().get(0).getId()).getTableGroupId()).isNull(),
                () -> assertThat(테이블_조회(savedTableGroupResponse.getOrderTables().get(1).getId()).getTableGroupId()).isNull()
        );
    }
}
