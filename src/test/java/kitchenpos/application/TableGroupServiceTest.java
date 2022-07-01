package kitchenpos.application;


import kitchenpos.domain.*;
import org.junit.jupiter.api.Assertions;
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

import static kitchenpos.domain.OrderTableTest.주문_태이블_생성;
import static kitchenpos.domain.TableGroupTest.단체_지정_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private TableGroupRepository tableGroupRepository;
    @InjectMocks
    private TableGroupService tableGroupService;

    @DisplayName("단체 지정을 생성한다.")
    @Test
    void create() {
        // given
        OrderTable orderTable1 = 주문_태이블_생성(1L, null, 1, true);
        OrderTable orderTable2 = 주문_태이블_생성(2L, null, 1, true);
        TableGroup tableGroup = 단체_지정_생성(null, Arrays.asList(orderTable1, orderTable2));

        when(orderTableRepository.findAllByIdIn(any())).thenReturn(Arrays.asList(orderTable1, orderTable2));
        when(tableGroupRepository.save(tableGroup)).thenReturn(
                단체_지정_생성(1L, LocalDateTime.now(), Arrays.asList(orderTable1, orderTable2))
        );

        // when
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // then
        Assertions.assertAll(
                () -> assertThat(savedTableGroup.getId()).isNotNull(),
                () -> assertThat(savedTableGroup.getCreatedDate()).isNotNull(),
                () -> assertThat(savedTableGroup.getOrderTables()).containsExactly(orderTable1, orderTable2)
        );
    }

    @DisplayName("주문 테이블이 2개 이상이여야 한다.")
    @Test
    void create1() {
        // given
        List<OrderTable> orderTables = Collections.singletonList(주문_태이블_생성(1L, null, 1, true));
        TableGroup tableGroup = 단체_지정_생성(null, orderTables);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("요청한 주문 테이블수와 조회한 테이블 수가 일치해야한다.")
    @Test
    void create2() {
        // given
        OrderTable orderTable1 = 주문_태이블_생성(1L, null, 1, true);
        OrderTable orderTable2 = 주문_태이블_생성(2L, null, 1, true);
        TableGroup tableGroup = 단체_지정_생성(null, Arrays.asList(orderTable1, orderTable2));

        when(orderTableRepository.findAllByIdIn(any())).thenReturn(Collections.singletonList(orderTable1));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블은 비여있는 상태여야한다.")
    @Test
    void create3() {
        // given
        OrderTable orderTable1 = 주문_태이블_생성(1L, null, 1, false);
        OrderTable orderTable2 = 주문_태이블_생성(2L, null, 1, false);
        TableGroup tableGroup = 단체_지정_생성(null, Arrays.asList(orderTable1, orderTable2));

        when(orderTableRepository.findAllByIdIn(any())).thenReturn(Arrays.asList(orderTable1, orderTable2));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블은 단체 지정이 되어 있으면 안된다.")
    @Test
    void create4() {
        // given
        TableGroup tableGroup = 단체_지정_생성(2L, LocalDateTime.now(), null);
        OrderTable orderTable1 = 주문_태이블_생성(1L, tableGroup, 1, false);
        OrderTable orderTable2 = 주문_태이블_생성(2L, tableGroup, 1, false);
        tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));

        when(orderTableRepository.findAllByIdIn(any())).thenReturn(Arrays.asList(orderTable1, orderTable2));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 해체한다.")
    @Test
    void ungroup() {
        // given
        Long tableGroupId = 1L;
        TableGroup tableGroup = 단체_지정_생성(tableGroupId, LocalDateTime.now(), null);
        OrderTable orderTable1 = 주문_태이블_생성(1L, tableGroup, 1, false);
        OrderTable orderTable2 = 주문_태이블_생성(2L, tableGroup, 1, false);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);

        when(orderTableRepository.findAllByTableGroupId(tableGroupId)).thenReturn(orderTables);
        when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(false);

        // when, then
        tableGroupService.ungroup(tableGroupId);
    }

    @DisplayName("'조리', '식사' 상태 주문 존재하면 안된다.")
    @Test
    void ungroup1() {
        Long tableGroupId = 1L;
        TableGroup tableGroup = 단체_지정_생성(tableGroupId, LocalDateTime.now(), null);
        OrderTable orderTable1 = 주문_태이블_생성(1L, tableGroup, 1, false);
        OrderTable orderTable2 = 주문_태이블_생성(2L, tableGroup, 1, false);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);

        when(orderTableRepository.findAllByTableGroupId(tableGroupId)).thenReturn(orderTables);
        when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(true);

        // when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
