package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.fixture.OrderFixture.요리중_상태_주문;
import static kitchenpos.fixture.OrderFixture.주문완료_상태_주문;
import static kitchenpos.fixture.OrderLineItemFixture.주문_항목;
import static kitchenpos.fixture.TableFixture.주문_테이블_그룹_없음;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
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

    @Test
    @DisplayName("주문 생성 성공 테스트")
    void create() {
        when(menuDao.countByIdIn(any())).thenReturn((long) 요리중_상태_주문.getOrderLineItems().size());
        when(orderTableDao.findById(any())).thenReturn(Optional.of(주문_테이블_그룹_없음));
        when(orderDao.save(any())).thenReturn(요리중_상태_주문);
        when(orderLineItemDao.save(any())).thenReturn(주문_항목);

        Order 주문_생성_결과 = orderService.create(요리중_상태_주문);

        Assertions.assertThat(주문_생성_결과).isEqualTo(요리중_상태_주문);
    }

    @Test
    @DisplayName("주문 생성시 주문의 메뉴 개수가 일치하지 않은 경우 실패 테스트")
    void create2() {
        when(menuDao.countByIdIn(any())).thenReturn(0L);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderService.create(요리중_상태_주문));
    }

    @Test
    @DisplayName("주문 생성시 주문의 테이블이 조회되지 않은 경우 실패 테스트")
    void create3() {
        when(menuDao.countByIdIn(any())).thenReturn((long) 요리중_상태_주문.getOrderLineItems().size());
        when(orderTableDao.findById(any())).thenReturn(Optional.empty());

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderService.create(요리중_상태_주문));
    }

    @Test
    @DisplayName("주문 생성시 주문의 테이블이 비어있는 경우 실패 테스트")
    void create4() {
        OrderTable 주문_테이블_비어있음 = new OrderTable();
        주문_테이블_비어있음.setEmpty(true);

        when(menuDao.countByIdIn(any())).thenReturn((long) 요리중_상태_주문.getOrderLineItems().size());
        when(orderTableDao.findById(any())).thenReturn(Optional.of(주문_테이블_비어있음));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderService.create(요리중_상태_주문));
    }

    @Test
    @DisplayName("주문 조회 성공 테스트")
    void list() {
        when(orderDao.findAll()).thenReturn(Arrays.asList(요리중_상태_주문));
        when(orderLineItemDao.findAllByOrderId(any())).thenReturn(Arrays.asList(주문_항목));

        List<Order> 주문_목록_조회_결과 = orderService.list();

        assertAll(
                () -> Assertions.assertThat(주문_목록_조회_결과).hasSize(1),
                () -> Assertions.assertThat(주문_목록_조회_결과).containsExactly(요리중_상태_주문)
        );
    }

    @Test
    @DisplayName("주문 상태 변경 성공 테스트")
    void changeOrderStatus() {
        // given
        Order 주문 = new Order();
        주문.setOrderStatus(OrderStatus.MEAL.name());

        when(orderDao.findById(any())).thenReturn(Optional.of(요리중_상태_주문));
        when(orderDao.save(any())).thenReturn(요리중_상태_주문);
        when(orderLineItemDao.findAllByOrderId(any())).thenReturn(Arrays.asList(주문_항목));

        Order 주문_상태_변경_결과 = orderService.changeOrderStatus(요리중_상태_주문.getId(), 주문);

        Assertions.assertThat(주문_상태_변경_결과.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @Test
    @DisplayName("주문 상태 변경시 주문이 완료 상태인 경우 실패 테스트")
    void changeOrderStatus2() {
        when(orderDao.findById(any())).thenReturn(Optional.of(주문완료_상태_주문));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderService.changeOrderStatus(주문완료_상태_주문.getId(), 주문완료_상태_주문));
    }

    @Test
    @DisplayName("주문 상태 변경시 주문이 조회되지 않는 경우 실패 테스트")
    void changeOrderStatus3() {
        when(orderDao.findById(any())).thenReturn(Optional.empty());

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderService.changeOrderStatus(주문완료_상태_주문.getId(), 주문완료_상태_주문));
    }
}
