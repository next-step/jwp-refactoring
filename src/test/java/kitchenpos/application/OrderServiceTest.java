package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.*;

import kitchenpos.fixture.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @Mock
    private MenuDao menuDao;
    @Mock
    private OrderLineItemDao orderLineItemDao;

    @InjectMocks
    private OrderService orderService;


    private MenuGroup 분식류;
    private Product 진매;
    private Product 진순이;
    private MenuProduct 메뉴_진매;
    private MenuProduct 메뉴_진순이;
    private Menu 메뉴;
    private Order 주문;
    private OrderTable 주문_테이블;
    private OrderLineItem 주문_메뉴;

    @BeforeEach
    void setUp() {
        분식류 = TestMenuGroupFactory.create("분식류");

        진매 = TestProductFactory.create(1L, "진라면 매운맛", 5_000);
        진순이 = TestProductFactory.create(2L, "진라면 순한맛", 5_000);

        메뉴 = TestMenuFactory.create(10L, 4_000, 분식류, "라면메뉴");

        메뉴_진매 = TestMenuProductFactory.create(메뉴, 진매, 1);
        메뉴_진순이 = TestMenuProductFactory.create(메뉴, 진매, 1);

        메뉴.setMenuProducts(Arrays.asList(메뉴_진매, 메뉴_진순이));

        주문_테이블 = TestOrderTableFactory.create(1L);
        주문 = TestOrderFactory.create(1L);

        주문_메뉴 = TestOrderLineItemFactory.create(주문, 메뉴, 3);
    }

    @DisplayName("주문을 등록할 수 있다")
    @Test
    void create() throws Exception {
        // given
        주문.setOrderStatus(OrderStatus.COOKING.toString());
        주문.setOrderedTime(LocalDateTime.now());
        주문.setOrderTableId(주문_테이블.getId());
        주문.setOrderLineItems(Collections.singletonList(주문_메뉴));
        given(menuDao.countByIdIn(any())).willReturn(1L);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(주문_테이블));
        given(orderLineItemDao.save(any(OrderLineItem.class))).willReturn(주문_메뉴);
        given(orderDao.save(any(Order.class))).willReturn(주문);

        // when
        Order order = orderService.create(주문);

        // then
        assertThat(order).isEqualTo(주문);
    }

    @DisplayName("주문 항목이 비어있으면 등록할 수 없다")
    @Test
    void createException1() throws Exception {
        // given
        주문.setOrderStatus(OrderStatus.COOKING.toString());
        주문.setOrderedTime(LocalDateTime.now());
        주문.setOrderTableId(주문_테이블.getId());

        // when & then
        assertThatThrownBy(() -> orderService.create(주문)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 항목의 메뉴가 존재하지 않으면 등록할 수 없다")
    @Test
    void createException2() throws Exception {
        // given
        주문.setOrderStatus(OrderStatus.COOKING.toString());
        주문.setOrderedTime(LocalDateTime.now());
        주문.setOrderTableId(주문_테이블.getId());
        주문.setOrderLineItems(Collections.singletonList(주문_메뉴));

        given(menuDao.countByIdIn(any())).willReturn(0L);

        // when & then
        assertThatThrownBy(() -> orderService.create(주문)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 항목의 주문테이블이 존재하지 않으면 등록할 수 없다")
    @Test
    void createException3() throws Exception {
        // given
        주문.setOrderStatus(OrderStatus.COOKING.toString());
        주문.setOrderedTime(LocalDateTime.now());
        주문.setOrderLineItems(Collections.singletonList(주문_메뉴));

        given(menuDao.countByIdIn(any())).willReturn(1L);

        // when & then
        assertThatThrownBy(() -> orderService.create(주문)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 항목의 주문테이블이 존재하지 않으면 등록할 수 없다")
    @Test
    void createException4() throws Exception {
        // given
        주문.setOrderStatus(OrderStatus.COOKING.toString());
        주문.setOrderedTime(LocalDateTime.now());
        주문.setOrderLineItems(Collections.singletonList(주문_메뉴));
        주문.setOrderTableId(99L);
        given(menuDao.countByIdIn(any())).willReturn(1L);
        given(orderTableDao.findById(anyLong())).willThrow(IllegalArgumentException.class);

        // when & then
        assertThatThrownBy(() -> orderService.create(주문)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("전체 주문목록을 조회할 수 있다")
    @Test
    void list() throws Exception {
        // given
        given(orderDao.findAll()).willReturn(Collections.singletonList(주문));

        // when
        List<Order> list = orderService.list();

        // then
        assertThat(list).containsExactly(주문);
    }

    @DisplayName("주문상태를 변경한다")
    @ParameterizedTest
    @CsvSource(
            value = {
                    "MEAL/MEAL", "MEAL/COOKING", "MEAL/COMPLETION",
                    "COOKING/MEAL", "COOKING/COOKING", "COOKING/COMPLETION",
            }, delimiter = '/')
    void change(OrderStatus baseOrderStatus, OrderStatus newOrderStatus) throws Exception {
        // given
        주문.setOrderStatus(baseOrderStatus.toString());
        Order order = new Order();
        order.setOrderStatus(newOrderStatus.toString());

        given(orderDao.findById(anyLong())).willReturn(Optional.of(주문));

        // when
        Order changedOrder = orderService.changeOrderStatus(1L, order);

        // then
        assertThat(changedOrder.getOrderStatus()).isEqualTo(newOrderStatus.toString());
    }

    @DisplayName("주문상태가 COMPLETION 이면 상태를 변경할 수 없다")
    @ParameterizedTest
    @CsvSource({"MEAL", "COOKING", "COMPLETION"})
    void changeException(OrderStatus orderStatus) throws Exception {
        // given
        주문.setOrderStatus(OrderStatus.COMPLETION.toString());
        Order order = new Order();
        order.setOrderStatus(orderStatus.toString());

        given(orderDao.findById(anyLong())).willReturn(Optional.of(주문));

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, order))
                .isInstanceOf(IllegalArgumentException.class);
    }


}
