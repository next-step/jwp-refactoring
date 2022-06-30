package kitchenpos.application;

import static kitchenpos.application.TableServiceTest.주문_테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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

    private OrderLineItem 주문_목록_반반치킨;

    private Long 반반_치킨_메뉴 = 1L;

    @BeforeEach
    void init() {
        주문_테이블 = 주문_테이블_생성(1L, 4, false);
        주문 = 주문_생성(1L, 주문_테이블.getId());

        주문_목록_반반치킨 = 주문_목록_생성(주문.getId(), 반반_치킨_메뉴, 2);
    }

    @Test
    @DisplayName("주문을 생성한다.")
    void create() {
        // given
        주문.setOrderLineItems(Arrays.asList(주문_목록_반반치킨));
        given(menuDao.countByIdIn(anyList())).willReturn(반반_치킨_메뉴);
        given(orderTableDao.findById(any())).willReturn(Optional.of(주문_테이블));
        given(orderDao.save(any(Order.class))).willReturn(주문);
        given(orderLineItemDao.save(any())).willReturn(주문_목록_반반치킨);

        // when
        Order savedOrder = orderService.create(주문);

        // then
        assertAll(
            () -> assertThat(savedOrder).isNotNull(),
            () -> assertThat(savedOrder.getOrderTableId()).isEqualTo(주문.getOrderTableId())
        );
    }

    @Test
    @DisplayName("존재하지 않은 주문 목록으로 주문을 생성할 경우 - 오류")
    void createOrderIfNonExistentMenu() {
        // given
        OrderLineItem 존재하지_않는_메뉴의_주문_목록 = 주문_목록_생성(주문.getId(), 2L, 2);
        Long 존재하는_메뉴_개수 = 0L;
        주문.setOrderLineItems(Arrays.asList(존재하지_않는_메뉴의_주문_목록));
        given(menuDao.countByIdIn(anyList())).willReturn(존재하는_메뉴_개수);

        // when then
        assertThatThrownBy(() -> orderService.create(주문))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않은 주문 테이블로 주문을 생성할 경우 - 오류")
    void createOrderIfNonExistentOrderTable() {
        // given
        주문.setOrderLineItems(Arrays.asList(주문_목록_반반치킨));
        given(menuDao.countByIdIn(anyList())).willReturn(반반_치킨_메뉴);
        given(orderTableDao.findById(any())).willReturn(Optional.empty());

        // when then
        assertThatThrownBy(() -> orderService.create(주문))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈 주문 테이블로 주문을 생성할 경우 - 오류")
    void createOrderIfOrderTableIsEmpty() {
        // given
        OrderTable 빈_주문_테이블 = 주문_테이블_생성(주문_테이블.getId(), 주문_테이블.getNumberOfGuests(), true);

        주문.setOrderLineItems(Arrays.asList(주문_목록_반반치킨));
        given(menuDao.countByIdIn(anyList())).willReturn(반반_치킨_메뉴);
        given(orderTableDao.findById(any())).willReturn(Optional.of(빈_주문_테이블));

        // when then
        assertThatThrownBy(() -> orderService.create(주문))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 목록을 조회한다.")
    void findAll() {
        // given
        given(orderDao.findAll()).willReturn(Arrays.asList(주문));

        // when
        List<Order> orders = orderService.list();

        // then
        assertThat(orders).containsExactly(주문);
    }

    @Test
    @DisplayName("주문의 상태를 변경한다.")
    void changeOrderStatus() {
        // given
        Order 저장된_주문 = 주문_생성(1L, 주문_테이블.getId(), OrderStatus.MEAL);
        Order 주문_상태가_변경된_주문 = 주문_생성(저장된_주문.getId(), 저장된_주문.getOrderTableId(), OrderStatus.COOKING);

        저장된_주문.setOrderLineItems(Arrays.asList(주문_목록_반반치킨));
        given(orderDao.save(any(Order.class))).willReturn(저장된_주문);
        given(orderDao.findById(저장된_주문.getId())).willReturn(Optional.of(저장된_주문));
        given(orderLineItemDao.findAllByOrderId(any(Long.class))).willReturn(Arrays.asList(주문_목록_반반치킨));

        // when
        Order changedOrder = orderService.changeOrderStatus(저장된_주문.getId(), 주문_상태가_변경된_주문);

        // then
        assertThat(changedOrder.getOrderStatus()).isEqualTo(주문_상태가_변경된_주문.getOrderStatus());
    }

    @Test
    @DisplayName("존재하지 않는 주문의 상태를 변경할 경우 - 오류")
    void changeOrderStatusIfNonExistentOrder() {
        // given
        Order 없는_주문 = 주문_생성(2L, 주문.getOrderTableId(), OrderStatus.COOKING);

        given(orderDao.findById(없는_주문.getId())).willReturn(Optional.empty());

        // when then
        assertThatThrownBy(() -> orderService.changeOrderStatus(없는_주문.getId(), 없는_주문))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("저장된 '주문 완료' 상태의 주문을 변경할 경우 - 오류")
    void changeOrderStatusIfOrderStatusIsCompletion() {
        // given
        Order 주문_완료된_주문 = 주문_생성(주문.getId(), 주문.getOrderTableId(), OrderStatus.COMPLETION);
        Order 주문_상태가_변경된_주문 = 주문_생성(주문.getId(), 주문.getOrderTableId(), OrderStatus.COOKING);

        given(orderDao.findById(주문.getId())).willReturn(Optional.of(주문_완료된_주문));

        // when then
        assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), 주문_상태가_변경된_주문))
            .isInstanceOf(IllegalArgumentException.class);
    }

    public static Order 주문_생성(Long id, Long orderTableId) {
        return new Order(id, orderTableId);
    }

    public static Order 주문_생성(Long id, Long orderTableId, OrderStatus orderStatus) {
        return new Order(id, orderTableId, orderStatus.name());
    }

    public static Order 주문_생성(Long id, Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        return new Order(id, orderTableId, orderStatus.name(), orderLineItems);
    }

    public static OrderLineItem 주문_목록_생성(Long orderId, Long menuId, int quantity) {
        return new OrderLineItem(orderId, menuId, quantity);
    }
}
