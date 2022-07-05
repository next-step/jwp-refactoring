package kitchenpos.order.application;

import static kitchenpos.menu.application.MenuGroupServiceTest.메뉴_그룹_생성;
import static kitchenpos.menu.application.MenuServiceTest.메뉴_생성;
import static kitchenpos.table.application.TableServiceTest.주문_테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuDao;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderDao;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemDao;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableDao;
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
    private OrderDao orderDao;

    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @InjectMocks
    private OrderService orderService;

    private Order 주문;
    private OrderTable 주문_테이블;
    private OrderLineItem 주문_목록_추천_치킨;
    private MenuGroup 추천_메뉴;
    private Menu 후라이드_세트_메뉴;

    @BeforeEach
    void init() {
        주문_테이블 = 주문_테이블_생성(1L, 4, false);
        추천_메뉴 = 메뉴_그룹_생성(1L, "추천메뉴");
        후라이드_세트_메뉴 = 메뉴_생성(1L, "후라이드세트메뉴", 16_000L, 추천_메뉴);

        주문_목록_추천_치킨 = 주문_목록_생성(주문, 후라이드_세트_메뉴, 2);
    }

    @Test
    @DisplayName("주문을 생성한다.")
    void create() {
        // given
        OrderLineItems orderLineItems = new OrderLineItems(Arrays.asList(주문_목록_추천_치킨));
        주문 = 주문_생성(1L, 주문_테이블, orderLineItems);

        given(orderTableDao.findById(any())).willReturn(Optional.of(주문_테이블));
        given(menuDao.countByIdIn(anyList())).willReturn(1L);
        given(menuDao.findById(any())).willReturn(Optional.of(후라이드_세트_메뉴));
        given(orderDao.save(any(Order.class))).willReturn(주문);

        // when
        OrderResponse savedOrder = orderService.create(new OrderRequest(주문));

        // then
        assertAll(
            () -> assertThat(savedOrder).isNotNull(),
            () -> assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name())
        );
    }

    @Test
    @DisplayName("존재하지 않은 주문 목록으로 주문을 생성할 경우 - 오류")
    void createOrderIfNonExistentMenu() {
        // given
        Menu 존재하지_않는_메뉴 = 메뉴_생성(3L, "존재하지않는메뉴", 3000L, null);
        OrderLineItem 존재하지_않는_메뉴의_주문_목록 = 주문_목록_생성(주문, 존재하지_않는_메뉴, 2);
        Long 존재하는_메뉴_개수 = 0L;
//        주문.setOrderLineItems(Arrays.asList(존재하지_않는_메뉴의_주문_목록));
        given(menuDao.countByIdIn(anyList())).willReturn(존재하는_메뉴_개수);

        // when then
        assertThatThrownBy(() -> orderService.create(new OrderRequest(주문)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않은 주문 테이블로 주문을 생성할 경우 - 오류")
    void createOrderIfNonExistentOrderTable() {
        // given
//        주문.setOrderLineItems(Arrays.asList(주문_목록_추천_치킨));
//        given(menuDao.countByIdIn(anyList())).willReturn(반반_치킨_메뉴);
//        given(orderTableDao.findById(any())).willReturn(Optional.empty());
//
//        // when then
//        assertThatThrownBy(() -> orderService.create(new OrderRequest(주문)))
//            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈 주문 테이블로 주문을 생성할 경우 - 오류")
    void createOrderIfOrderTableIsEmpty() {
        // given
//        OrderTable 빈_주문_테이블 = 주문_테이블_생성(주문_테이블.getId(), 주문_테이블.getNumberOfGuests(), true);
//
////        주문.setOrderLineItems(Arrays.asList(주문_목록_추천_치킨));
//        given(menuDao.countByIdIn(anyList())).willReturn(반반_치킨_메뉴);
//        given(orderTableDao.findById(any())).willReturn(Optional.of(빈_주문_테이블));
//
//        // when then
//        assertThatThrownBy(() -> orderService.create(new OrderRequest(주문)))
//            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 목록을 조회한다.")
    void findAll() {
        // given
        OrderLineItems orderLineItems = new OrderLineItems(Arrays.asList(주문_목록_추천_치킨));
        주문 = 주문_생성(1L, 주문_테이블, orderLineItems);
        given(orderDao.findAll()).willReturn(Arrays.asList(주문));

        // when
        List<OrderResponse> orders = orderService.list();

        // then
        assertThat(orders.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("주문의 상태를 변경한다.")
    void changeOrderStatus() {
        // given
//        Order 저장된_주문 = 주문_생성(1L, 주문_테이블);
//        Order 주문_상태가_변경된_주문 = 주문_생성(저장된_주문.getId(), 저장된_주문.getOrderTableId(), OrderStatus.COOKING);

//        저장된_주문.setOrderLineItems(Arrays.asList(주문_목록_추천_치킨));
//        given(orderDao.save(any(Order.class))).willReturn(저장된_주문);
//        given(orderDao.findById(저장된_주문.getId())).willReturn(Optional.of(저장된_주문));
//        given(orderLineItemDao.findAllByOrderId(any(Long.class))).willReturn(Arrays.asList(주문_목록_반반치킨));

        // when
//        Order changedOrder = orderService.changeOrderStatus(저장된_주문.getId(), 주문_상태가_변경된_주문);

        // then
//        assertThat(changedOrder.getOrderStatus()).isEqualTo(주문_상태가_변경된_주문.getOrderStatus());
    }

    @Test
    @DisplayName("존재하지 않는 주문의 상태를 변경할 경우 - 오류")
    void changeOrderStatusIfNonExistentOrder() {
        // given
//        Order 없는_주문 = 주문_생성(2L, 주문.getOrderTableId(), OrderStatus.COOKING);

//        given(orderDao.findById(없는_주문.getId())).willReturn(Optional.empty());

        // when then
//        assertThatThrownBy(() -> orderService.changeOrderStatus(없는_주문.getId(), 없는_주문))
//            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("저장된 '주문 완료' 상태의 주문을 변경할 경우 - 오류")
    void changeOrderStatusIfOrderStatusIsCompletion() {
        // given
//        Order 주문_완료된_주문 = 주문_생성(주문.getId(), 주문.getOrderTableId(), OrderStatus.COMPLETION);
//        Order 주문_상태가_변경된_주문 = 주문_생성(주문.getId(), 주문.getOrderTableId(), OrderStatus.COOKING);

//        given(orderDao.findById(주문.getId())).willReturn(Optional.of(주문_완료된_주문));

        // when then
//        assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), 주문_상태가_변경된_주문))
//            .isInstanceOf(IllegalArgumentException.class);
    }

    public static Order 주문_생성(Long id, OrderTable orderTable, OrderLineItems orderLineItems) {
        return new Order(id, orderTable, orderLineItems);
    }

    public static OrderLineItem 주문_목록_생성(Order order, Menu menu, int quantity) {
        return new OrderLineItem(order, menu, quantity);
    }
}
