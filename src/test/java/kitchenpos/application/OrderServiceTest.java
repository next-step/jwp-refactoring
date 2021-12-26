package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.*;
import kitchenpos.product.domain.Product;
import org.assertj.core.util.Lists;
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

import static kitchenpos.application.MenuGroupServiceTest.메뉴_그룹_등록;
import static kitchenpos.application.MenuServiceTest.메뉴_등록;
import static kitchenpos.application.MenuServiceTest.메뉴_상품_등록;
import static kitchenpos.application.ProductServiceTest.상품_등록;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("주문 관련 기능")
public class OrderServiceTest {
    @Mock
    private MenuDao menuDao;
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderLineItemDao orderLineItemDao;
    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    OrderService orderService;

    private Product 짜장면;
    private Product 탕수육;
    private MenuGroup 중국음식;
    private Menu 짜장면메뉴1;
    private Menu 짜장면메뉴2;
    private Menu 짜장면메뉴3;

    private OrderTable 테이블;

    @BeforeEach
    void setUp() {
        짜장면 = 상품_등록("짜장면", 5000);
        탕수육 = 상품_등록("탕수육", 15000);
        중국음식 = 메뉴_그룹_등록(1L, "중국음식");
        짜장면메뉴1 = 메뉴_등록(1L, "짜장면탕수육세트", 짜장면.getPrice().add(탕수육.getPrice()), 중국음식.getId(), Arrays.asList(메뉴_상품_등록(1L, 짜장면.getId(), 1L), 메뉴_상품_등록(2L, 탕수육.getId(), 1L)));
        짜장면메뉴2 = 메뉴_등록(2L, "짜장면", 짜장면.getPrice(), 중국음식.getId(), Arrays.asList(메뉴_상품_등록(1L, 짜장면.getId(), 1L)));
        짜장면메뉴3 = 메뉴_등록(3L, "탕수육", 탕수육.getPrice(), 중국음식.getId(), Arrays.asList(메뉴_상품_등록(1L, 탕수육.getId(), 1L)));

        테이블 = TableServiceTest.테이블_등록(1L, true);
    }

    @Test
    @DisplayName("주문을 등록할 때 주문 항목이 비어있으면 실패한다.")
    void createWithNoOrder() {
        Order 주문 = 주문_등록(1L, 테이블.getId(), Lists.emptyList());
        주문_등록_실패(주문);
    }

    @Test
    @DisplayName("주문을 등록할 때 메뉴 정보가 DB에 없으면 실패한다.")
    void createWithInvalidMenuId() {
        Order 주문 = 주문_등록(1L, 테이블.getId(), Arrays.asList(주문_항목(1L, 짜장면메뉴1.getId(), 1L), 주문_항목(2L, 짜장면메뉴2.getId(), 1L)));
        given(menuDao.countByIdIn(any())).willReturn(0l);

        주문_등록_실패(주문);
    }

    @Test
    @DisplayName("주문을 등록할 때 주문 테이블 정보가 DB에 없으면 실패한다.")
    void createWithInvalidOrderTable() {
        Order 주문 = 주문_등록(1L, 테이블.getId(), Arrays.asList(주문_항목(1L, 짜장면메뉴1.getId(), 1L), 주문_항목(2L, 짜장면메뉴2.getId(), 1L)));
        given(menuDao.countByIdIn(any())).willReturn(2l);
        given(orderTableDao.findById(any())).willReturn(Optional.empty());
        주문_등록_실패(주문);
    }

    @Test
    @DisplayName("주문을 등록할 때 주문 테이블이 비어있으면 실패한다.")
    void createWithEmptyOrderTable() {
        Order 주문 = 주문_등록(1L, 테이블.getId(), Arrays.asList(주문_항목(1L, 짜장면메뉴1.getId(), 1L), 주문_항목(2L, 짜장면메뉴2.getId(), 1L)));
        given(menuDao.countByIdIn(any())).willReturn(2l);
        given(orderTableDao.findById(any())).willReturn(Optional.ofNullable(테이블));
        주문_등록_실패(주문);
    }

    @Test
    @DisplayName("주문을 등록한다.")
    void createOrder() {
        Order 주문 = 주문_등록(1L, 테이블.getId(), Arrays.asList(주문_항목(1L, 짜장면메뉴1.getId(), 1L), 주문_항목(2L, 짜장면메뉴2.getId(), 1L)));
        given(menuDao.countByIdIn(any())).willReturn(2l);
        테이블.setEmpty(false);
        given(orderTableDao.findById(any())).willReturn(Optional.ofNullable(테이블));
        given(orderDao.save(any())).willReturn(주문);
        Order 새주문 = orderService.create(주문);
        assertThat(새주문.getOrderLineItems()).hasSize(2);
    }

    @Test
    @DisplayName("주문을 조회한다.")
    void list() {
        List<OrderLineItem> orderLineItems = Arrays.asList(주문_항목(1L, 짜장면메뉴1.getId(), 1L), 주문_항목(2L, 짜장면메뉴2.getId(), 1L));
        Order 주문1 = 주문_등록(1L, 테이블.getId(), orderLineItems);
        Order 주문2 = 주문_등록(2L, 테이블.getId(), Arrays.asList(주문_항목(1L, 짜장면메뉴1.getId(), 1L)));
        given(orderDao.findAll()).willReturn(Arrays.asList(주문1, 주문2));
        given(orderLineItemDao.findAllByOrderId(주문1.getId())).willReturn(orderLineItems);
        given(orderLineItemDao.findAllByOrderId(주문2.getId())).willReturn(Arrays.asList(주문_항목(1L, 짜장면메뉴1.getId(), 1L)));

        List<Order> orders = orderService.list();
        assertThat(orders).hasSize(2);
        assertThat(orders.get(0).getOrderLineItems()).hasSize(2);
        assertThat(orders.get(1).getOrderLineItems()).hasSize(1);
    }

    @Test
    @DisplayName("주문 상태가 완료이면 상태 변경에 실패한다.")
    void changeOrderStatusOfCompleted() {
        List<OrderLineItem> orderLineItems = Arrays.asList(주문_항목(1L, 짜장면메뉴1.getId(), 1L), 주문_항목(2L, 짜장면메뉴2.getId(), 1L));
        Order 주문 = 주문_등록(1L, 테이블.getId(), orderLineItems);
        주문.setOrderStatus(OrderStatus.COMPLETION.name());
        given(orderDao.findById(any())).willReturn(Optional.of(주문));
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            orderService.changeOrderStatus(주문.getId(), 주문);
        });
    }

    @Test
    @DisplayName("주문 상태를 변경한다.")
    void changeOrderStatus() {
        List<OrderLineItem> orderLineItems = Arrays.asList(주문_항목(1L, 짜장면메뉴1.getId(), 1L), 주문_항목(2L, 짜장면메뉴2.getId(), 1L));
        Order 주문_조리중 = 주문_등록(1L, 테이블.getId(), orderLineItems);
        주문_조리중.setOrderStatus(OrderStatus.COOKING.name());

        Order 주문_식사중 = 주문_등록(1L, 테이블.getId(), orderLineItems);
        주문_식사중.setOrderStatus(OrderStatus.MEAL.name());

        given(orderDao.findById(any())).willReturn(Optional.of(주문_조리중));
        Order 주문_상태_변경 = orderService.changeOrderStatus(주문_조리중.getId(), 주문_식사중);
        assertThat(주문_상태_변경.getOrderLineItems()).isNotNull();
    }

    private void 주문_등록_실패(Order order) {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            orderService.create(order);
        });
    }

    private OrderLineItem 주문_항목(Long seq, Long menuId, long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(seq);
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }

    private Order 주문_등록(Long id, Long orderTableId, List<OrderLineItem> orderLineItems) {
        Order order = new Order();
        order.setId(id);
        order.setOrderTableId(orderTableId);
        order.setOrderLineItems(orderLineItems);
        return order;
    }
}
