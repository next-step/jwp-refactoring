package kitchenpos.application;

import static kitchenpos.domain.MenuGroupTest.메뉴그룹_생성;
import static kitchenpos.domain.MenuProductTest.메뉴상품_생성;
import static kitchenpos.domain.MenuTest.메뉴_생성;
import static kitchenpos.domain.OrderLineItemTest.주문_항목_생성;
import static kitchenpos.domain.OrderTableTest.주문_테이블_생성;
import static kitchenpos.domain.OrderTest.주문_생성;
import static kitchenpos.domain.ProductTest.상품_생성;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private OrderService orderService;

    private Product 미역국;
    private Product 소머리국밥;
    private MenuGroup 식사;
    private MenuProduct 미역국_메뉴상품;
    private MenuProduct 소머리국밥_메뉴상품;
    private Menu 미역국_메뉴;
    private Menu 소머리국밥_메뉴;
    private OrderLineItem 주문_항목;
    private OrderTable 주문_테이블;
    private Order 주문;

    @BeforeEach
    public void setUp() {
        미역국 = 상품_생성(1L, "미역국", BigDecimal.valueOf(6000));
        소머리국밥 = 상품_생성(2L, "소머리국밥", BigDecimal.valueOf(8000));
        식사 = 메뉴그룹_생성(1L, "식사");

        미역국_메뉴상품 = 메뉴상품_생성(null, 미역국.getId(), 미역국.getId(), 1L);
        미역국_메뉴 = 메뉴_생성(1L, "미역국", BigDecimal.valueOf(6000), 식사.getId(), Arrays.asList(미역국_메뉴상품));

        소머리국밥_메뉴상품 = 메뉴상품_생성(null, 소머리국밥.getId(), 소머리국밥.getId(), 1L);
        소머리국밥_메뉴 = 메뉴_생성(2L, "소머리국밥", BigDecimal.valueOf(8000), 식사.getId(), Arrays.asList(소머리국밥_메뉴상품));

        주문_항목 = 주문_항목_생성(1L, null, 소머리국밥.getId(), 2);
        주문_테이블 = 주문_테이블_생성(1L, null, 2, false);

        주문 = 주문_생성(1L, 주문_테이블.getId(), null, null, Arrays.asList(주문_항목));
    }

    @Test
    @DisplayName("주문 등록")
    void create() {
        // given
        when(menuDao.countByIdIn(anyList())).thenReturn(1L);
        when(orderTableDao.findById(any())).thenReturn(Optional.of(주문_테이블));
        when(orderDao.save(any(Order.class))).thenReturn(주문);
        when(orderLineItemDao.save(any(OrderLineItem.class))).thenReturn(주문_항목);

        // when
        Order 등록된_주문 = orderService.create(주문);

        // then
        assertThat(등록된_주문).isEqualTo(주문);
    }

    @Test
    @DisplayName("주문 목록 조회")
    void list() {
        // given
        when(orderDao.findAll()).thenReturn(Arrays.asList(주문));
        when(orderLineItemDao.findAllByOrderId(any())).thenReturn(Arrays.asList(주문_항목));

        // when
        List<Order> orders = orderService.list();

        // then
        assertThat(orders).hasSize(1);
        assertThat(orders).contains(주문);
    }

    @Test
    @DisplayName("주문 상태 변경")
    void changeOrderStatus() {
        // given
        when(orderDao.findById(any())).thenReturn(Optional.of(주문));
        when(orderDao.save(any(Order.class))).thenReturn(주문);
        when(orderLineItemDao.findAllByOrderId(any())).thenReturn(Arrays.asList(주문_항목));
        Order 상태_변경_주문 = 주문_생성(null, null, OrderStatus.MEAL.name(),null, null);

        // when
        orderService.changeOrderStatus(주문.getId(), 상태_변경_주문);

        // then
        assertThat(주문.getOrderStatus()).isEqualTo(상태_변경_주문.getOrderStatus());
    }
}