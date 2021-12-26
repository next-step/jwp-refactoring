package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.dto.TableRequest;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TableGroupServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;

    @DisplayName("주문 테이블 목록을 단체 지정할 수 있다.")
    @Test
    void 테이블_그룹_등록() {
        // given
        List<Long> orderTableIds = Arrays.asList(1L, 2L);
        TableGroupRequest request = new TableGroupRequest(orderTableIds);

        // when
        TableGroupResponse actual = tableGroupService.create(request);

        // then
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getCreateDate()).isNotNull();
        assertThat(actual.getOrderTables().sameSizeAs(orderTableIds.size())).isTrue();
    }

    @DisplayName("주문 테이블 목록이 빈 경우 단체 지정할 수 없다.")
    @Test
    void 테이블_그룹_생성_예외_테이블_목록_없음() {
        // given
        TableGroupRequest request = new TableGroupRequest(Lists.emptyList());

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
            () -> tableGroupService.create(request)
        );
    }

    @DisplayName("주문 테이블 목록이 1개만 있는 경우 단체 지정할 수 없다.")
    @Test
    void 테이블_그룹_생성_예외_테이블_목록이_1개() {
        // given
        TableGroupRequest request = new TableGroupRequest(Lists.newArrayList(1L));

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
            () -> tableGroupService.create(request)
        );
    }

    @DisplayName("빈 테이블이 아닌 테이블이 있을 경우 단체 지정할 수 없다.")
    @Test
    void 테이블_그룹_생성_예외_빈테이블_포함() {
        // given
        tableService.changeEmpty(2L, new TableRequest(3, false));

        List<Long> orderTableIds = Arrays.asList(1L, 2L);
        TableGroupRequest request = new TableGroupRequest(orderTableIds);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
            () -> tableGroupService.create(request)
        );
    }

    @DisplayName("이미 단체 지정된 테이블인 경우 단체 지정할 수 없다.")
    @Test
    void 테이블_그룹_생성_예외_이미_단체_지정됨() {
        // given
        단체_지정(1L, 2L);
        TableGroupRequest request = new TableGroupRequest(Arrays.asList(1L, 2L));

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
            () -> tableGroupService.create(request)
        );
    }

    @DisplayName("단체 지정을 해제할 수 있다.")
    @Test
    void 테이블_그룹_해제() {
        // given
        TableGroupResponse tableGroupResponse = 단체_지정(1L, 2L);

        // when, then
        tableGroupService.ungroup(tableGroupResponse.getId());
    }

    @DisplayName("주문 테이블 목록 중 '조리' 나 '식사' 상태가 있는 경우 단체 지정을 해제할 수 없다.")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void 테이블_그룹_해제_예외_조리_혹은_식사_상태(String statusName) {
        // given
        Long orderTableId = 1L;
        TableGroupResponse tableGroupResponse = 단체_지정(1L, 2L);

        주문_생성(orderTableId, statusName);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
            () -> tableGroupService.ungroup(tableGroupResponse.getId())
        );
    }

    private TableGroupResponse 단체_지정(Long... orderTableIdList) {
        List<Long> orderTableIds = Arrays.asList(orderTableIdList);
        TableGroupRequest request = new TableGroupRequest(orderTableIds);
        return tableGroupService.create(request);
    }

    private OrderResponse 주문_생성(Long orderTableId, String statusName) {
        OrderStatus orderStatus = OrderStatus.valueOf(statusName);
        OrderRequest request = new OrderRequest(orderTableId, orderStatus, OrderServiceTest.ORDER_LINE_ITEMS);
        return orderService.create(request);
    }
}
