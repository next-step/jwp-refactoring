package kitchenpos.test.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.exception.NotFoundOrderTable;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.domain.*;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.exception.NotChangeToEmptyThatCookingOrMealTable;
import kitchenpos.table.exception.NotEmptyOrExistTableGroupException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("테이블 기능 관련 테스트")
@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {
    private OrderTableRequest firstOrderTable;
    private OrderTableRequest secondOrderTable;
    private TableGroupRequest tableGroupRequest;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    @Test
    @DisplayName("주문 테이블이 하나거나 없을 때 예외")
    void isBlankOrOnlyOneTable_exception() {
        // given
        firstOrderTable = 주문_테이블_생성_요청(1L);
        tableGroupRequest = 단체_테이블_생성_요청(Arrays.asList(firstOrderTable));

        // than
        // 그룹 테이블 생성 요청 예외 발생
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("저장되어 있지 않은 테이블 주문 시 예외")
    void isNotSavedTable_exception() {
        // given
        firstOrderTable = 주문_테이블_생성_요청(1L);
        secondOrderTable = 주문_테이블_생성_요청(2L);
        tableGroupRequest = 단체_테이블_생성_요청(Arrays.asList(firstOrderTable, secondOrderTable));

        // than
        // 단체 테이블 등록 요청시 예외 발생
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(NotFoundOrderTable.class);
    }

    @Test
    @DisplayName("이미 단체 테이블에 등록되어 있는 테이블 예외")
    void isDuplicateTableGroup_exception() {
        // given
        firstOrderTable = 주문_테이블_생성_요청(1L);
        secondOrderTable = 주문_테이블_생성_요청(2L);
        tableGroupRequest = 단체_테이블_생성_요청(Arrays.asList(firstOrderTable, secondOrderTable));

        // and
        // 테이블 하나는 이미 단체 테이블에 속함
        OrderTable firstOrder = new OrderTable(1L, 10);
        TableGroup tableGroup = new TableGroup(new OrderTables(Arrays.asList(firstOrder)));
        firstOrder.changeTableGroupId(tableGroup);
        OrderTable secondOrder = new OrderTable(2L, 10);

        // when
        when(orderTableRepository.findAllByIdIn(Arrays.asList(1L, 2L))).thenReturn(new ArrayList<OrderTable>(Arrays.asList(firstOrder, secondOrder)));

        // than
        // 단체 테이블 등록 요청시 예외 발생
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(NotEmptyOrExistTableGroupException.class);
    }

    @Test
    @DisplayName("단체 테이블 정상 등록")
    void 단체_테이블_정상_등록() {
        // given
        firstOrderTable = 주문_테이블_생성_요청(1L);
        secondOrderTable = 주문_테이블_생성_요청(2L);
        tableGroupRequest = 단체_테이블_생성_요청(Arrays.asList(firstOrderTable, secondOrderTable));

        // and
        OrderTable firstOrder = new OrderTable(1L, 10);
        firstOrder.changeToEmpty();
        OrderTable secondOrder = new OrderTable(2L, 10);
        secondOrder.changeToEmpty();

        // when
        when(orderTableRepository.findAllByIdIn(Arrays.asList(1L, 2L))).thenReturn(new ArrayList<OrderTable>(Arrays.asList(firstOrder, secondOrder)));

        // and
        // 단체 테이블 등록 요청
        OrderTable mockFirstOrder = new OrderTable(1L, 10);
        firstOrder.changeToEmpty();
        OrderTable mockSecondOrder = new OrderTable(2L, 10);
        secondOrder.changeToEmpty();
        TableGroup tableGroup = new TableGroup(new OrderTables(Arrays.asList(mockFirstOrder, mockSecondOrder)));
        when(tableGroupRepository.save(any(TableGroup.class))).thenReturn(tableGroup);

        // than
        // 단체테이블 등록됨
        TableGroupResponse expected = tableGroupService.create(tableGroupRequest);
        List<OrderTableResponse> orderTables = expected.getOrderTables();
        assertThat(orderTables.stream()
                .noneMatch(orderTable -> orderTable.getEmpty())).isTrue();
    }

    @Test
    @DisplayName("요리 중 이거나 식사중인 테이블 단체 테이블 해제 예외")
    void isCookingOrMealTable_exception() {
        // given
        // not empty 주문 테이블 생성되어 있음
        OrderTable firstOrderTable = new OrderTable(1L, 10);
        OrderTable secondOrderTable = new OrderTable(2L, 10);
        when(orderTableRepository.findAllByTableGroupId(1L)).thenReturn(new ArrayList<OrderTable>(Arrays.asList(firstOrderTable, secondOrderTable)));

        // when
        // 아직 COOKING 또는 MEAL order 상태
        Menu firstMenu = new Menu(1L);
        Menu secondMenu = new Menu(2L);
        OrderLineItem firstOrderLineItem = new OrderLineItem(firstMenu, 1L);
        OrderLineItem secondOrderLineItem = new OrderLineItem(secondMenu, 1L);
        Order firstOrder = new Order(firstOrderTable, new OrderLineItems(Arrays.asList(firstOrderLineItem, secondOrderLineItem)));
        Order secondOrder = new Order(secondOrderTable, new OrderLineItems(Arrays.asList(firstOrderLineItem, secondOrderLineItem)));
        when(orderRepository.findByOrderTableIdIn(Arrays.asList(1L, 2L))).thenReturn(new ArrayList<Order>(Arrays.asList(firstOrder, secondOrder)));

        // than
        // 단체테이블 해제 오류
        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                .isInstanceOf(NotChangeToEmptyThatCookingOrMealTable.class);
    }

    @Test
    @DisplayName("단체 테이블 정상 해제")
    void 단체_테이블_정상_해제() {
        // given
        // not empty 주문 테이블 생성되어 있음
        OrderTable firstOrderTable = new OrderTable(1L, 10);
        OrderTable secondOrderTable = new OrderTable(2L, 10);
        when(orderTableRepository.findAllByTableGroupId(1L)).thenReturn(new ArrayList<OrderTable>(Arrays.asList(firstOrderTable, secondOrderTable)));

        // when
        // 아직 COOKING 또는 MEAL order 상태
        Menu firstMenu = new Menu(1L);
        Menu secondMenu = new Menu(2L);
        OrderLineItem firstOrderLineItem = new OrderLineItem(firstMenu, 1L);
        OrderLineItem secondOrderLineItem = new OrderLineItem(secondMenu, 1L);
        Order firstOrder = new Order(firstOrderTable, new OrderLineItems(Arrays.asList(firstOrderLineItem, secondOrderLineItem)));
        Order secondOrder = new Order(secondOrderTable, new OrderLineItems(Arrays.asList(firstOrderLineItem, secondOrderLineItem)));


        // and
        // COOKING 또는 MEAL 상태가 아님
        firstOrder.changeOrderStatusComplete();
        secondOrder.changeOrderStatusComplete();
        when(orderRepository.findByOrderTableIdIn(Arrays.asList(1L, 2L))).thenReturn(new ArrayList<Order>(Arrays.asList(firstOrder, secondOrder)));

        // than
        // 단체 테이블 해제
        List<OrderTableResponse> orderTables = tableGroupService.ungroup(1L);
        assertThat(orderTables.stream()
                .noneMatch(orderTable -> orderTable.getTableGroupId() != null)).isTrue();
    }


    private TableGroupRequest 단체_테이블_생성_요청(List<OrderTableRequest> orderTableRequests) {
        return new TableGroupRequest(orderTableRequests);
    }

    private OrderTableRequest 주문_테이블_생성_요청(Long id) {
        return new OrderTableRequest(id, 10);
    }
}
