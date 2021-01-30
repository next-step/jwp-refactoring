package kitchenpos.order.applicatioin;

import kitchenpos.order.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

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

    private TableGroup tableGroup;
    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        orderTable1 = new OrderTable(null, 1,true);
        orderTable2 = new OrderTable(null, 2, true);
        tableGroup = new TableGroup(Arrays.asList(orderTable1.getId(), orderTable2.getId()));
    }

    @Test
    @DisplayName("단체지정 등록")
    void create() {
        when(orderTableRepository.findAllByIdIn(any())).thenReturn(Arrays.asList(orderTable1, orderTable2));
        when(tableGroupRepository.save(any())).thenReturn(tableGroup);
        when(orderTableRepository.save(any())).thenReturn(orderTable1, orderTable2);

        assertThat(tableGroupService.create(tableGroup)).isNotNull();
    }

    @Test
    @DisplayName("단체지정 등록시 주문테이블이 2개 미만이면 등록할 수 없음")
    void callExceptionCreate() {
        when(orderTableRepository.findAllByIdIn(any())).thenReturn(Arrays.asList(orderTable1));

        assertThatThrownBy(() -> {
            tableGroupService.create(tableGroup);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체지정 삭제")
    void ungroup() {
        when(orderTableRepository.findAllByTableGroupId(any()))
                .thenReturn(Arrays.asList(orderTable1, orderTable2));
        when(orderTableRepository.save(any())).thenReturn(orderTable1, orderTable2);

       tableGroupService.ungroup(tableGroup.getId());
    }

    @Test
    @DisplayName("단체지정 삭제시 주문상태가 조리, 식사일 경우 삭제할 수 없음")
    void callExceptionUngroup() {
        when(orderTableRepository.findAllByTableGroupId(any()))
                .thenReturn(Arrays.asList(orderTable1, orderTable2));
        when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(true);

        assertThatThrownBy(() -> {
            tableGroupService.ungroup(tableGroup.getId());
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
