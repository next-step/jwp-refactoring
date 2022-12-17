package kitchenpos.appliction;

import kitchenpos.application.OrderService;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.domain.OrderFixture.주문;
import static kitchenpos.domain.OrderLineItemFixture.주문라인아이템;
import static kitchenpos.domain.OrderTableFixture.빈주문테이블;
import static kitchenpos.domain.OrderTableFixture.주문테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("주문 테스트")
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

    Order 주문1;
    Order 주문2;

    OrderTable 테이블1;
    OrderTable 테이블2;
    OrderTable 빈테이블;

    OrderLineItem 풀코스_주문;
    OrderLineItem 오일2인세트_주문;


    @BeforeEach
    void setup() {
        풀코스_주문 = 주문라인아이템(1L, 1L, 1);
        오일2인세트_주문 = 주문라인아이템(2L, 2L, 1);
        오일2인세트_주문 = 주문라인아이템(3L, 2L, 1);

        테이블1 = 주문테이블(1L, null, 4, false);
        테이블2 = 주문테이블(2L, null, 2, false);
        빈테이블 = 빈주문테이블(3L);

        주문1 = 주문(1L, 테이블1.getId(), OrderStatus.COOKING.name(), Arrays.asList(풀코스_주문, 오일2인세트_주문));
        주문2 = 주문(1L, 테이블2.getId(), OrderStatus.COOKING.name(), Arrays.asList(오일2인세트_주문));
    }

    @DisplayName("주문을 생성한다")
    @Test
    void 주문_생성() {
        // given
        given(menuDao.countByIdIn(Arrays.asList(1L, 2L))).willReturn(2L);
        given(orderTableDao.findById(테이블1.getId())).willReturn(Optional.ofNullable(테이블1));
        given(orderDao.save(any())).willReturn(주문1);
        given(orderLineItemDao.save(풀코스_주문)).willReturn(풀코스_주문);
        given(orderLineItemDao.save(오일2인세트_주문)).willReturn(오일2인세트_주문);

        // when
        Order order = orderService.create(주문1);

        // then
        verify(orderDao).save(any());
        verify(orderLineItemDao, times(2)).save(any());
        assertAll(
                () -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(order.getOrderLineItems()).hasSize(2),
                () -> assertThat(order.getOrderLineItems()).containsExactly(풀코스_주문, 오일2인세트_주문)
        );
    }

    @DisplayName("전체 주문 목록을 조회한다")
    @Test
    void 전체_주문_목록_조회() {
        // given
        given(orderDao.findAll()).willReturn(Arrays.asList(주문1, 주문2));
        given(orderLineItemDao.findAllByOrderId(주문1.getId())).willReturn(주문1.getOrderLineItems());
        given(orderLineItemDao.findAllByOrderId(주문2.getId())).willReturn(주문2.getOrderLineItems());

        // when
        List<Order> orders = orderService.list();

        //then
        assertAll(
                () -> assertThat(orders).hasSize(2),
                () -> assertThat(orders).containsExactly(주문1, 주문2),
                () -> assertThat(orders.get(0).getOrderLineItems()).isEqualTo(주문1.getOrderLineItems())
        );
    }

    @DisplayName("주문 상태를 갱신한다")
    @Test
    void 주문_상태_갱신() {
        // given
        주문1.setOrderStatus(OrderStatus.MEAL.name());
        given(orderDao.findById(주문1.getId())).willReturn(Optional.ofNullable(주문1));
        given(orderDao.save(주문1)).willReturn(주문1);
        given(orderLineItemDao.findAllByOrderId(주문1.getId())).willReturn(주문1.getOrderLineItems());

        // when
        Order order = orderService.changeOrderStatus(주문1.getId(), 주문1);

        //then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("주문라인아이템 정보가 없는 주문을 생성한다")
    @Test
    void 주문라인아이템_정보가_없는_주문_생성() {
        // given
        주문1.setOrderLineItems(null);

        // when & then
        assertThatThrownBy(
                () -> orderService.create(주문1)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 메뉴가 있는 주문을 생성한다")
    @Test
    void 존재하지_않는_메뉴가_포함된_주문_생성() {
        // given
        given(menuDao.countByIdIn(Arrays.asList(1L, 2L))).willReturn(1L);

        // when & then
        assertThatThrownBy(
                () -> orderService.create(주문1)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블 정보가 없는 주문을 생성한다")
    @Test
    void 주문테이블_정보가_없는_주문_생성() {
        // given
        given(menuDao.countByIdIn(Arrays.asList(1L, 2L))).willReturn(2L);
        given(orderTableDao.findById(테이블1.getId())).willReturn(Optional.ofNullable(null));

        // when & then
        assertThatThrownBy(
                () -> orderService.create(주문1)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("비어있는 주문테이블에 주문을 생성한다")
    @Test
    void 빈_주문테이블에_주문_생성() {
        // given
        주문1.setOrderTableId(빈테이블.getId());
        given(menuDao.countByIdIn(Arrays.asList(1L, 2L))).willReturn(2L);
        given(orderTableDao.findById(빈테이블.getId())).willReturn(Optional.ofNullable(빈테이블));

        // when & then
        assertThatThrownBy(
                () -> orderService.create(주문1)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 주문의 주문상태를 갱신한다")
    @Test
    void 존재하지_않는_주문의_주문상태_갱신() {
        // given
        given(orderDao.findById(주문1.getId())).willReturn(Optional.ofNullable(null));

        // when & then
        assertThatThrownBy(
                () -> orderService.changeOrderStatus(주문1.getId(), 주문1)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("완료된 주문의 주문상태를 갱신한다")
    @Test
    void 완료된_주문의_주문상태_갱신() {
        // given
        주문1.setOrderStatus(OrderStatus.COMPLETION.name());
        given(orderDao.findById(주문1.getId())).willReturn(Optional.ofNullable(주문1));

        // when & then
        assertThatThrownBy(
                () -> orderService.changeOrderStatus(주문1.getId(), 주문1)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
