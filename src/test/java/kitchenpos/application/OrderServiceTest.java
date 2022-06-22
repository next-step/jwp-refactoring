//package kitchenpos.application;
//
//import kitchenpos.dao.MenuDao;
//import kitchenpos.dao.OrderDao;
//import kitchenpos.dao.OrderLineItemDao;
//import kitchenpos.dao.OrderTableDao;
//import kitchenpos.domain.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.math.BigDecimal;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.junit.jupiter.api.Assertions.assertAll;
//
//@ExtendWith(MockitoExtension.class)
//class OrderServiceTest {
//    @Mock
//    private MenuDao menuDao;
//    @Mock
//    private OrderDao orderDao;
//    @Mock
//    private OrderLineItemDao orderLineItemDao;
//    @Mock
//    private OrderTableDao orderTableDao;
//    @InjectMocks
//    private OrderService orderService;
//
//    private Product 제육볶음;
//    private Product 소불고기;
//    private List<MenuProduct> 고기반찬;
//    private MenuGroup 점심특선;
//    private Menu 점심특선A;
//
//    @BeforeEach
//    void beforeEach() {
//        제육볶음 = productService.create(Product.of("제육볶음", BigDecimal.valueOf(1000)));
//        소불고기 = productService.create(Product.of("소불고기", BigDecimal.valueOf(1000)));
//        고기반찬 = Arrays.asList(MenuProduct.of(제육볶음.getId(), 1), MenuProduct.of(소불고기.getId(), 1));
//        점심특선 = 메뉴묶음_요청("점심특선");
//        점심특선A = menuService.create(Menu.of("점심특선", BigDecimal.valueOf(2000), this.점심특선.getId(), 고기반찬));
//    }
//
//    @Test
//    void create() {
//        // given
//        OrderTable 주문불가능테이블 = tableService.create(OrderTable.of(0, true));
//        OrderTable 주문가능테이블 = tableService.changeEmpty(주문불가능테이블.getId(), OrderTable.from(false));
//        List<OrderLineItem> 주문항목 = Collections.singletonList(OrderLineItem.of(점심특선A.getId(), 1));
//
//        // then
//        Order 점심특선A주문 = orderService.create(Order.of(주문가능테이블.getId(), 주문항목));
//
//        // then
//        assertAll(
//                () -> assertThat(주문불가능테이블.getId()).isEqualTo(주문가능테이블.getId()),
//                () -> assertThat(점심특선A주문.getOrderTableId()).isEqualTo(주문가능테이블.getId()),
//                () -> assertThat(점심특선A주문.getId()).isNotNull(),
//                () -> assertThat(점심특선A주문.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name())
//        );
//    }
//
//    @Test
//    void create_throwException_ifEmptyTable() {
//        // given
//        OrderTable 주문불가능테이블 = tableService.create(OrderTable.of(0, true));
//        List<OrderLineItem> 주문항목 = Collections.singletonList(OrderLineItem.of(점심특선A.getId(), 1));
//
//        // then
//        // when
//        assertThatThrownBy(() -> orderService.create(Order.of(주문불가능테이블.getId(), 주문항목)));
//    }
//
//    @Test
//    void changeOrderStatus() {
//        // given
//        OrderTable 주문불가능테이블 = tableService.create(OrderTable.of(0, true));
//        OrderTable 주문가능테이블 = tableService.changeEmpty(주문불가능테이블.getId(), OrderTable.from(false));
//        List<OrderLineItem> 주문항목 = Collections.singletonList(OrderLineItem.of(점심특선A.getId(), 1));
//        Order 점심특선A주문 = orderService.create(Order.of(주문가능테이블.getId(), 주문항목));
//
//        // when
//        Order 주문상태변경 = orderService.changeOrderStatus(점심특선A주문.getId(), Order.from(OrderStatus.MEAL.name()));
//
//        // then
//        assertThat(주문상태변경.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
//    }
//
//    MenuGroup 메뉴묶음_요청(String name) {
//        return menuGroupService.create(new MenuGroup(name));
//    }
//}
