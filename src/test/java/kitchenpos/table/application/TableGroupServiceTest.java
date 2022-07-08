package kitchenpos.table.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.*;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    OrderRepository orderRepository;

    @Mock
    OrderTableRepository orderTableRepository;

    @Mock
    TableGroupRepository tableGroupRepository;

    @InjectMocks
    TableGroupService tableGroupService;

    private TableGroup 단체_지정;

    private OrderTable 단체_지정할_테이블1;
    private OrderTable 단체_지정할_테이블2;
    private OrderTable 단체_지정할_테이블3;
    private OrderTable 단체_지정할_테이블4;
    private List<OrderTable> 단체_지정할_테이블_목록;

    @BeforeEach
    void setUp() {
        단체_지정할_테이블1 = new OrderTable(1L, null, 4, true);
        단체_지정할_테이블2 = new OrderTable(2L, null, 6, true);
        단체_지정할_테이블_목록 = Arrays.asList(단체_지정할_테이블1, 단체_지정할_테이블2);
        단체_지정 = new TableGroup(1L, 단체_지정할_테이블_목록);

        단체_지정할_테이블3 = new OrderTable(3L, 4, true);
        단체_지정할_테이블4 = new OrderTable(4L, 6, true);
    }

    @DisplayName("단체 지정(주문 테이블 그룹화)을 할 수 있다.")
    @Test
    void createTableGroup() {
        // given
        TableGroupRequest 단체_지정_요청 = new TableGroupRequest(Arrays.asList(3L, 4L));

        given(orderTableRepository.findAllByIdIn(anyList()))
                .willReturn(Arrays.asList(단체_지정할_테이블3, 단체_지정할_테이블4));
        given(tableGroupRepository.save(any()))
                .willReturn(단체_지정);

        // when
        TableGroupResponse 단체_지정_응답 = tableGroupService.create(단체_지정_요청);

        // then
        단체_지정_성공(단체_지정_응답, 단체_지정);
    }

    @DisplayName("주문 테이블이 2개 미만인 경우 단체 지정에 실패한다.")
    @Test
    void createTableGroupFailsWhenNotEnoughOrderTables() {
        // given
        TableGroupRequest 단체_지정_요청 = new TableGroupRequest(Arrays.asList(3L));

        given(orderTableRepository.findAllByIdIn(anyList()))
                .willReturn(Arrays.asList(단체_지정할_테이블3));

        // when & then
        단체_지정_실패_주문테이블_부족(단체_지정_요청);
    }

    @DisplayName("중복된 주문 테이블이 존재하면 단체 지정에 실패한다.")
    @Test
    void createTableGroupFailsWhenDuplicateOrderTables() {
        TableGroupRequest 단체_지정_요청 = new TableGroupRequest(Arrays.asList(3L, 3L));
        List<OrderTable> 유니크한_주문테이블_목록 = Arrays.asList(단체_지정할_테이블3);

        given(orderTableRepository.findAllByIdIn(anyList()))
                .willReturn(유니크한_주문테이블_목록);

        // when & then
        단체_지정_실패_중복_주문테이블(단체_지정_요청);
    }

    @DisplayName("비어있지 않은 주문 테이블이 존재하면 단체 지정에 실패한다.")
    @Test
    void createTableGroupFailsWhenNotEmptyOrderTables() {
        // given
        OrderTable 빈_테이블 = new OrderTable(3L, 4, true);
        OrderTable 비어있지_않은_테이블 = new OrderTable(4L, 6, false);
        단체_지정할_테이블_목록 = Arrays.asList(빈_테이블, 비어있지_않은_테이블);

        TableGroupRequest 단체_지정_요청 = new TableGroupRequest(Arrays.asList(3L, 4L));

        given(orderTableRepository.findAllByIdIn(anyList()))
                .willReturn(단체_지정할_테이블_목록);

        // when & then
        단체_지정_실패_비어있지_않은_테이블(단체_지정_요청);
    }

    @DisplayName("단체 지정되어 있는 주문 테이블이 존재하면 단체 지정에 실패한다.")
    @Test
    void createTableGroupFailsWhenAlreadyGrouped() {
        // given
        TableGroupRequest 단체_지정_요청 = new TableGroupRequest(Arrays.asList(1L, 2L));

        given(orderTableRepository.findAllByIdIn(anyList()))
                .willReturn(단체_지정할_테이블_목록);

        // when & then
        단체_지정_실패_단체_지정_존재(단체_지정_요청);
    }

    @DisplayName("단체 지정을 해제 할 수 있다.")
    @Test
    void ungroupTableGroup() {
        // given
        given(tableGroupRepository.findById(단체_지정.getId()))
                .willReturn(Optional.ofNullable(단체_지정));
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList()))
                .willReturn(false);

        // when & then
        단체_지정_해제_성공(단체_지정.getId());
    }

    @DisplayName("주문 테이블이 '조리' 혹은 '식사' 상태이면 단체 지정에 실패한다.")
    @Test
    void ungroupTableGroupFailsWhenCookingOrMealOrderStatus() {
        // given
        given(tableGroupRepository.findById(단체_지정.getId()))
                .willReturn(Optional.ofNullable(단체_지정));
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList()))
                .willReturn(true);

        // when & then
        단체_지정_해제_실패_계산완료_아님(단체_지정.getId());
    }


    private void 단체_지정_성공(TableGroupResponse 단체_지정_응답, TableGroup 생성할_단체_지정) {
        assertAll(
                () -> assertThat(단체_지정_응답.getId())
                        .isNotNull(),
                () -> assertThat(단체_지정_응답.getCreatedDate())
                        .isNotNull(),
                () -> assertThat(단체_지정_응답.getOrderTables())
                        .hasSize(생성할_단체_지정.getOrderTables().size())
        );
    }

    private void 단체_지정_실패_중복_주문테이블(TableGroupRequest 단체_지정_요청) {
        assertThatThrownBy(() -> tableGroupService.create(단체_지정_요청))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("단체 지정시 주문 테이블은 중복될 수 없습니다.");
    }

    private void 단체_지정_실패_주문테이블_부족(TableGroupRequest 단체_지정_요청) {
        assertThatThrownBy(() -> tableGroupService.create(단체_지정_요청))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("단체 지정시 테이블은 최소 2개 이상이어야 합니다.");
    }

    private void 단체_지정_실패_비어있지_않은_테이블(TableGroupRequest 단체_지정_요청) {
        assertThatThrownBy(() -> tableGroupService.create(단체_지정_요청))
                .isExactlyInstanceOf(IllegalStateException.class)
                .hasMessage("비어있지 않은 테이블은 단체 지정을 할 수 없습니다.");
    }

    private void 단체_지정_실패_단체_지정_존재(TableGroupRequest 단체_지정_요청) {
        assertThatThrownBy(() -> tableGroupService.create(단체_지정_요청))
                .isExactlyInstanceOf(IllegalStateException.class)
                .hasMessage("이미 단체 지정이 되어있는 테이블은 단체 지정을 할 수 없습니다.");
    }

    private void 단체_지정_해제_성공(Long 해제할_단체_지정ID) {
        tableGroupService.ungroup(해제할_단체_지정ID);
    }

    private void 단체_지정_해제_실패_계산완료_아님(Long 해제할_단체_지정ID) {
        assertThatThrownBy(() -> tableGroupService.ungroup(해제할_단체_지정ID))
                .isExactlyInstanceOf(IllegalStateException.class)
                .hasMessage("주문 상태가 모두 완료일때만 단체 지정해제가 가능합니다.");
    }
}
