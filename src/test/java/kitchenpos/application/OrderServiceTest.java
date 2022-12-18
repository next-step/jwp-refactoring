package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 Business Object 테스트")
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

    Long 주문_id;
    OrderTable 주문_테이블;
    OrderLineItem 주문_아이템_1;
    OrderLineItem 주문_아이템_2;
    List<OrderLineItem> 주문_아이템_목록;
    Order 주문;

    @BeforeEach
    void setUp() {
        Long 메뉴_1_id = 1L;
        Long 메뉴_2_id = 2L;

        주문_id = 1L;
        주문_테이블 = new OrderTable(1L, null, 0, false);
        주문_아이템_1 = new OrderLineItem(1L, 주문_id, 메뉴_1_id, 2);
        주문_아이템_2 = new OrderLineItem(2L, 주문_id, 메뉴_2_id, 1);
        주문_아이템_목록 = Arrays.asList(주문_아이템_1, 주문_아이템_2);
        주문 = new Order(주문_id, 주문_테이블.getId(), OrderStatus.COOKING.name(), null, 주문_아이템_목록);
    }

    @DisplayName("주문 등록")
    @Test
    void 주문_등록() {
        when(menuDao.countByIdIn(메뉴_Id_목록(주문_아이템_목록))).thenReturn((long) 주문_아이템_목록.size());
        when(orderTableDao.findById(주문_테이블.getId())).thenReturn(Optional.of(주문_테이블));
        when(orderDao.save(주문)).thenReturn(주문);
        when(orderLineItemDao.save(주문_아이템_1)).thenReturn(주문_아이템_1);
        when(orderLineItemDao.save(주문_아이템_2)).thenReturn(주문_아이템_2);

        Order 등록된_주문 = orderService.create(주문);

        assertAll(
                () -> assertThat(등록된_주문).isEqualTo(주문),
                () -> assertThat(등록된_주문.getOrderedTime()).isNotNull(),
                () -> assertThat(등록된_주문.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(등록된_주문.getOrderLineItems()).containsAll(주문_아이템_목록)
        );
    }

    @DisplayName("빈 테이블에 대한 주문 등록 요청 시 예외처리")
    @Test
    void 빈테이블_주문_등록_예외처리() {
        OrderTable 빈_주문_테이블 = new OrderTable(1L, null, 0, true);
        Order 빈_테이블_주문 = new Order(1L, 빈_주문_테이블.getId(), OrderStatus.COOKING.name(), null, 주문_아이템_목록);
        when(menuDao.countByIdIn(메뉴_Id_목록(주문_아이템_목록))).thenReturn((long) 주문_아이템_목록.size());
        when(orderTableDao.findById(빈_주문_테이블.getId())).thenReturn(Optional.of(빈_주문_테이블));

        assertThatThrownBy(() -> orderService.create(빈_테이블_주문)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("생성되지 않은 주문 테이블에 대한 주문 등록 요청 시 예외처리")
    @Test
    void 생성안된_주문_테이블_주문_등록_예외처리() {
        when(menuDao.countByIdIn(메뉴_Id_목록(주문_아이템_목록))).thenReturn((long) 주문_아이템_목록.size());
        when(orderTableDao.findById(주문_테이블.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.create(주문)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴와 수량 정보 없이 주문 등록 요청 시 예외처리")
    @Test
    void 메뉴와_수량_누락_주문_등록_예외처리() {
        List<OrderLineItem> 빈_아이템_목록 = new ArrayList<>();
        Order 주문_아이템_누락된_주문 = new Order(주문_id, 주문_테이블.getId(), OrderStatus.COOKING.name(), null, 빈_아이템_목록);

        assertThatThrownBy(() -> orderService.create(주문_아이템_누락된_주문)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 조회")
    @Test
    void 주문_조회() {
        Order 주문2 = new Order(2L, 주문_테이블.getId(), OrderStatus.COOKING.name(), null, 주문_아이템_목록);
        List<Order> 등록된_주문_목록 = Arrays.asList(주문, 주문2);
        when(orderDao.findAll()).thenReturn(등록된_주문_목록);
        when(orderLineItemDao.findAllByOrderId(주문.getId())).thenReturn(주문_아이템_목록);
        when(orderLineItemDao.findAllByOrderId(주문2.getId())).thenReturn(주문_아이템_목록);

        List<Order> 조회된_주문_목록 = orderService.list();

        assertThat(조회된_주문_목록).isEqualTo(등록된_주문_목록);
    }

    @DisplayName("주문 상태변경")
    @Test
    void 주문_상태변경() {
        Order 변경할_주문 = new Order(주문_id, 주문_테이블.getId(), OrderStatus.MEAL.name(), null, 주문_아이템_목록);
        when(orderDao.findById(주문_id)).thenReturn(Optional.of(주문));
        when(orderLineItemDao.findAllByOrderId(주문_id)).thenReturn(주문_아이템_목록);

        //조리 -> 식사
        Order 상태_변경된_주문 = orderService.changeOrderStatus(주문_id, 변경할_주문);
        assertAll(
                () -> assertThat(상태_변경된_주문.getId()).isEqualTo(주문_id),
                () -> assertThat(상태_변경된_주문.getOrderStatus()).isEqualTo(변경할_주문.getOrderStatus())
        );

        //식사 -> 조리
        변경할_주문.setOrderStatus(OrderStatus.COOKING.name());
        Order 상태_변경된_주문2 = orderService.changeOrderStatus(주문_id, 변경할_주문);
        assertAll(
                () -> assertThat(상태_변경된_주문2.getId()).isEqualTo(주문_id),
                () -> assertThat(상태_변경된_주문2.getOrderStatus()).isEqualTo(변경할_주문.getOrderStatus())
        );

        //조리 -> 계산 완료
        변경할_주문.setOrderStatus(OrderStatus.COMPLETION.name());
        Order 상태_변경된_주문3 = orderService.changeOrderStatus(주문_id, 변경할_주문);
        assertAll(
                () -> assertThat(상태_변경된_주문3.getId()).isEqualTo(주문_id),
                () -> assertThat(상태_변경된_주문3.getOrderStatus()).isEqualTo(변경할_주문.getOrderStatus())
        );
    }

    @DisplayName("등록되지 않은 주문의 상태변경 시 예외처리")
    @Test
    void 동록안된_주문_상태변경_예외처리() {
        Long 변경할_주문_id = 3L;
        Order 변경할_주문 = new Order(변경할_주문_id, 주문_테이블.getId(), OrderStatus.MEAL.name(), null, 주문_아이템_목록);
        when(orderDao.findById(변경할_주문_id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.changeOrderStatus(변경할_주문_id, 변경할_주문)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @DisplayName("계산 완료 상태인 주문 상태변경 시 예외처리")
    @Test
    void 계산_완료_주문_상태변경_예외처리() {
        Order 계산_완료된_주문 = new Order(2L, 주문_테이블.getId(), OrderStatus.COMPLETION.name(), null, 주문_아이템_목록);
        Order 변경할_주문 = new Order(2L, 주문_테이블.getId(), OrderStatus.MEAL.name(), null, 주문_아이템_목록);
        when(orderDao.findById(계산_완료된_주문.getId())).thenReturn(Optional.of(계산_완료된_주문));

        assertThatThrownBy(() -> orderService.changeOrderStatus(계산_완료된_주문.getId(), 변경할_주문)).isInstanceOf(
                IllegalArgumentException.class);
    }

    private List<Long> 메뉴_Id_목록(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

}