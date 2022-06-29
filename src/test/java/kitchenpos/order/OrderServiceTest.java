package kitchenpos.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.order.application.OrderService;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderLineItemDao;
import kitchenpos.order.dao.OrderTableDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    OrderService orderService;

    @Mock
    MenuDao menuDao;

    @Mock
    OrderDao orderDao;

    @Mock
    OrderLineItemDao orderLineItemDao;

    @Mock
    OrderTableDao orderTableDao;

    Order 주문 = new Order();
    OrderTable 테이블1 = new OrderTable();
    OrderLineItem 주문내역 = new OrderLineItem();

    @BeforeEach
    void setUp() {
        주문.setOrderedTime(LocalDateTime.now());
        주문.setOrderStatus(OrderStatus.COOKING.name());
        주문.setOrderLineItems(Collections.singletonList(주문내역));
    }

    @Test
    @DisplayName("주문을 저장한다")
    void create() {
        // given
        given(menuDao.countByIdIn(any())).willReturn(1L);
        given(orderTableDao.findById(any())).willReturn(Optional.of(테이블1));
        given(orderDao.save(any())).willReturn(주문);
        given(orderLineItemDao.save(any())).willReturn(주문내역);

        // when
        Order actual = orderService.create(주문);

        // then
        assertThat(actual).isEqualTo(주문);
    }

    @Test
    @DisplayName("주문시 주문내역이 존재해야 한다")
    void create_EmptyOrderLineItemsError() {
        // given
        주문.setOrderLineItems(null);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> orderService.create(주문)
        );
    }

    @Test
    @DisplayName("주문시 주문내역의 메뉴는 모두 존재하는 메뉴여야 한다")
    void create_nonMenuError() {
        // given
        OrderLineItem 주문내역1 = new OrderLineItem();
        OrderLineItem 주문내역2 = new OrderLineItem();
        주문.setOrderLineItems(Arrays.asList(주문내역1, 주문내역2));

        given(menuDao.countByIdIn(any())).willReturn(1L);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> orderService.create(주문)
        );
    }

    @Test
    @DisplayName("주문시 주문테이블 정보를 가지고 있어야 한다")
    void create_nonExistTableError() {
        // given
        given(menuDao.countByIdIn(any())).willReturn(1L);
        given(orderTableDao.findById(any())).willReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> orderService.create(주문)
        );
    }

    @Test
    @DisplayName("주문 리스트를 조회한다")
    void list() {
        // given
        given(orderDao.findAll()).willReturn(Collections.singletonList(주문));
        given(orderLineItemDao.findAllByOrderId(any())).willReturn(Collections.singletonList(주문내역));

        // when
        List<Order> actual = orderService.list();

        // then
        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual.get(0).getOrderLineItems()).hasSize(1)
        );
    }

    @ParameterizedTest(name = "주문 상태를 {0}에서 {1}로 변경한다")
    @CsvSource(value = {"COOKING|MEAL", "COOKING|COMPLETION", "MEAL|COMPLETION"}, delimiter = '|')
    void changeOrderStatus(OrderStatus currentStatus, OrderStatus expected) {
        // given
        주문.setId(1L);
        주문.setOrderStatus(currentStatus.name());
        Order 변경하려는_주문 = new Order();
        변경하려는_주문.setOrderStatus(expected.name());
        given(orderDao.findById(any())).willReturn(Optional.ofNullable(주문));

        // when
        Order actual = orderService.changeOrderStatus(주문.getId(), 변경하려는_주문);

        // then
        assertThat(actual.getOrderStatus()).isEqualTo(expected.name());
    }

    @Test
    @DisplayName("현재 주문 상태가 계산 완료인 경우 변경할 수 없다")
    void changeOrderStatus_completion() {
        // given
        주문.setId(1L);
        주문.setOrderStatus(OrderStatus.COMPLETION.name());
        Order 변경하려는_주문 = new Order();
        변경하려는_주문.setOrderStatus(OrderStatus.COMPLETION.name());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> orderService.changeOrderStatus(주문.getId(), 변경하려는_주문)
        );
    }
}
