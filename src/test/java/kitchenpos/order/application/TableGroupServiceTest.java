package kitchenpos.order.application;

import kitchenpos.order.domain.*;
import kitchenpos.order.application.TableGroupService;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.domain.repository.OrderTableRepository;
import kitchenpos.order.domain.repository.TableGroupRepository;
import kitchenpos.order.dto.OrderTableRequest;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DisplayName("단체 그룹 관련 기능 테스트")
@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {
    private TableGroupRequest tableGroupRequest;
    private OrderTable orderTable;
    private OrderTableRequest orderTableRequest;

    @Mock
    OrderRepository orderRepository;

    @Mock
    OrderTableRepository orderTableRepository;

    @Mock
    TableGroupRepository tableGroupRepository;

    @InjectMocks
    TableGroupService tableGroupService;

    @DisplayName("그룹화되어 있는 테이블은 그룹화 할 수 없습니다.")
    @Test
    void create_그룹화_되어있는_테이블_그룹화_예외() {

        TableGroupRequest tableGroupRequest = mock(TableGroupRequest.class);

        OrderTableRequest orderTableRequest = 주문_테이블_요청값_생성(1L,1L,2,false);
        OrderTableRequest orderTableRequest2 = 주문_테이블_요청값_생성(2L,1L,2,false);

        TableGroup tableGroup = mock(TableGroup.class);
        OrderTable orderTable = new OrderTable(1L,tableGroup, 3, true);
        OrderTable orderTable2 = new OrderTable(2L,tableGroup, 3, true);

        when(tableGroupRequest.getOrderTables()).thenReturn(Arrays.asList(orderTableRequest, orderTableRequest2));
        when(orderTableRepository.findAllById(anyList())).thenReturn(Arrays.asList(orderTable, orderTable2));

        그룹화_되어있는_테이블_그룹화_예외_발생함(tableGroupRequest);

    }

    private void 그룹화_되어있는_테이블_그룹화_예외_발생함(TableGroupRequest tableGroupRequest) {
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 그룹으로 묶여있는 경우 그룹화 할 수 없습니다.");
    }

    @DisplayName("비어있지 않은 테이블은 그룹화 할 수 없습니다.")
    @Test
    void create_비어있지_않은_테이블_그룹화_예외() {
        TableGroupRequest tableGroupRequest = mock(TableGroupRequest.class);

        OrderTableRequest orderTableRequest = 주문_테이블_요청값_생성(1L,1L,2,false);
        OrderTableRequest orderTableRequest2 = 주문_테이블_요청값_생성(2L,1L,2,false);

        TableGroup tableGroup = mock(TableGroup.class);
        OrderTable orderTable = new OrderTable(1L, tableGroup, 3, false);
        OrderTable orderTable2 = new OrderTable(2L, tableGroup, 3, false);

        when(tableGroupRequest.getOrderTables()).thenReturn(Arrays.asList(orderTableRequest, orderTableRequest2));
        when(orderTableRepository.findAllById(anyList())).thenReturn(Arrays.asList(orderTable, orderTable2));

        비어있지_않은_테이블_그룹화_예외_발생함(tableGroupRequest);
    }

    private void 비어있지_않은_테이블_그룹화_예외_발생함(TableGroupRequest tableGroupRequest) {
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("비어있지 않은 테이블은 그룹화 할 수 없습니다.");
    }

    @DisplayName("등록된 주문테이블만 그룹화 할 수 있다.")
    @Test
    void create_등록되지_않은_주문테이블_그룹화_예외() {
        TableGroupRequest tableGroupRequest = mock(TableGroupRequest.class);

        OrderTableRequest orderTableRequest = 주문_테이블_요청값_생성(1L,null,2,false);
        OrderTableRequest orderTableRequest2 = 주문_테이블_요청값_생성(2L,null,2,false);


        when(tableGroupRequest.getOrderTables()).thenReturn(Arrays.asList(orderTableRequest, orderTableRequest2));
        when(orderTableRepository.findAllById(anyList())).thenReturn(new ArrayList<>());

        등록되지_않은_주문테이블_그룹화_예외_발생함(tableGroupRequest);
    }

    private OrderTableRequest 주문_테이블_요청값_생성(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        return new OrderTableRequest(id, tableGroupId, numberOfGuests, empty);
    }

    private void 등록되지_않은_주문테이블_그룹화_예외_발생함(TableGroupRequest tableGroupRequest) {
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("등록된 주문테이블만 그룹화 할 수 있습니다");
    }

    @DisplayName("그룹테이블은 2개 이상이어야 그룹화가 가능합니다")
    @Test
    void create_주문테이블_2개_미만_예외() {
        TableGroupRequest tableGroupRequest = mock(TableGroupRequest.class);
        OrderTable orderTable = mock(OrderTable.class);

        OrderTableRequest orderTableRequest = 주문_테이블_요청값_생성(1L,null,2,false);

        when(tableGroupRequest.getOrderTables()).thenReturn(Arrays.asList(orderTableRequest));
        when(orderTableRepository.findAllById(anyList())).thenReturn(Arrays.asList(orderTable));

        주문테이블_2개_미만_예외_발생(tableGroupRequest);
    }

    private void 주문테이블_2개_미만_예외_발생(TableGroupRequest tableGroupRequest) {
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("그룹테이블은 2개 이상이어야 그룹화가 가능합니다.");
    }

    @DisplayName("주문 테이블을 그룹화 한다.")
    @Test
    void create() {
        TableGroupRequest tableGroupRequest = mock(TableGroupRequest.class);

        OrderTableRequest orderTableRequest = 주문_테이블_요청값_생성(1L,null,2,false);
        OrderTableRequest orderTableRequest2 = 주문_테이블_요청값_생성(2L,null,2,false);

        TableGroup tableGroup = mock(TableGroup.class);
        OrderTable orderTable = new OrderTable(1L,  null, 3, true);
        OrderTable orderTable2 = new OrderTable(2L, null, 3, true);

        when(tableGroupRequest.getOrderTables()).thenReturn(Arrays.asList(orderTableRequest, orderTableRequest2));
        when(orderTableRepository.findAllById(anyList())).thenReturn(Arrays.asList(orderTable, orderTable2));
        when(tableGroupRepository.save(any())).thenReturn(tableGroup);

        TableGroupResponse response = 주문테이블_그룹화_요청(tableGroupRequest);

        주문테이블_그룹화_완료(response);
    }

    private TableGroupResponse 주문테이블_그룹화_요청(TableGroupRequest tableGroupRequest) {
        return tableGroupService.create(tableGroupRequest);
    }

    private void 주문테이블_그룹화_완료(TableGroupResponse response) {
        assertThat(response).isNotNull();
    }

    @DisplayName("단체를 해제한다.")
    @Test
    void ungroup() {
        Order order = mock(Order.class);
        Order order2 = mock(Order.class);

        TableGroup tableGroup = mock(TableGroup.class);

        when(tableGroupRepository.findById(any())).thenReturn(java.util.Optional.of(tableGroup));
        when(orderRepository.findAllByOrderTableIds(anyList())).thenReturn(Arrays.asList(order, order2));

        tableGroupService.ungroup(1L);

        verify(tableGroup).ungroupOrderTables(Arrays.asList(order, order2));
    }
}