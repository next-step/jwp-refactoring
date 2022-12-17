package kitchenpos.application;

import kitchenpos.menu.dao.MenuDao;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderLineItemDao;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
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

import static kitchenpos.application.MenuServiceTest.generateMenu;
import static kitchenpos.application.TableServiceTest.generateOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("주문")
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    private Menu 메뉴1;
    private Menu 메뉴2;

    private OrderTable 메뉴테이블;

    private OrderLineItem 주문항목1;
    private OrderLineItem 주문항목2;

    private Order 주문;

    @BeforeEach
    void setUp() {
        메뉴1 = generateMenu(1L, "menu1", null, 1L, null);
        메뉴2 = generateMenu(2L, "menu2", null, 1L, null);

        메뉴테이블 = generateOrderTable(1L, 0, false);

        주문항목1 = generateOrderLineItem(메뉴1.getId(), 2L);
        주문항목2 = generateOrderLineItem(메뉴2.getId(), 1L);

        주문 = generateOrder(1L, 메뉴테이블.getId(), Arrays.asList(주문항목1, 주문항목2));
    }

    @Test
    @DisplayName("전체 주문을 조회할 수 있다.")
    void orderTest1() {
        List<Order> 주문들 = 주문들_생성();

        given(orderDao.findAll()).willReturn(주문들);
        given(orderLineItemDao.findAllByOrderId(주문.getId())).willReturn(주문.getOrderLineItems());

        List<Order> 조회된_주문들 = orderService.list();

        assertThat(조회된_주문들.size()).isEqualTo(주문들.size());
    }

    @Test
    @DisplayName("새로운 주문을 추가할 수 있다.")
    void orderTest2() {
        given(menuDao.countByIdIn(Arrays.asList(메뉴1.getId(), 메뉴2.getId()))).willReturn(2L);
        given(orderTableDao.findById(주문.getOrderTableId())).willReturn(Optional.of(메뉴테이블));
        given(orderDao.save(주문)).willReturn(주문);
        given(orderLineItemDao.save(주문항목1)).willReturn(주문항목1);
        given(orderLineItemDao.save(주문항목2)).willReturn(주문항목2);

        Order 생성된_주문 = orderService.create(주문);

        assertThat(생성된_주문.getId()).isEqualTo(주문.getId());
    }

    @Test
    @DisplayName("새로운 주문 추가 : 주문항목은 존재하는 메뉴들로만 구성되야 한다.")
    void orderTest3() {
        Order 존재하지않는_메뉴가포함된_주문 = generateOrder(1L, 메뉴테이블.getId(), null);

        assertThatThrownBy(() -> orderService.create(존재하지않는_메뉴가포함된_주문))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("새로운 주문 추가 : 존재하지 않는 주문 테이블로 요청할 수 없다.")
    void orderTest4() {
        given(menuDao.countByIdIn(Arrays.asList(메뉴1.getId(), 메뉴2.getId()))).willReturn(2L);
        given(orderTableDao.findById(any(Long.class))).willReturn(Optional.of(generateOrderTable(1L, 0, true)));

        assertThatThrownBy(() -> orderService.create(주문))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("새로운 주문 추가 : 실제로 존재하는 메뉴로만 요청할 수 있다.")
    void orderTest5() {
        given(menuDao.countByIdIn(Arrays.asList(메뉴1.getId(), 메뉴2.getId()))).willReturn(1L);

        assertThatThrownBy(() -> orderService.create(주문))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 현황을 수정할 수 있다.")
    void orderTest6() {
        given(orderDao.findById(주문.getId())).willReturn(Optional.of(주문));
        given(orderDao.save(주문)).willReturn(주문);
        given(orderLineItemDao.findAllByOrderId(주문.getId())).willReturn(Arrays.asList(주문항목1, 주문항목2));

        Order 변경할_주문_현황 = generateOrder(OrderStatus.COMPLETION.name());
        Order 변경된_주문 = orderService.changeOrderStatus(주문.getId(), 변경할_주문_현황);

        assertThat(변경된_주문.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

    @Test
    @DisplayName("주문 현황 수정 : 존재하지 않는 주문으로 요청할 수 없다.")
    void orderTest7() {
        Order 변경할_주문_현황 = generateOrder(OrderStatus.COMPLETION.name());

        assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), 변경할_주문_현황))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 현황 수정 : 완료된 상태의 주문 현황은 수정할 수 없다.")
    void orderTest8() {
        Order 변경할_주문_현황 = generateOrder(OrderStatus.COMPLETION.name());
        주문.setOrderStatus(변경할_주문_현황.getOrderStatus());

        given(orderDao.findById(주문.getId())).willReturn(Optional.of(주문));

        assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), 변경할_주문_현황))
                .isInstanceOf(IllegalArgumentException.class);

    }

    public static OrderLineItem generateOrderLineItem(Long menuId, long quantity) {
        return OrderLineItem.of(null, null, menuId, quantity);
    }

    public static Order generateOrder(Long id, Long orderTableId, List<OrderLineItem> orderLineItems) {
        return Order.of(id, orderTableId, null, null, orderLineItems);
    }

    public static Order generateOrder(String orderStatus) {
        return Order.of(null, null, orderStatus, null, null);
    }

    private List<Order> 주문들_생성() {
        return Arrays.asList(주문);
    }

}
