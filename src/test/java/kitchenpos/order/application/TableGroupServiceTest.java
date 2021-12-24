package kitchenpos.order.application;

import kitchenpos.order.application.exception.InvalidOrderState;
import kitchenpos.order.application.exception.InvalidTableState;
import kitchenpos.order.application.exception.TableNotFoundException;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
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
import java.util.Optional;

import static kitchenpos.order.domain.OrderStatus.COMPLETION;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("테이블 그룹 서비스")
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

    private TableGroup 테이블그룹;
    private OrderTable 테이블1;
    private OrderTable 테이블2;

    @BeforeEach
    void setUp() {
        테이블1 = new OrderTable(1L, 3, new TableState(false));
        테이블2 = new OrderTable(2L, 3, new TableState(false));
        테이블그룹 = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(테이블1, 테이블2));
    }

    @Test
    @DisplayName("테이블 그룹을 등록한다.")
    void create() {
        TableGroupRequest request = new TableGroupRequest(Arrays.asList(테이블1.getId(), 테이블2.getId()));

        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(테이블1));
        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(테이블1));
        when(tableGroupRepository.save(any())).thenReturn(테이블그룹);

        TableGroupResponse response = tableGroupService.create(request);

        verify(orderTableRepository, times(2)).findById(any());
        verify(tableGroupRepository, times(1)).save(any(TableGroup.class));
        assertThat(response.getOrderTableResponses()).hasSize(2);
    }

    @Test
    @DisplayName("주문 테이블 목록이 2개 미만인 경우 예외가 발생한다.")
    void validateOrderTableSize() {
        TableGroupRequest 테이블1개그룹 = new TableGroupRequest(Collections.singletonList(테이블1.getId()));

        assertThatThrownBy(() -> tableGroupService.create(테이블1개그룹))
                .isInstanceOf(TableNotFoundException.class);
    }

    @Test
    @DisplayName("주문 테이블이 등록되어 있지 않은 경우 예외가 발생한다.")
    void validateOrderTable() {
        TableGroupRequest request = new TableGroupRequest(Arrays.asList(테이블1.getId(), 테이블2.getId()));
        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(테이블1));
        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(TableNotFoundException.class);
        verify(orderTableRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("주문 테이블이 빈 테이블인 경우 예외가 발생한다.")
    void validateOrderTableEmpty() {
        OrderTable 빈테이블 = new OrderTable(3L, 0, new TableState(true));
        TableGroupRequest request = new TableGroupRequest(Arrays.asList(테이블2.getId(), 빈테이블.getId()));

        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(테이블1));
        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(빈테이블));

        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(InvalidTableState.class);
        verify(orderTableRepository, times(2)).findById(anyLong());
    }

    @Test
    @DisplayName("다른 테이블 그룹에 등록되어 있는 주문 테이블인 경우 예외가 발생한다.")
    void validateExistTableGroup() {
        TableGroupRequest request = new TableGroupRequest(Arrays.asList(테이블1.getId(), 테이블2.getId()));
        테이블2.setTableGroup(new TableGroup(LocalDateTime.now(), Arrays.asList(테이블1, 테이블2)));

        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(테이블1));
        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(테이블2));

        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(InvalidTableState.class);
        verify(orderTableRepository, times(2)).findById(anyLong());
    }

    @Test
    @DisplayName("테이블 그룹을 해제한다.")
    void ungroup() {
        테이블1.changeStatus(COMPLETION);
        테이블2.changeStatus(COMPLETION);
        when(tableGroupRepository.findById(anyLong())).thenReturn(Optional.of(테이블그룹));

        tableGroupService.ungroup(테이블그룹.getId());

        verify(tableGroupRepository, times(1)).findById(anyLong());
        assertThat(테이블1.getTableGroup()).isNull();
        assertThat(테이블2.getTableGroup()).isNull();
    }

    @Test
    @DisplayName("모든 테이블의 주문 상태가 계산 완료가 아닌 경우 예외가 발생한다.")
    void validateStatusUngroup() {
        when(tableGroupRepository.findById(anyLong())).thenReturn(Optional.of(테이블그룹));

        assertThatThrownBy(() -> tableGroupService.ungroup(테이블그룹.getId()))
                .isInstanceOf(InvalidOrderState.class);
        verify(tableGroupRepository, times(1)).findById(anyLong());
    }
}