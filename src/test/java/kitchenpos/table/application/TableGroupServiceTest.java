package kitchenpos.table.application;

import kitchenpos.fixture.TestTableGroupFactory;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.*;
import kitchenpos.table.dto.OrderTableIdRequest;
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
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private TableGroupRepository tableGroupRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private TableService tableService;
    @Mock
    private TableGroupValidator tableGroupValidator;

    @InjectMocks
    private TableGroupService tableGroupService;
    @InjectMocks
    private TableGroupValidator tableGroupValidatorInject;

    @InjectMocks
    private UnGroupWithGroupEventHandler unGroupWithGroupEventHandler;

    private OrderTable 주문_테이블_1;
    private OrderTable 주문_테이블_2;
    private TableGroup 단체_테이블;
    private TableGroupRequest 단체_테이블_요청;

    @BeforeEach
    void setUp() {
        주문_테이블_1 = new OrderTable(1L, 단체_테이블, 4, true);
        주문_테이블_2 = new OrderTable(2L, 단체_테이블, 4, true);
        단체_테이블 = TestTableGroupFactory.create(1L, Arrays.asList(주문_테이블_1, 주문_테이블_2));
        단체_테이블_요청 = new TableGroupRequest(Arrays.asList(new OrderTableIdRequest(1L), new OrderTableIdRequest(2L)));
    }

    @DisplayName("단체 테이블을 등록한다")
    @Test
    void create() throws Exception {
        // given
        given(tableGroupRepository.save(any())).willReturn(단체_테이블);
        given(tableService.getOrderTables(any())).willReturn(단체_테이블.getOrderTables());
        doNothing().when(tableGroupValidator).validate(any(), any());

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
        assertThatThrownBy(() -> tableGroupValidatorInject.validate(단체_테이블_요청.getOrderTables(), 단체_테이블.getOrderTables()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 테이블로 등록할 주문 테이블은 모두 존재해야 한다.")
    @Test
    void createException2() throws Exception {
        // given
        주문_테이블_1 = new OrderTable(4, true);

        // when & then
        assertThatThrownBy(() -> tableGroupValidatorInject.validate(단체_테이블_요청.getOrderTables(), 단체_테이블.getOrderTables()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 테이블 등록 시 빈 주문 테이블이 있으면 예외가 발생한다")
    @Test
    void createException3() throws Exception {
        // given
        주문_테이블_2 = new OrderTable(3, true);

        // when & then
        assertThatThrownBy(() -> tableGroupValidatorInject.validate(단체_테이블_요청.getOrderTables(), 단체_테이블.getOrderTables()))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("단체 테이블로 등록 시 단체로 지정된 테이블이 있으면 예외가 발생한다")
    @Test
    void createException4() throws Exception {
        // given
        주문_테이블_1 = new OrderTable(1L, 단체_테이블, 3, false);
        주문_테이블_2 = new OrderTable(2L, 단체_테이블, 3, false);
        단체_테이블 = TestTableGroupFactory.create(1L, Arrays.asList(주문_테이블_1, 주문_테이블_2));

        // when & then
        assertThatThrownBy(() -> tableGroupValidatorInject.validate(단체_테이블_요청.getOrderTables(), 단체_테이블.getOrderTables()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 테이블을 해제한다")
    @Test
    void ungroup() throws Exception {
        // given
        주문_테이블_1 = new OrderTable(1L, 단체_테이블, 4, false);
        주문_테이블_2 = new OrderTable(2L, 단체_테이블, 4, false);

        given(orderTableRepository.findAllByTableGroupId(단체_테이블.getId())).willReturn(Arrays.asList(주문_테이블_1, 주문_테이블_2));
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(false);
        // when
        unGroupWithGroupEventHandler.handle(new OrderTableUnGroupEvent(단체_테이블));

        // then
        assertAll("모든 주문 테이블의 단체테이블이 null이 된다",
                () -> assertThat(주문_테이블_1.getTableGroup()).isNull(),
                () -> assertThat(주문_테이블_2.getTableGroup()).isNull());
    }

    @DisplayName("해제시 주문테이블의 주문상태가 COOKING, MEAL 이면 해제할 수 없다.")
    @Test
    void ungroupException() throws Exception {
        // given
        given(orderTableRepository.findAllByTableGroupId(any())).willReturn(Arrays.asList(주문_테이블_1, 주문_테이블_2));
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(true);

        // when & then
        assertThatThrownBy(() -> unGroupWithGroupEventHandler.handle(new OrderTableUnGroupEvent(단체_테이블)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
