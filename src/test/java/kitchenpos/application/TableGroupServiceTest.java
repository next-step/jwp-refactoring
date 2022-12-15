package kitchenpos.application;

import kitchenpos.port.OrderPort;
import kitchenpos.port.OrderTablePort;
import kitchenpos.port.TableGroupPort;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private OrderPort orderPort;

    @Mock
    private OrderTablePort orderTablePort;

    @Mock
    private TableGroupPort tableGroupPort;

    @InjectMocks
    private TableGroupService tableGroupService;

    private OrderTable 주문테이블_일번;
    private OrderTable 주문테이블_이번;
    private List<OrderTable> 주문테이블_리스트;

    @BeforeEach
    void setUp() {
        주문테이블_일번 = new OrderTable(1L, null, 3, true);
        주문테이블_이번 = new OrderTable(2L, null, 7, true);

        주문테이블_리스트 = Arrays.asList(주문테이블_일번, 주문테이블_이번);
    }

    @Test
    @DisplayName("단체지정을 등록 할 수 있다.")
    void createTableGroup() {
        TableGroup 단체지정 = new TableGroup(1L, LocalDateTime.now(), 주문테이블_리스트);

        when(orderTablePort.findAllByIdIn(Arrays.asList(주문테이블_일번.getId(), 주문테이블_이번.getId()))).thenReturn(주문테이블_리스트);
        when(tableGroupPort.save(단체지정)).thenReturn(단체지정);

        TableGroup savedTableGroup = tableGroupService.create(단체지정);

        assertThat(savedTableGroup.getId()).isNotNull();
        assertThat(savedTableGroup.getOrderTables()).containsAll(주문테이블_리스트);
    }

    @Test
    @DisplayName("주문 테이블이 비어있어야만 단체지정이 가능한다.")
    void emptyOrderTableAddTableGroup() {
        TableGroup 단체지정 = new TableGroup(1L, LocalDateTime.now(), 주문테이블_리스트);

        assertThatThrownBy(() ->
                tableGroupService.create(단체지정)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 2개 이여야만 단채 지정이 가능하다.")
    void tableGroupsSizeMinTwo() {
        TableGroup 단체지정 = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(주문테이블_일번));

        assertThatThrownBy(() ->
                tableGroupService.create(단체지정)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정 테이블은 주문 테이블이여야한다.")
    void tableGroupIsOrderTable() {
        TableGroup 단체지정 = new TableGroup(1L, LocalDateTime.now(), 주문테이블_리스트);

        when(orderTablePort.findAllByIdIn(anyList())).thenReturn(Collections.emptyList());

        assertThatThrownBy(() ->
                tableGroupService.create(단체지정)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 이미 단체지정 되어있으면(이용중) 등록 할 수 없다.")
    void tableGroupIsAlreadyUseFail() {
        주문테이블_일번.setEmpty(false);
        TableGroup 단체지정 = new TableGroup(1L, LocalDateTime.now(), 주문테이블_리스트);

        when(orderTablePort.findAllByIdIn(Arrays.asList(1L, 2L))).thenReturn(주문테이블_리스트);

        assertThatThrownBy(() ->
                tableGroupService.create(단체지정)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체지정 등록 취소 할 수 있다.")
    void cancelTableGroup() {
        when(orderTablePort.findAllByTableGroupId(1L)).thenReturn(주문테이블_리스트);
        when(orderPort.existsByOrderTableIdInAndOrderStatusIn(any(), anyList())).thenReturn(false);

        tableGroupService.ungroup(1L);

        assertThat(주문테이블_일번.getTableGroupId()).isNull();
        assertThat(주문테이블_이번.getTableGroupId()).isNull();
    }

    @Test
    @DisplayName("주문 테이블이 이미 조리중이거나 식사중이면 취소가 불가능하다")
    void cancelTableGroupIfCookingAndMealFail() {
        when(orderTablePort.findAllByTableGroupId(any())).thenReturn(주문테이블_리스트);
        when(orderPort.existsByOrderTableIdInAndOrderStatusIn(any(), anyList())).thenReturn(true);

        assertThatThrownBy(() ->
                tableGroupService.ungroup(1L)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
