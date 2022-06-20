package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static kitchenpos.fixture.OrderFixture.*;
import static kitchenpos.fixture.OrderLineItemFixture.주문항목_데이터_생성;
import static kitchenpos.fixture.OrderLineItemFixture.주문항목_데이터_확인;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private OrderService orderService;

    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void create() {
        //given
        OrderLineItem orderLineItem = 주문항목_데이터_생성();
        List<OrderLineItem> requestOrderLineItems = Arrays.asList(orderLineItem);
        Order request = 주문_요청_데이터_생성(requestOrderLineItems);

        given(menuDao.countByIdIn(anyList())).willReturn(1L);
        given(orderTableDao.findById(any())).willReturn(Optional.of(new OrderTable(1L, 1L, 4, false)));
        given(orderDao.save(any())).willReturn(new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), requestOrderLineItems));
        given(orderLineItemDao.save(any())).willReturn(orderLineItem);

        //when
        Order order = orderService.create(request);

        //then
        주문_데이터_확인(order, 1L, 1L, OrderStatus.COOKING);
        주문항목_데이터_확인(order.getOrderLineItems().get(0), 1L, 1L, 1L, 1);
    }

    @DisplayName("주문항목이 하나도 없으면 생성할 수 없다.")
    @Test
    void create_fail_menuNotExists() {
        //given
        List<OrderLineItem> requestOrderLineItems = Collections.emptyList();
        Order request = 주문_요청_데이터_생성(requestOrderLineItems);

        //when //then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(request));
    }

    @DisplayName("주문항목과 메뉴 수가 다르면 생성할 수 없다.")
    @Test
    void create_fail_notEqualsMenuSize() {
        //given
        OrderLineItem orderLineItem = 주문항목_데이터_생성();
        List<OrderLineItem> requestOrderLineItems = Arrays.asList(orderLineItem);
        Order request = 주문_요청_데이터_생성(requestOrderLineItems);

        given(menuDao.countByIdIn(anyList())).willReturn(2L);

        //when //then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(request));
    }

    @DisplayName("주문테이블이 존재하지 않으면 생성할 수 없다.")
    @Test
    void create_fail_notExistsOrderTable() {
        //given
        OrderLineItem orderLineItem = 주문항목_데이터_생성();
        List<OrderLineItem> requestOrderLineItems = Arrays.asList(orderLineItem);
        Order request = 주문_요청_데이터_생성(requestOrderLineItems);

        given(menuDao.countByIdIn(anyList())).willReturn(1L);
        given(orderTableDao.findById(any())).willReturn(Optional.empty());

        //when //then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(request));
    }

    @DisplayName("주문테이블이 빈테이블이면 생성할 수 없다.")
    @Test
    void create_fail_emptyOrderTable() {
        //given
        OrderLineItem orderLineItem = 주문항목_데이터_생성();
        List<OrderLineItem> requestOrderLineItems = Arrays.asList(orderLineItem);
        Order request = 주문_요청_데이터_생성(requestOrderLineItems);

        given(menuDao.countByIdIn(anyList())).willReturn(1L);
        given(orderTableDao.findById(any())).willReturn(Optional.of(new OrderTable(1L, 1L, 1, true)));

        //when //then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(request));
    }

    @DisplayName("주문과 주문항목을 전체 조회한다.")
    @Test
    void list() {
        //given
        OrderLineItem orderLineItem = 주문항목_데이터_생성();
        List<OrderLineItem> requestOrderLineItems = Arrays.asList(orderLineItem);

        Order order = new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), requestOrderLineItems);
        given(orderDao.findAll()).willReturn(Arrays.asList(order));

        given(orderLineItemDao.findAllByOrderId(any())).willReturn(Arrays.asList(orderLineItem));

        //when
        List<Order> list = orderService.list();

        //then
        assertEquals(1, list.size());
    }

    @DisplayName("주문상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        //given
        Order requestOrder = 주문수정_요청_데이터_생성(OrderStatus.COOKING.name());

        OrderLineItem orderLineItem = 주문항목_데이터_생성();
        List<OrderLineItem> requestOrderLineItems = Arrays.asList(orderLineItem);
        given(orderDao.findById(any())).willReturn(
                Optional.of(주문_데이터_생성(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), requestOrderLineItems)));
        given(orderLineItemDao.findAllByOrderId(any())).willReturn(requestOrderLineItems);

        //when
        Order order = orderService.changeOrderStatus(1L, requestOrder);

        //then
        주문_데이터_확인(order, 1L, 1L, OrderStatus.COOKING);
    }

    @DisplayName("해당 주문 id가 없는경우, 상태를 변경할 수 없다.")
    @Test
    void changeOrderStatus_fail_notExistsOrder() {
        //given
        Order requestOrder = 주문수정_요청_데이터_생성(OrderStatus.COOKING.name());

        given(orderDao.findById(any())).willReturn(Optional.empty());

        //when //then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.changeOrderStatus(1L, requestOrder));
    }

    @DisplayName("주문상태가 계산완료인 경우, 상태를 변경할 수 없다")
    @Test
    void changeOrderStatus_fail_statusComplete() {
        List<OrderLineItem> orderLineItems = Arrays.asList(주문항목_데이터_생성());

        given(orderDao.findById(any())).willReturn(
                Optional.of(주문_데이터_생성(1L, 1L, OrderStatus.COMPLETION.name(), LocalDateTime.now(), orderLineItems)));

        //when //then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.changeOrderStatus(1L, 주문수정_요청_데이터_생성(OrderStatus.COOKING.name())));
    }

}