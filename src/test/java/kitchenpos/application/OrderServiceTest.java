package kitchenpos.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import kitchenpos.application.fixture.Fixture;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
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
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        //given
        주문등록_사전DB저장내용();
    }

    @DisplayName("주문이 저장된다.")
    @Test
    void create_order() {
        // given
        Order 주문신청 = new Order();
        주문신청.setOrderLineItems(List.of(Fixture.치킨_주문항목));
        주문신청.setOrderTableId(Fixture.치킨_주문_단체테이블.getId());

        // when
        Order savedOrder = orderService.create(주문신청);

        // then
        Assertions.assertThat(savedOrder).isEqualTo(Fixture.치킨주문);
    }

    @DisplayName("주문에속하는 수량있는 메뉴가 없는 주문은 예외가 발생된다.")
    @Test
    void exception_createOrder_emptyOrderedMenu() {
        등록된_메뉴개수조회전_DB내용(List.of(Fixture.뿌링클콤보.getId()));

        // given
        Order 주문신청 = new Order();
        주문신청.setOrderLineItems(List.of(Fixture.치킨_주문항목));

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> orderService.create(주문신청));
    }

    @DisplayName("미등록된 주문테이블에서 주문 시 예외가 발생된다.")
    @Test
    void exception_createOrder_notExistedOrderTable() {
        미등록된_주문테이블_조회전_DB내용(Fixture.치킨_주문_단체테이블);

        // given
        Order 주문신청 = new Order();
        주문신청.setOrderLineItems(List.of(Fixture.치킨_주문항목));

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> orderService.create(주문신청));
    }

    @DisplayName("주문이 조회된다.")
    @Test
    void search_order() {
        주문조회전_DB등록내용();
        
        // when
        List<Order> orders = orderService.list();

        // then
        Assertions.assertThat(orders).isEqualTo(List.of(Fixture.치킨주문));
    }

    @DisplayName("주문의 상태가 변경된다.")
    @Test
    void update_orderStatus() {
        계산안된_주문_상태변경전_DB내용();

        // given
        Order 주문_상태변경 = Fixture.치킨주문;
        주문_상태변경.setOrderStatus(OrderStatus.MEAL.name());

        // when
        Order chagedOrder = orderService.changeOrderStatus(주문_상태변경.getId(), 주문_상태변경);

        // then
        Assertions.assertThat(chagedOrder).isEqualTo(Fixture.치킨주문);
    }

    @DisplayName("계산이 완료된 주문의 상태를 변경시 예외가 발생된다.")
    @Test
    void exception_updateOrderStatus_afterOrderStatusCompletion() {
        계산된_주문_상태변경전_DB내용();

        // given
        Order 주문_상태변경 = Fixture.치킨주문;
        주문_상태변경.setOrderStatus(OrderStatus.COMPLETION.name());

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> orderService.changeOrderStatus(주문_상태변경.getId(), 주문_상태변경));
    }

    private void 등록된_메뉴개수조회전_DB내용(List<Long> menuIds) {
        when(menuDao.countByIdIn(menuIds)).thenReturn(0L);
    }

    private void 미등록된_주문테이블_조회전_DB내용(OrderTable orderTable) {
        when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.empty());
    }

    private void 주문조회전_DB등록내용() {
        when(orderDao.findAll()).thenReturn(List.of(Fixture.치킨주문));
        when(orderLineItemDao.findAllByOrderId(Fixture.치킨주문.getId())).thenReturn(List.of(Fixture.치킨_주문항목));
    }

    private void 계산안된_주문_상태변경전_DB내용() {
        Fixture.치킨주문.setOrderStatus(OrderStatus.MEAL.name());
        when(orderDao.findById(Fixture.치킨주문.getId())).thenReturn(Optional.of(Fixture.치킨주문));
    }
    private void 계산된_주문_상태변경전_DB내용() {
        Fixture.치킨주문.setOrderStatus(OrderStatus.COMPLETION.name());
        when(orderDao.findById(Fixture.치킨주문.getId())).thenReturn(Optional.of(Fixture.치킨주문));
    }

    private void 주문등록_사전DB저장내용() {
        when(menuDao.countByIdIn(List.of(Fixture.뿌링클콤보.getId()))).thenReturn(1L);
        when(orderTableDao.findById(Fixture.치킨_주문_단체테이블.getId())).thenReturn(Optional.of(Fixture.치킨_주문_단체테이블));
        when(orderDao.save(any(Order.class))).thenReturn(Fixture.치킨주문);
        when(orderLineItemDao.save(Fixture.치킨_주문항목)).thenReturn(Fixture.치킨_주문항목);
    }
}
