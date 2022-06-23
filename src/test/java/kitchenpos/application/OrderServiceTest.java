package kitchenpos.application;

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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

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

    Product 샐러드;
    MenuProduct 메뉴_샐러드;
    MenuGroup 채식;
    Menu 기본_메뉴;
    OrderTable 테이블;
    OrderTable 빈_테이블;

    @BeforeEach
    void init() {
        샐러드 = ProductServiceTest.상품_생성(1L, "샐러드", 100);
        메뉴_샐러드 = MenuServiceTest.메뉴_상품_생성(샐러드.getId(), 2);
        채식 = MenuGroupServiceTest.메뉴_그룹_생성(1L, "채식");
        기본_메뉴 = MenuServiceTest.메뉴_생성(채식.getId(), "기본 메뉴", 200, 메뉴_샐러드);
        테이블 = TableServiceTest.주문_테이블_생성(null, 1L, false, 0);
        빈_테이블 = TableServiceTest.주문_테이블_생성(null, 1L, true, 0);
    }

    @DisplayName("주문을 생성한다")
    @Test
    void 주문_생성_성공() {
        // given
        OrderLineItem 주문_항목 = 주문_항목_생성(기본_메뉴.getId(), 2);
        Order 주문 = 주문_생성(테이블.getId(), 1L, OrderStatus.COOKING, 주문_항목);
        given(menuDao.countByIdIn(anyList())).willReturn(1L);
        given(orderTableDao.findById(any())).willReturn(Optional.of(테이블));
        given(orderDao.save(any())).willReturn(주문);
        given(orderLineItemDao.save(any())).willReturn(주문_항목);

        // when
        Order saved = orderService.create(주문);

        // then
        assertThat(saved).isNotNull();
    }

    @DisplayName("빈 테이블에 주문을 등록할 수 없다.")
    @Test
    void 주문_생성_예외_빈_테이블() {
        // given
        OrderLineItem 주문_항목 = 주문_항목_생성(기본_메뉴.getId(), 2);
        Order 주문 = 주문_생성(빈_테이블.getId(), 1L, OrderStatus.COOKING, 주문_항목);
        given(menuDao.countByIdIn(anyList())).willReturn(1L);
        given(orderTableDao.findById(any())).willReturn(Optional.of(빈_테이블));

        // when, then
        assertThatThrownBy(() -> orderService.create(주문))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 항목이 없어서 주문 생성에 실패한다.")
    @Test
    void 주문_생성_예외_주문_항목_없음() {
        // given
        Order 주문 = 주문_생성(빈_테이블.getId(), 1L, OrderStatus.COOKING);

        // when, then
        assertThatThrownBy(() -> orderService.create(주문))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void 주문_목록_조회() {
        // given
        OrderLineItem 주문_항목 = 주문_항목_생성(기본_메뉴.getId(), 2);
        Order 주문 = 주문_생성(테이블.getId(), 1L, OrderStatus.COOKING, 주문_항목);
        given(orderDao.findAll()).willReturn(Arrays.asList(주문));
        given(orderLineItemDao.findAllByOrderId(any(Long.class))).willReturn(Arrays.asList(주문_항목));

        // when
        List<Order> 주문_목록 = orderService.list();

        // then
        assertThat(주문_목록).containsExactly(주문);
    }

    @DisplayName("주문의 상태를 변경한다.")
    @Test
    void 주문_상태_변경() {
        // given
        OrderLineItem 주문_항목 = 주문_항목_생성(기본_메뉴.getId(), 2);
        Order 주문 = 주문_생성(테이블.getId(), 1L, OrderStatus.COOKING, 주문_항목);
        Order 변경된_주문 = 주문_생성(테이블.getId(), 1L, OrderStatus.MEAL, 주문_항목);
        given(orderDao.findById(any(Long.class))).willReturn(Optional.of(주문));
        given(orderDao.save(any(Order.class))).willReturn(변경된_주문);
        given(orderLineItemDao.findAllByOrderId(any(Long.class))).willReturn(Arrays.asList(주문_항목));

        // when
        Order changed = orderService.changeOrderStatus(주문.getId(), 변경된_주문);

        // then
        assertThat(changed).isNotNull();
        assertThat(changed.getOrderStatus()).isEqualTo(변경된_주문.getOrderStatus());
    }

    @DisplayName("주문 상태 변경에 실패한다.")
    @Test
    void 주문_상태_변경_예외() {
        // given
        OrderLineItem 주문_항목 = 주문_항목_생성(기본_메뉴.getId(), 2);
        Order 주문 = 주문_생성(테이블.getId(), 1L, OrderStatus.COMPLETION, 주문_항목);
        Order 변경된_주문 = 주문_생성(테이블.getId(), 1L, OrderStatus.MEAL, 주문_항목);
        given(orderDao.findById(any(Long.class))).willReturn(Optional.of(주문));

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), 변경된_주문))
                .isInstanceOf(IllegalArgumentException.class);
    }

    public static OrderLineItem 주문_항목_생성(Long menuId, int quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }

    public static Order 주문_생성(Long orderTableId, Long orderId, OrderStatus orderStatus, OrderLineItem... orderLineItems) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setId(orderId);
        order.setOrderStatus(orderStatus.name());
        order.setOrderLineItems(Arrays.asList(orderLineItems));
        order.setOrderedTime(LocalDateTime.now());
        return order;
    }
}
