package kitchenpos.table.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.*;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.exception.NotChangeToEmptyThatCookingOrMealTable;
import kitchenpos.table.exception.NotEmptyOrExistTableGroupException;
import kitchenpos.table.exception.NotFoundOrderTableException;
import kitchenpos.table.handler.TableGroupMapper;
import kitchenpos.table.handler.TableGroupValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    private TableGroupMapper tableGroupMapper;

    private TableGroupValidator tableGroupValidator;

    @Mock
    private TableGroupRepository tableGroupRepository;

    private TableGroupService tableGroupService;

    @BeforeEach
    void setUp() {
        tableGroupValidator = new TableGroupValidator(orderRepository, orderTableRepository);
        tableGroupService = new TableGroupService(tableGroupMapper, tableGroupValidator, tableGroupRepository);
    }

    @Test
    @DisplayName("주문 테이블이 하나거나 없을 때 예외")
    void isBlankOrOnlyOneTable_exception() {
        // given
        firstOrderTable = 주문_테이블_생성_요청(1L);
        tableGroupRequest = 단체_테이블_생성_요청(Arrays.asList(firstOrderTable));

        // when
        OrderTables orderTables = new OrderTables(Arrays.asList(new OrderTable(1L, 10)));
        TableGroup tableGroup = new TableGroup(orderTables);
        when(tableGroupMapper.mapFormToTableGroup(tableGroupRequest)).thenReturn(tableGroup);

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

        // when
        OrderTables orderTables = new OrderTables(Arrays.asList(new OrderTable(1L, 10), new OrderTable(2L, 10)));
        TableGroup tableGroup = new TableGroup(orderTables);
        when(tableGroupMapper.mapFormToTableGroup(tableGroupRequest)).thenReturn(tableGroup);
        when(orderTableRepository.findAllByIdIn(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(new OrderTable(1L, 10)));
        // than
        // 단체 테이블 등록 요청시 예외 발생
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(NotFoundOrderTableException.class);
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
        OrderTable secondOrder = new OrderTable(2L, 10);
        TableGroup tableGroup = new TableGroup(new OrderTables(Arrays.asList(firstOrder, secondOrder)));
        firstOrder.changeTableGroup(tableGroup);

        // when
        when(tableGroupMapper.mapFormToTableGroup(tableGroupRequest)).thenReturn(tableGroup);
        when(orderTableRepository.findAllByIdIn(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(firstOrder, secondOrder));

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
        TableGroup tableGroup = new TableGroup(new OrderTables(Arrays.asList(firstOrder, secondOrder)));

        // when
        when(tableGroupMapper.mapFormToTableGroup(tableGroupRequest)).thenReturn(tableGroup);
        when(orderTableRepository.findAllByIdIn(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(firstOrder, secondOrder));
        when(tableGroupRepository.save(tableGroup)).thenReturn(tableGroup);

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
        firstOrderTable.registerOrder(1L);
        OrderTable secondOrderTable = new OrderTable(2L, 10);
        secondOrderTable.registerOrder(2L);
        TableGroup tableGroup = new TableGroup(new OrderTables(Arrays.asList(firstOrderTable, secondOrderTable)));
        when(tableGroupRepository.findById(1L)).thenReturn(Optional.of(tableGroup));

        // when
        // 아직 COOKING 또는 MEAL order 상태
        OrderLineItem firstOrderLineItem = new OrderLineItem(1L, 1L);
        OrderLineItem secondOrderLineItem = new OrderLineItem(2L, 1L);
        Order firstOrder = new Order(1L, new OrderLineItems(Arrays.asList(firstOrderLineItem, secondOrderLineItem)));
        firstOrder.progressCook();
        Order secondOrder = new Order(2L, new OrderLineItems(Arrays.asList(firstOrderLineItem, secondOrderLineItem)));
        when(orderRepository.findAllByIdIn(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(firstOrder, secondOrder));

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
        firstOrderTable.registerOrder(1L);
        OrderTable secondOrderTable = new OrderTable(2L, 10);
        secondOrderTable.registerOrder(2L);
        TableGroup tableGroup = new TableGroup(new OrderTables(Arrays.asList(firstOrderTable, secondOrderTable)));
        when(tableGroupRepository.findById(1L)).thenReturn(Optional.of(tableGroup));

        // when
        // 계산 완료 상태
        OrderLineItem firstOrderLineItem = new OrderLineItem(1L, 1L);
        OrderLineItem secondOrderLineItem = new OrderLineItem(2L, 1L);
        Order firstOrder = new Order(1L, new OrderLineItems(Arrays.asList(firstOrderLineItem, secondOrderLineItem)));
        Order secondOrder = new Order(2L, new OrderLineItems(Arrays.asList(firstOrderLineItem, secondOrderLineItem)));
        firstOrder.changeOrderStatusComplete();
        secondOrder.changeOrderStatusComplete();
        when(orderRepository.findAllByIdIn(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(firstOrder, secondOrder));

        // than
        // 단체 테이블 해제
        tableGroupService.ungroup(1L);
    }


    private TableGroupRequest 단체_테이블_생성_요청(List<OrderTableRequest> orderTableRequests) {
        return new TableGroupRequest(orderTableRequests);
    }

    private OrderTableRequest 주문_테이블_생성_요청(Long id) {
        return new OrderTableRequest(id, 10);
    }
}
