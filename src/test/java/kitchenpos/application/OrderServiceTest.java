package kitchenpos.application;

import static kitchenpos.helper.MenuFixtures.메뉴_만들기;
import static kitchenpos.helper.OrderFixtures.주문_요청_만들기;
import static kitchenpos.helper.OrderLineItemFixtures.주문_항목_요청_만들기;
import static kitchenpos.helper.TableFixtures.테이블_만들기;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 관련 Service 단위 테스트 - Stub")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderLineItemDao orderLineItemDao;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private MenuRepository menuRepository;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(orderDao, orderLineItemDao, orderRepository, orderTableRepository,
                menuRepository);
    }

    @DisplayName("주문을 등록한다.")
    @Test
    void create() {
        //given
        long generateNewOrderId = 5;
        Menu menu = 메뉴_만들기(1L, "테스트 메뉴", 15000);
        OrderLineItemRequest orderLineItem1 = 주문_항목_요청_만들기(menu.getId(), 1);
        OrderLineItemRequest orderLineItem2 = 주문_항목_요청_만들기(menu.getId(), 2);
        kitchenpos.table.domain.OrderTable orderTable = 테이블_만들기(1L, 3, false);
        OrderRequest request = 주문_요청_만들기(orderTable.getId(), Arrays.asList(orderLineItem1, orderLineItem2));

        given(orderTableRepository.findById(request.getOrderTableId())).willReturn(Optional.of(orderTable));
        given(menuRepository.findById(orderLineItem1.getMenuId())).willReturn(Optional.of(menu));

        doAnswer(invocation -> {
            kitchenpos.order.domain.Order order = new kitchenpos.order.domain.Order(generateNewOrderId,
                    OrderStatus.COOKING, LocalDateTime.now(), orderTable);
            return order;
        }).when(orderRepository).save(any());

        //when
        OrderResponse result = orderService.create(request);

        //then
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(result.getOrderTableId()).isEqualTo(orderTable.getId());
    }


    @DisplayName("주문 항목이 없거나 null이면 주문을 등록 할 수 없다.")
    @Test
    void create_empty_or_null() {
        //given
        kitchenpos.table.domain.OrderTable orderTable = 테이블_만들기(1L, 3, false);
        OrderRequest emptyRequest = 주문_요청_만들기(orderTable.getId(), Collections.emptyList());
        OrderRequest nullRequest = 주문_요청_만들기(orderTable.getId(), null);

        //when then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(emptyRequest));
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(nullRequest));
    }

    @DisplayName("등록 되어있지 않은 메뉴가 있는 경우 주문을 등록 할 수 없다.")
    @Test
    void create_not_registered_menu() {
        //given
        Menu menu = 메뉴_만들기(1L, "테스트 메뉴", 15000);
        OrderLineItemRequest orderLineItem1 = 주문_항목_요청_만들기(menu.getId(), 1);
        OrderLineItemRequest orderLineItem2 = 주문_항목_요청_만들기(menu.getId(), 2);
        kitchenpos.table.domain.OrderTable orderTable = 테이블_만들기(1L, 3, false);
        OrderRequest request = 주문_요청_만들기(orderTable.getId(), Arrays.asList(orderLineItem1, orderLineItem2));

        given(orderTableRepository.findById(request.getOrderTableId())).willReturn(Optional.of(orderTable));
        given(menuRepository.findById(anyLong())).willReturn(Optional.empty());

        //when then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(request));
    }


    @DisplayName("빈 테이블인 경우 주문을 등록 할 수 없다.")
    @Test
    void create_empty_table() {
        //given
        Menu menu = 메뉴_만들기(1L, "테스트 메뉴", 15000);
        OrderLineItemRequest orderLineItem1 = 주문_항목_요청_만들기(menu.getId(), 1);
        OrderLineItemRequest orderLineItem2 = 주문_항목_요청_만들기(menu.getId(), 2);
        kitchenpos.table.domain.OrderTable orderTable = 테이블_만들기(1L, 3, true);
        OrderRequest request = 주문_요청_만들기(orderTable.getId(), Arrays.asList(orderLineItem1, orderLineItem2));

        given(orderTableRepository.findById(request.getOrderTableId())).willReturn(Optional.of(orderTable));
        given(menuRepository.findById(anyLong())).willReturn(Optional.of(menu));

        //when then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(request));
    }

    @DisplayName("주문 테이블 없는 경우 주문을 등록 할 수 없다.")
    @Test
    void create_not_exist_order_table() {
        //given
        Menu menu = 메뉴_만들기(1L, "테스트 메뉴", 15000);
        OrderLineItemRequest orderLineItem1 = 주문_항목_요청_만들기(menu.getId(), 1);
        OrderLineItemRequest orderLineItem2 = 주문_항목_요청_만들기(menu.getId(), 2);
        kitchenpos.table.domain.OrderTable orderTable = 테이블_만들기(1L, 3, true);
        OrderRequest request = 주문_요청_만들기(orderTable.getId(), Arrays.asList(orderLineItem1, orderLineItem2));

        given(orderTableRepository.findById(request.getOrderTableId())).willReturn(Optional.empty());

        //when then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(request));
    }

    @DisplayName("주문 상태를 업데이트 한다.")
    @Test
    void changeOrderStatus() {
        //given
        long requestOrderId = 1;
        Order request = new Order(null, null, OrderStatus.MEAL.name(), null, null);
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L, 1L, 10);
        OrderLineItem orderLineItem2 = new OrderLineItem(2L, 1L, 3L, 5);

        given(orderDao.findById(requestOrderId))
                .willReturn(Optional.of(new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), null)));
        given(orderLineItemDao.findAllByOrderId(requestOrderId))
                .willReturn(Arrays.asList(orderLineItem1, orderLineItem2));

        //when
        Order result = orderService.changeOrderStatus(requestOrderId, request);

        //then
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }


    @DisplayName("주문 상태가 계산완료인 경우 주문 상태를 업데이트 할 수 없다.")
    @Test
    void changeOrderStatus_completion() {
        //given
        long requestOrderId = 1;
        Order request = new Order(null, null, OrderStatus.MEAL.name(), null, null);

        given(orderDao.findById(requestOrderId))
                .willReturn(Optional.of(new Order(1L, 1L, OrderStatus.COMPLETION.name(), LocalDateTime.now(), null)));

        //when then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.changeOrderStatus(requestOrderId, request));
    }


    @DisplayName("주문 목록을 조회한다.")
    @Test
    void list() {
        //given
        OrderLineItem orderLineItem1 = new OrderLineItem(null, null, 1L, 10);
        OrderLineItem orderLineItem2 = new OrderLineItem(null, null, 3L, 5);
        OrderLineItem orderLineItem3 = new OrderLineItem(null, null, 1L, 10);
        Order order1 = new Order(1L, 1L, OrderStatus.MEAL.name(), LocalDateTime.now(), null);
        Order order2 = new Order(2L, 2L, OrderStatus.COMPLETION.name(), LocalDateTime.now(), null);

        given(orderDao.findAll()).willReturn(Arrays.asList(order1, order2));
        given(orderLineItemDao.findAllByOrderId(order1.getId()))
                .willReturn(Arrays.asList(orderLineItem1, orderLineItem2));
        given(orderLineItemDao.findAllByOrderId(order2.getId())).willReturn(Arrays.asList(orderLineItem3));

        //when
        List<Order> results = orderService.list();

        //then
        assertThat(results.get(0).getOrderLineItems()).hasSize(2);
        assertThat(results.get(1).getOrderLineItems()).hasSize(1);
    }

}
