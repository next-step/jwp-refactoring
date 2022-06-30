package kitchenpos.table.application;

import kitchenpos.order.application.OrderStatusValidator;
import kitchenpos.table.domain.*;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.table.application.TableServiceTest.주문_테이블_데이터_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@DisplayName("단체 지정 관련 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    OrderTableRepository orderTableRepository;

    @Mock
    TableGroupRepository tableGroupRepository;

    @Mock
    OrderStatusValidator statusValidator;

    @InjectMocks
    TableGroupService tableGroupService;

    @DisplayName("단체 지정을 할 수 있다")
    @Test
    void create() {
        // given
        OrderTable orderTableId1 = 주문_테이블_데이터_생성(1L, null, 2, true);
        OrderTable orderTableId2 = 주문_테이블_데이터_생성(2L, null, 4, true);
        List<OrderTable> orderTables = Arrays.asList(orderTableId1, orderTableId2);
        TableGroupRequest request = 테이블_그룹_요청_데이터_생성(orderTables);
        TableGroup 예상값 = 테이블_그룹_데이터_생성(1L, orderTables);
        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(orderTables);
        given(tableGroupRepository.save(any(TableGroup.class))).willReturn(예상값);

        // when
        TableGroupResponse 테이블_그룹_생성_결과 = 테이블_그룹_생성(request);

        // then
        테이블_그룹_데이터_비교(테이블_그룹_생성_결과, TableGroupResponse.of(예상값));
    }

    @DisplayName("단체 지정을 할 수 있다 - 주문 테이블이 빈 테이블이어야 한다")
    @Test
    void create_exception1() {
        // given
        OrderTable orderTableId1 = 주문_테이블_데이터_생성(1L, null, 2, false);
        OrderTable orderTableId2 = 주문_테이블_데이터_생성(2L, null, 4, false);
        List<OrderTable> orderTables = Arrays.asList(orderTableId1, orderTableId2);
        TableGroupRequest request = 테이블_그룹_요청_데이터_생성(orderTables);
        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(orderTables);

        // when && then
        assertThatThrownBy(() -> 테이블_그룹_생성(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 할 수 있다 - 주문 테이블의 수가 2개 이상이어야 한다")
    @Test
    void create_exception2() {
        // given
        OrderTable orderTableId1 = 주문_테이블_데이터_생성(1L, null, 2, false);
        List<OrderTable> orderTables = Collections.singletonList(orderTableId1);
        TableGroupRequest request = 테이블_그룹_요청_데이터_생성(orderTables);

        // when && then
        assertThatThrownBy(() -> 테이블_그룹_생성(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 할 수 있다 - 그룹으로 지정되지 않은 테이블이어야 한다")
    @Test
    void create_exception3() {
        // given
        OrderTable orderTableId1 = 주문_테이블_데이터_생성(1L, 테이블_그룹_데이터_생성(), 2, false);
        OrderTable orderTableId2 = 주문_테이블_데이터_생성(2L, 테이블_그룹_데이터_생성(), 4, false);
        List<OrderTable> orderTables = Arrays.asList(orderTableId1, orderTableId2);
        TableGroupRequest request = 테이블_그룹_요청_데이터_생성(orderTables);
        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(orderTables);

        // when && then
        assertThatThrownBy(() -> 테이블_그룹_생성(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 해제할 수 있다")
    @Test
    void ungroup() {
        // given
        OrderTable orderTableId1 = 주문_테이블_데이터_생성(1L, 테이블_그룹_데이터_생성(), 2, true);
        OrderTable orderTableId2 = 주문_테이블_데이터_생성(2L, 테이블_그룹_데이터_생성(), 4, true);
        List<OrderTable> orderTables = Arrays.asList(orderTableId1, orderTableId2);

        given(orderTableRepository.findAllByTableGroupId(1L)).willReturn(orderTables);

        // when & then
        테이블_그룹_해제(1L);
    }

    public static TableGroup 테이블_그룹_데이터_생성(Long id, List<OrderTable> orderTables) {
        return new TableGroup(id, new OrderTables(orderTables));
    }

    public static TableGroup 테이블_그룹_데이터_생성() {
        return new TableGroup();
    }

    public static TableGroupRequest 테이블_그룹_요청_데이터_생성(List<OrderTable> orderTables) {
        return new TableGroupRequest(orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList()));
    }

    private TableGroupResponse 테이블_그룹_생성(TableGroupRequest tableGroupRequest) {
        return tableGroupService.create(tableGroupRequest);
    }

    private void 테이블_그룹_해제(long tableGroupId) {
        tableGroupService.ungroup(tableGroupId);
    }

    private void 테이블_그룹_데이터_비교(TableGroupResponse 테이블_그룹_생성_결과, TableGroupResponse 예상값) {
        assertAll(
                () -> assertThat(테이블_그룹_생성_결과.getId()).isEqualTo(예상값.getId()),
                () -> assertThat(테이블_그룹_생성_결과.getOrderTables()).isEqualTo(예상값.getOrderTables())
        );
    }
}
