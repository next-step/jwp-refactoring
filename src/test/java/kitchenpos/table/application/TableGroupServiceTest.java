package kitchenpos.table.application;

import java.util.Optional;
import kitchenpos.exception.KitchenposException;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupRequest.OrderTableIdRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static kitchenpos.exception.KitchenposExceptionMessage.EXISTS_NOT_COMPLETION_ORDER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

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

    @DisplayName("테이블 그룹 생성 테스트")
    @Test
    void createTest() {
        // given
        TableGroup tableGroup = new TableGroup(Arrays.asList(new OrderTable(3, false),
                                                             new OrderTable(4, false)));

        TableGroupRequest request = new TableGroupRequest(Arrays.asList(new OrderTableIdRequest(1L),
                                                                        new OrderTableIdRequest(2L)));

        Mockito.when(orderTableRepository.findById(1L)).thenReturn(Optional.of(new OrderTable(3, false)));
        Mockito.when(orderTableRepository.findById(2L)).thenReturn(Optional.of(new OrderTable(4, false)));
        Mockito.when(tableGroupRepository.save(any())).thenReturn(tableGroup);

        // when
        TableGroupResponse actual = tableGroupService.create(request);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual.getOrderTables()).isNotEmpty().hasSize(2);
    }


    @DisplayName("테이블 그룹 해제 테스트")
    @Test
    void ungroupTest() {
        // given
        OrderTable orderTable1 = new OrderTable(3, false);
        OrderTable orderTable2 = new OrderTable(4, false);

        Mockito.when(tableGroupRepository.findById(1L))
               .thenReturn(Optional.of(new TableGroup(Arrays.asList(orderTable1, orderTable2))));
        Mockito.when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(false);

        // when
        tableGroupService.ungroup(1L);
    }

    @DisplayName("테이블 그룹으로 묶일 주문 테이블의 상태가 COMPLETION이 아닐 경우")
    @Test
    void ungroupTestWithWrongStatus() {
        // given
        OrderTable orderTable1 = new OrderTable(3, false);
        OrderTable orderTable2 = new OrderTable(4, false);

        Mockito.when(tableGroupRepository.findById(1L))
               .thenReturn(Optional.of(new TableGroup(Arrays.asList(orderTable1, orderTable2))));
        Mockito.when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(true);

        // when
        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
            .isInstanceOf(KitchenposException.class)
            .hasMessageContaining(EXISTS_NOT_COMPLETION_ORDER.getMessage());
    }
}
