package kitchenpos.application;

import kitchenpos.fixture.TestTableGroupFactory;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.OrderTableIdRequest;
import kitchenpos.table.dto.OrderTableRequest;
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
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private TableGroupRepository tableGroupRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    private OrderTable 주문_테이블_1;
    private OrderTable 주문_테이블_2;
    private OrderTableRequest 주문_테이블_요청_1;
    private OrderTableRequest 주문_테이블_요청_2;
    private TableGroup 단체_테이블;
    private TableGroupRequest 단체_테이블_요청;

    @BeforeEach
    void setUp() {
        주문_테이블_요청_1 = new OrderTableRequest(4, true);
        주문_테이블_요청_2 = new OrderTableRequest(3, true);

        주문_테이블_1 = new OrderTable(1L, 단체_테이블, 4, true);
        주문_테이블_2 = new OrderTable(2L, 단체_테이블, 4, true);
        단체_테이블 = TestTableGroupFactory.create(1L, Arrays.asList(주문_테이블_1, 주문_테이블_2));
        단체_테이블_요청 = new TableGroupRequest(Arrays.asList(new OrderTableIdRequest(1L), new OrderTableIdRequest(2L)));
    }

    @DisplayName("단체 테이블을 등록한다")
    @Test
    void create() throws Exception {
        // given
        given(orderTableRepository.findAllByIdIn(any())).willReturn(Arrays.asList(주문_테이블_1, 주문_테이블_2));
        given(tableGroupRepository.save(any())).willReturn(단체_테이블);

        // when
        TableGroupResponse tableGroup = tableGroupService.create(단체_테이블_요청);

        // then
        assertThat(tableGroup.getOrderTableResponses()).hasSize(2);
    }

    @DisplayName("단체 테이블을 등록시 주문테이블이 1개라면 등록할 수 없다")
    @Test
    void createException() throws Exception {
        // given
        단체_테이블_요청 = new TableGroupRequest(Collections.singletonList(new OrderTableIdRequest(1L)));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(단체_테이블_요청)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 테이블로 등록할 주문 테이블은 모두 존재해야 한다.")
    @Test
    void createException2() throws Exception {
        // given
        주문_테이블_1 = new OrderTable(4, true);
        given(orderTableRepository.findAllByIdIn(any())).willReturn(Collections.singletonList(주문_테이블_1));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(단체_테이블_요청)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 테이블로 등록 시 모든 주문 테이블은 빈 테이블이어야 한다")
    @Test
    void createException3() throws Exception {
        // given
        주문_테이블_2 = new OrderTable(3, false);
        단체_테이블 = TestTableGroupFactory.create(1L, Arrays.asList(주문_테이블_1, 주문_테이블_2));
        given(orderTableRepository.findAllByIdIn(any())).willReturn(Arrays.asList(주문_테이블_1, 주문_테이블_2));
        given(tableGroupRepository.save(any())).willReturn(단체_테이블);

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(단체_테이블_요청)).isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("단체 테이블로 등록 시 모든 주문 테이블은 단체로 지정된 테이블이 아니어야 한다")
    @Test
    void createException4() throws Exception {
        // given
        주문_테이블_2 = new OrderTable(1L, 단체_테이블, 3, false);
        단체_테이블 = TestTableGroupFactory.create(1L, Arrays.asList(주문_테이블_1, 주문_테이블_2));
        given(orderTableRepository.findAllByIdIn(any())).willReturn(Arrays.asList(주문_테이블_1, 주문_테이블_2));
        given(tableGroupRepository.save(any())).willReturn(단체_테이블);
        // when & then
        assertThatThrownBy(() -> tableGroupService.create(단체_테이블_요청)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 테이블을 해제한다")
    @Test
    void ungroup() throws Exception {
        // given
        주문_테이블_1 = new OrderTable(1L, 단체_테이블, 4, false);
        주문_테이블_2 = new OrderTable(2L, 단체_테이블, 4, false);

        given(orderTableRepository.findAllByTableGroupId(단체_테이블.getId())).willReturn(Arrays.asList(주문_테이블_1, 주문_테이블_2));

        // when
        tableGroupService.ungroup(단체_테이블.getId());

        // then
        assertAll("모든 주문 테이블의 단체테이블 연관관계가 끊어진다",
                () -> assertThat(주문_테이블_1.getTableGroup()).isNull(),
                () -> assertThat(주문_테이블_2.getTableGroup()).isNull());
    }

    @DisplayName("해제시 주문테이블의 주문상태가 COOKING, MEAL 이면 해제할 수 없다.")
    @Test
    void ungroupException() throws Exception {
        // given
        given(orderTableRepository.findAllByTableGroupId(단체_테이블.getId())).willReturn(Arrays.asList(주문_테이블_1, 주문_테이블_2));
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(true);

        // when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(단체_테이블.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
