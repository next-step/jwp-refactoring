package kitchenpos.unit;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.order.domain.type.OrderStatus;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.table.port.OrderTablePort;
import kitchenpos.tablegroup.port.TableGroupPort;
import kitchenpos.tablegroup.validator.TableGroupValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.common.constants.ErrorCodeType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private OrderTablePort orderTablePort;
    @Mock
    private TableGroupPort tableGroupPort;

    @Mock
    private TableGroupValidator tableGroupValidator;
    @InjectMocks
    private TableGroupService tableGroupService;

    private OrderTable 주문테이블_일번;
    private OrderTable 주문테이블_이번;
    private TableGroup 단체지정;

    private List<OrderTable> 주문테이블_리스트;

    @BeforeEach
    void setUp() {
        주문테이블_일번 = new OrderTable(1L, null, 3, true);
        주문테이블_이번 = new OrderTable(2L, null, 7, true);

        주문테이블_리스트 = Arrays.asList(주문테이블_일번, 주문테이블_이번);
        단체지정 = new TableGroup();
    }

    @Test
    @DisplayName("단체지정을 등록 할 수 있다.")
    void createTableGroup() {
        doNothing().when(tableGroupValidator).validOrderTableIds(any());

        given(orderTablePort.findAllByTableGroupIdIn(
                Arrays.asList(주문테이블_일번.getId(), 주문테이블_이번.getId()))
        ).willReturn(주문테이블_리스트);

        given(tableGroupPort.save(any())).willReturn(단체지정);

        TableGroupResponse result =
                tableGroupService.create(new TableGroupRequest(Arrays.asList(1L, 2L)));

        assertThat(result.getId()).isEqualTo(단체지정.getId());
    }

    @Test
    @DisplayName("주문 테이블이 비어있어야만 단체지정이 가능한다.")
    void emptyOrderTableAddTableGroup() {
        doThrow(new IllegalArgumentException(NOT_FOUND_ORDER_TABLE.getMessage()))
                .when(tableGroupValidator).validOrderTableIds(any());

        assertThatThrownBy(() ->
                tableGroupService.create(new TableGroupRequest(Arrays.asList(1L, 2L, 3L)))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 2개 이여야만 단채 지정이 가능하다.")
    void tableGroupsSizeMinTwo() {
        doThrow(new IllegalArgumentException(ORDER_TABLE_MIN_SIZE_ERROR.getMessage()))
                .when(tableGroupValidator).validOrderTableIds(any());

        assertThatThrownBy(() ->
                tableGroupService.create(new TableGroupRequest(Arrays.asList(1L))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정 테이블은 주문 테이블이여야한다.")
    void tableGroupIsOrderTable() {
        doThrow(new IllegalArgumentException(MATCH_GROUP_PRESENT.getMessage()))
                .when(tableGroupValidator).validOrderTableIds(any());

        assertThatThrownBy(() ->
                tableGroupService.create(new TableGroupRequest(Arrays.asList(1L, 2L)))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 이미 단체지정 되어있으면(이용중) 등록 할 수 없다.")
    void tableGroupIsAlreadyUseFail() {
        doThrow(new IllegalArgumentException(NOT_MATCH_ORDER_TABLE.getMessage()))
                .when(tableGroupValidator).validOrderTableIds(any());

        assertThatThrownBy(() ->
                tableGroupService.create(new TableGroupRequest(Arrays.asList(1L, 2L)))
        ).isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    @DisplayName("단체지정 등록 취소 할 수 있다.")
    void cancelTableGroup() {
        given(tableGroupPort.findById(1L)).willReturn(단체지정);

        tableGroupService.ungroup(1L);

        assertThat(주문테이블_일번.getTableGroupId()).isNull();
        assertThat(주문테이블_이번.getTableGroupId()).isNull();
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    @DisplayName("주문 테이블이 이미 조리중이거나 식사중이면 취소가 불가능하다")
    void cancelTableGroupIfCookingAndMealFail(OrderStatus status) {
        given(tableGroupPort.findById(any())).willReturn(단체지정);
        doThrow(new IllegalArgumentException(COOKING_MEAL_NOT_UNGROUP.getMessage()))
                .when(tableGroupValidator).validCheckUngroup(any());

        assertThatThrownBy(() ->
                tableGroupService.ungroup(1L)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
