package kitchenpos.order.application;

import kitchenpos.order.domain.*;
import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import kitchenpos.order.exception.OrderExceptionCode;
import kitchenpos.order.exception.OrderTableExceptionCode;
import kitchenpos.order.exception.TableGroupExceptionCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("TableGroupService 테스트")
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

    private TableGroup 단체_테이블;
    private OrderTable 주문테이블1;
    private OrderTable 주문테이블2;
    private OrderTable 주문테이블3;
    private OrderTable 주문테이블4;
    private List<OrderTable> 주문_테이블_목록;

    @BeforeEach
    void setUp() {
        단체_테이블 = new TableGroup();

        주문테이블1 = new OrderTable(4, true);
        주문테이블2 = new OrderTable(4, true);
        주문테이블3 = new OrderTable(2, true);
        주문테이블4 = new OrderTable(2, true);

        ReflectionTestUtils.setField(단체_테이블, "id", 1L);
        ReflectionTestUtils.setField(주문테이블1, "id", 1L);
        ReflectionTestUtils.setField(주문테이블2, "id", 2L);
        ReflectionTestUtils.setField(주문테이블3, "id", 3L);
        ReflectionTestUtils.setField(주문테이블4, "id", 4L);

        주문_테이블_목록 = Arrays.asList(주문테이블1, 주문테이블2);
        단체_테이블.group(주문_테이블_목록);
    }

    @Test
    void 등록되어_있지_않은_주문_테이블이_포함되어_있으면_테이블_그룹을_등록할_수_없음() {
        given(orderTableRepository.findAllById(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId())))
                .willReturn(Arrays.asList(주문테이블1));

        assertThatThrownBy(() -> {
            tableGroupService.create(new TableGroupRequest(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId())));
        }).isInstanceOf(EntityNotFoundException.class)
                .hasMessage(OrderTableExceptionCode.NOT_FOUND_BY_ID.getMessage());
    }

    @Test
    void 주문_테이블_목록이_비어있으면_테이블_그룹을_등록할_수_없음() {
        assertThatThrownBy(() -> {
            tableGroupService.create(new TableGroupRequest(Collections.emptyList()));
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(TableGroupExceptionCode.ORDER_TABLES_CANNOT_BE_EMPTY.getMessage());
    }

    @Test
    void 주문_테이블_목록이_2보다_작으면_테이블_그룹을_등록할_수_없음() {
        given(orderTableRepository.findAllById(Arrays.asList(주문테이블1.getId())))
                .willReturn(Arrays.asList(주문테이블1));

        assertThatThrownBy(() -> {
            tableGroupService.create(new TableGroupRequest(Arrays.asList(주문테이블1.getId())));
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(TableGroupExceptionCode.MUST_BE_GREATER_THAN_MINIMUM_SIZE.getMessage());
    }

    @Test
    void 빈_테이블이_아닌_주문_테이블이_포함되어_있으면_테이블_그룹을_등록할_수_없음() {
        given(orderTableRepository.findAllById(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId())))
                .willReturn(Arrays.asList(주문테이블1, 주문테이블2));

        assertThatThrownBy(() -> {
           tableGroupService.create(new TableGroupRequest(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId())));
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderTableExceptionCode.NON_EMPTY_ORDER_TABLE_CANNOT_BE_INCLUDED_IN_TABLE_GROUP.getMessage());
    }

    @Test
    void 다른_테이블_그룹에_포함되어_있으면_테이블_그룹을_등록할_수_없음() {
        ReflectionTestUtils.setField(주문테이블1, "empty", new OrderEmpty(true));
        ReflectionTestUtils.setField(주문테이블2, "empty", new OrderEmpty(true));

        given(orderTableRepository.findAllById(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId())))
                .willReturn(Arrays.asList(주문테이블1, 주문테이블2));

        assertThatThrownBy(() -> {
            tableGroupService.create(new TableGroupRequest(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId())));
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderTableExceptionCode.ALREADY_INCLUDED_IN_ANOTHER_TABLE_GROUP.getMessage());
    }

    @Test
    void 테이블_그룹을_등록() {
        given(orderTableRepository.findAllById(Arrays.asList(주문테이블3.getId(), 주문테이블4.getId())))
                .willReturn(Arrays.asList(주문테이블3, 주문테이블4));
        given(tableGroupRepository.save(any(TableGroup.class))).willReturn(단체_테이블);

        TableGroupResponse response =
                tableGroupService.create(new TableGroupRequest(Arrays.asList(주문테이블3.getId(), 주문테이블4.getId())));

        assertThat(response).satisfies(res -> {
            assertEquals(단체_테이블.getId(), res.getId());
            assertEquals(단체_테이블.getOrderTables().size(), res.getOrderTables().size());
        });
    }

    @Test
    void 등록되지_않은_테이블_그룹이면_테이블_그룹을_해제할_수_없음() {
        given(tableGroupRepository.findById(단체_테이블.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> {
            tableGroupService.ungroup(단체_테이블.getId());
        }).isInstanceOf(EntityNotFoundException.class)
                .hasMessage(TableGroupExceptionCode.NOT_FOUND_BY_ID.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = { "COOKING", "MEAL" })
    void 주문_테이블_중_조리중이거나_식사중인_테이블이_포함되어_있으면_테이블_그룹을_해제할_수_없음(OrderStatus orderStatus) {
        given(tableGroupRepository.findById(단체_테이블.getId())).willReturn(Optional.of(단체_테이블));
        given(orderRepository.findAllByOrderTableIds(단체_테이블.getOrderTableIds()))
                .willReturn(Arrays.asList(new Order(주문테이블1, OrderStatus.COMPLETION), new Order(주문테이블2, orderStatus)));

        assertThatThrownBy(() -> {
            tableGroupService.ungroup(단체_테이블.getId());
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderExceptionCode.CANNOT_BE_CHANGED.getMessage());
    }

    @Test
    void 테이블_그룹을_해제() {
        given(tableGroupRepository.findById(단체_테이블.getId())).willReturn(Optional.of(단체_테이블));
        given(orderRepository.findAllByOrderTableIds(단체_테이블.getOrderTableIds()))
                .willReturn(Arrays.asList(new Order(주문테이블1, OrderStatus.COMPLETION), new Order(주문테이블2, OrderStatus.COMPLETION)));

        tableGroupService.ungroup(단체_테이블.getId());

        assertTrue(주문_테이블_목록.stream().allMatch(orderTable -> orderTable.getTableGroup() == null));
    }
}
