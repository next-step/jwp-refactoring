package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@DisplayName("주문 서비스 테스트")
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

    private Product 상품;
    private MenuProduct 메뉴_상품;
    private List<MenuProduct> 메뉴_상품들;
    private MenuGroup 메뉴_그룹;
    private Menu 메뉴;

    @BeforeEach
    void beforeEach() {
        상품 = 상품_생성됨(1L, "소불고기", 1000);
        메뉴_상품 = 메뉴_상품_생성됨(상품);
        메뉴_상품들 = Arrays.asList(메뉴_상품, 메뉴_상품);
        메뉴_그룹 = 메뉴_그룹_생성됨("점심메뉴");
        메뉴 = 메뉴_생성됨("점심특선", 2000, 메뉴_그룹, 메뉴_상품들);
    }

    @DisplayName("주문을 생성하면 최초의 주문 상태는 조리로 설정된다.")
    @Test
    void create() {
        // given
        given(menuDao.countByIdIn(anyList())).willReturn(1L);
        given(orderTableDao.findById(anyLong())).willReturn(주문_테이블_생성됨());
        given(orderDao.save(any(Order.class))).willReturn(주문_생성됨());
        given(orderLineItemDao.save(any(OrderLineItem.class))).willReturn(주문_메뉴_생성됨());

        // when
        Order created = orderService.create(주문_생성_요청(1L, 주문_메뉴_생성_요청(메뉴.getId(), 1)));

        // then
        assertThat(created.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());

        // verify
        then(menuDao).should(times(1)).countByIdIn(anyList());
        then(orderTableDao).should(times(1)).findById(anyLong());
        then(orderDao).should(times(1)).save(any(Order.class));
        then(orderLineItemDao).should(times(1)).save(any(OrderLineItem.class));
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        // given
        given(orderDao.findById(anyLong())).willReturn(Optional.of(주문_생성됨()));
        given(orderLineItemDao.findAllByOrderId(anyLong())).willReturn(주문_메뉴_생성_요청(메뉴.getId(), 1));

        // when
        Order changed = orderService.changeOrderStatus(1L, 주문_상태_변경_요청(OrderStatus.MEAL.name()));

        // then
        assertThat(changed.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());

        // verify
        then(orderDao).should(times(1)).findById(anyLong());
        then(orderDao).should(times(1)).save(any(Order.class));
        then(orderLineItemDao).should(times(1)).findAllByOrderId(anyLong());
    }

    public static Product 상품_생성됨(Long id, String name, long price) {
        return new Product(id, name, price);
    }

    public static MenuProduct 메뉴_상품_생성됨(Product product) {
        return new MenuProduct.Builder(product.getId(), 2).build();
    }

    public static MenuGroup 메뉴_그룹_생성됨(String name) {
        return MenuGroup.builder().id(1L).name(name).build();
    }

    public static Menu 메뉴_생성됨(String name, long price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return new Menu.Builder(name, price, menuGroup.getId(), menuProducts).build();
    }

    public static Order 주문_생성_요청(Long orderTableId, List<OrderLineItem> orderLineItems) {
        return new Order.Builder(orderTableId, orderLineItems).build();
    }

    public static Order 주문_생성됨() {
        return new Order.Builder().id(1L).orderStatus(OrderStatus.COOKING.name()).build();
    }

    public static Order 주문_상태_변경_요청(String orderStatus) {
        return new Order.Builder().orderStatus(orderStatus).build();
    }

    public static List<OrderLineItem> 주문_메뉴_생성_요청(Long menuId, long quantity) {
        return Arrays.asList(new OrderLineItem.Builder(menuId, quantity).build());
    }

    public static OrderLineItem 주문_메뉴_생성됨() {
        return new OrderLineItem.Builder().build();
    }

    public static Optional<OrderTable> 주문_테이블_생성됨() {
        return Optional.of(new OrderTable.Builder().id(1L).empty(false).build());
    }
}
