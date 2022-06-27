package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
import kitchenpos.fixture.UnitTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    private UnitTestFixture 식당_포스;

    @BeforeEach
    void setUp() {
        식당_포스 = new UnitTestFixture();
    }

    @Test
    void 주문을_등록할_수_있어야_한다() {
        // given
        final Order given = new Order(
                1L,
                식당_포스.테이블.getId(),
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                Arrays.asList(식당_포스.주문_항목1, 식당_포스.주문_항목2));
        when(menuDao.countByIdIn(Arrays.asList(식당_포스.주문_항목1.getMenuId(), 식당_포스.주문_항목2.getMenuId()))).thenReturn(2L);
        when(orderTableDao.findById(식당_포스.테이블.getId())).thenReturn(Optional.of(식당_포스.테이블));
        when(orderDao.save(given)).thenReturn(given);

        // when
        final Order actual = orderService.create(given);

        // then
        assertThat(given).isEqualTo(actual);
    }

    @Test
    void 주문_등록_시_주문_항목이_비어있으면_에러가_발생해야_한다() {
        // given
        final Order given = new Order(
                1L,
                식당_포스.테이블.getId(),
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                new ArrayList<>());

        // when and then
        assertThatThrownBy(() -> orderService.create(given))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_등록_시_주문_항목에_속한_메뉴가_하나라도_존재하지_않으면_에러가_발생해야_한다() {
        // given
        final Long invalidMenuId = -1L;
        final Order given = new Order(
                1L,
                식당_포스.테이블.getId(),
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                Arrays.asList(식당_포스.주문_항목1, new OrderLineItem(3L, 1L, invalidMenuId, 1)));
        when(menuDao.countByIdIn(Arrays.asList(식당_포스.주문_항목1.getMenuId(), invalidMenuId))).thenReturn(1L);

        // when and then
        assertThatThrownBy(() -> orderService.create(given))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_등록_시_테이블이_존재하지_않으면_에러가_발생해야_한다() {
        // given
        final Long invalidOrderTableId = -1L;
        final Order given = new Order(
                1L,
                invalidOrderTableId,
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                Arrays.asList(식당_포스.주문_항목1, 식당_포스.주문_항목2));
        when(menuDao.countByIdIn(Arrays.asList(식당_포스.주문_항목1.getMenuId(), 식당_포스.주문_항목2.getMenuId()))).thenReturn(2L);

        // when and then
        assertThatThrownBy(() -> orderService.create(given))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_등록_시_테이블이_비어_있으면_에러가_발생해야_한다() {
        // given
        final Order given = new Order(
                1L,
                식당_포스.빈_테이블1.getId(),
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                Arrays.asList(식당_포스.주문_항목1, 식당_포스.주문_항목2));
        when(menuDao.countByIdIn(Arrays.asList(식당_포스.주문_항목1.getMenuId(), 식당_포스.주문_항목2.getMenuId()))).thenReturn(2L);
        when(orderTableDao.findById(식당_포스.빈_테이블1.getId())).thenReturn(Optional.of(식당_포스.빈_테이블1));

        // when and then
        assertThatThrownBy(() -> orderService.create(given))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_목록을_조회할_수_있어야_한다() {
        // given
        when(orderDao.findAll()).thenReturn(Arrays.asList(식당_포스.주문, 식당_포스.완료된_주문));

        // when
        final List<Order> actual = orderService.list();

        // then
        assertThat(actual).containsExactly(식당_포스.주문, 식당_포스.완료된_주문);
    }

    @Test
    void 주문_상태를_변경할_수_있어야_한다() {
        // given
        when(orderDao.findById(식당_포스.주문.getId())).thenReturn(Optional.of(식당_포스.주문));

        // when
        orderService.changeOrderStatus(
                식당_포스.주문.getId(),
                new Order(
                        식당_포스.주문.getId(),
                        식당_포스.주문.getOrderTableId(),
                        OrderStatus.MEAL.name(),
                        식당_포스.주문.getOrderedTime(),
                        식당_포스.주문.getOrderLineItems()));

        // then
        assertThat(식당_포스.주문.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @Test
    void 주문_상태_변경_시_완료된_주문이면_에러가_발생해야_한다() {
        // given
        when(orderDao.findById(식당_포스.완료된_주문.getId())).thenReturn(Optional.of(식당_포스.완료된_주문));

        // when and then
        assertThatThrownBy(() -> {
            orderService.changeOrderStatus(
                    식당_포스.완료된_주문.getId(),
                    new Order(
                            식당_포스.완료된_주문.getId(),
                            식당_포스.완료된_주문.getOrderTableId(),
                            OrderStatus.MEAL.name(),
                            식당_포스.완료된_주문.getOrderedTime(),
                            식당_포스.완료된_주문.getOrderLineItems()));
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
