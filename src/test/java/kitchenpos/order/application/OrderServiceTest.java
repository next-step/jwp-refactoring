package kitchenpos.order.application;

import static kitchenpos.menu.application.MenuServiceTestHelper.*;
import static kitchenpos.menugroup.application.MenuGroupServiceTestHelper.*;
import static kitchenpos.order.application.OrderServiceTestHelper.*;
import static kitchenpos.product.application.ProductServiceTestHelper.*;
import static kitchenpos.table.application.TableServiceTestHelper.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.application.OrderService;
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

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    MenuDao menuDao;
    @Mock
    OrderDao orderDao;
    @Mock
    OrderLineItemDao orderLineItemDao;
    @Mock
    OrderTableDao orderTableDao;

    @InjectMocks
    OrderService orderService;

    private MenuGroup 분식;
    private Product 제육;
    private MenuProduct 제육볶음;
    private Menu 제육볶음_메뉴;

    private OrderTable 좌석1;

    @BeforeEach
    void setUp() {
        // given
        분식 = 메뉴_그룹_생성(1L, "분식");
        제육 = 상품_생성(1L, "제육", 8_900);
        제육볶음 = 메뉴_상품_생성(1L, 10);
        제육볶음_메뉴 = 메뉴_생성("제육볶음", 8_000, 분식.getId(), 제육볶음);

        좌석1 = 좌석_정보(1L, 3, false, null);
    }

    @DisplayName("주문 생성")
    @Test
    void createOrder() {
        //given
        OrderLineItem 제육볶음_주문 = 주문_상품_생성(제육볶음_메뉴.getId(), 2);
        Order 주문 = 주문_생성(1L, 좌석1.getId(), OrderStatus.COOKING, 제육볶음_주문);
        given(menuDao.countByIdIn(anyList())).willReturn(1L);
        given(orderTableDao.findById(any())).willReturn(Optional.of(좌석1));
        given(orderDao.save(any())).willReturn(주문);
        given(orderLineItemDao.save(any())).willReturn(제육볶음_주문);

        // when
        Order expected = orderService.create(주문);

        // then
        assertThat(expected).isNotNull();
    }

    @DisplayName("주문 목록")
    @Test
    void listOrder() {
        //given
        OrderLineItem 제육볶음_주문 = 주문_상품_생성(제육볶음_메뉴.getId(), 2);
        Order 주문 = 주문_생성(1L, 좌석1.getId(), OrderStatus.COOKING, 제육볶음_주문);
        given(orderDao.findAll()).willReturn(Arrays.asList(주문));
        given(orderLineItemDao.findAllByOrderId(any(Long.class))).willReturn(Arrays.asList(제육볶음_주문));

        // when
        List<Order> orderList = orderService.list();

        // then
        assertThat(orderList.size()).isEqualTo(1);
    }

    @DisplayName("주문 상태 변경")
    @Test
    void changeOrderStatus() {
        //given
        OrderLineItem 제육볶음_주문 = 주문_상품_생성(제육볶음_메뉴.getId(), 2);
        Order 주문 = 주문_생성(1L, 좌석1.getId(), OrderStatus.COOKING, 제육볶음_주문);
        Order 주문_상태변경 = 주문_생성(1L, 좌석1.getId(), OrderStatus.MEAL, 제육볶음_주문);
        given(orderDao.findById(any(Long.class))).willReturn(Optional.of(주문));
        given(orderDao.save(any(Order.class))).willReturn(주문_상태변경);
        given(orderLineItemDao.findAllByOrderId(any(Long.class))).willReturn(Arrays.asList(제육볶음_주문));

        // when
        Order expected = orderService.changeOrderStatus(주문.getId(), 주문_상태변경);

        // then
        assertThat(expected).isNotNull();
        assertThat(expected.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }
}
