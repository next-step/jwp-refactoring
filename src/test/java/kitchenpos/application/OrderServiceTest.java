package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 서비스")
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

    @DisplayName("전체 주문을 조회할 수 있다.")
    @Test
    void 전체_주문_조회() {
        // given
        OrderLineItem 주문_항목 = new OrderLineItem(1L, 1);

        OrderTable 주문_테이블 = new OrderTable(1L, 5);
        Order 주문 = new Order(1L, 주문_테이블.getId(), Collections.singletonList(주문_항목));

        given(orderDao.findAll()).willReturn(Collections.singletonList(주문));
        given(orderLineItemDao.findAllByOrderId(eq(주문.getId()))).willReturn(Collections.singletonList(주문_항목));

        // when
        List<Order> orders = orderService.list();

        // then
        assertThat(orders).hasSize(1);
    }

    @DisplayName("주문 생성")
    @Nested
    class 주문_생성 {
        @DisplayName("주문을 생성할 수 있고 생성된 주문의 주문 상태는 조리이다.")
        @Test
        void 주문_생성_성공() {
            // given
            OrderLineItem 주문_항목 = new OrderLineItem(1L, 1);

            OrderTable 주문_테이블 = new OrderTable(1L, 5);
            Order 주문 = new Order(주문_테이블.getId(), Collections.singletonList(주문_항목));

            given(menuDao.countByIdIn(anyList())).willReturn(1L);
            given(orderTableDao.findById(eq(주문_테이블.getId()))).willReturn(Optional.ofNullable(주문_테이블));
            given(orderDao.save(any(Order.class))).willReturn(
                    new Order(1L, 주문_테이블.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(),
                            Collections.singletonList(주문_항목)));

            // when
            Order savedOrder = orderService.create(주문);

            // then
            assertAll(() -> assertThat(savedOrder).isNotNull(), () -> assertThat(savedOrder.getId()).isNotNull(),
                    () -> assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()));
        }

        @DisplayName("주문 생성 실패")
        @Nested
        class 주문_생성_실패 {
            @DisplayName("주문 항목이 없는 주문은 생성할 수 없다.")
            @Test
            void 주문_항목이_없는_주문_생성() {
                // given
                OrderTable 주문_테이블 = new OrderTable(1L, 5);
                Order 주문 = new Order(주문_테이블.getId());

                // when / then
                assertThatThrownBy(() -> orderService.create(주문)).isInstanceOf(IllegalArgumentException.class);
            }

            @DisplayName("중복된 메뉴의 주문 항목을 가진 주문은 생성할 수 없다.")
            @Test
            void 중복된_메뉴의_주문_항목을_가진_주문_생성() {
                // given
                OrderLineItem 주문_항목 = new OrderLineItem(1L, 1);
                OrderLineItem 중복된_메뉴의_주문_항목 = new OrderLineItem(1L, 3);

                OrderTable 주문_테이블 = new OrderTable(1L, 5);
                Order 주문 = new Order(주문_테이블.getId(), Arrays.asList(주문_항목, 중복된_메뉴의_주문_항목));

                given(menuDao.countByIdIn(anyList())).willReturn(1L);

                // when / then
                assertThatThrownBy(() -> orderService.create(주문)).isInstanceOf(IllegalArgumentException.class);
            }

            @DisplayName("존재하지 않는 주문 테이블로 주문을 생성할 수 없다.")
            @Test
            void 존재하지_않는_주문_테이블을_포함한_주문_생성() {
                // given
                OrderLineItem 주문_항목 = new OrderLineItem(1L, 1);

                Long 존재하지_않는_주문_테이블_아이디 = 99999L;
                Order 주문 = new Order(존재하지_않는_주문_테이블_아이디, Collections.singletonList(주문_항목));

                given(menuDao.countByIdIn(anyList())).willReturn(1L);
                given(orderTableDao.findById(eq(존재하지_않는_주문_테이블_아이디))).willReturn(Optional.empty());

                // when / then
                assertThatThrownBy(() -> orderService.create(주문)).isInstanceOf(IllegalArgumentException.class);
            }

            @DisplayName("빈 주문 테이블로 주문을 생성할 수 없다.")
            @Test
            void 빈_주문_테이블로_주문_생성() {
                // given
                OrderLineItem 주문_항목 = new OrderLineItem(1L, 1);

                OrderTable 빈_주문_테이블 = new OrderTable(1L, 5, Boolean.TRUE);
                Order 주문 = new Order(빈_주문_테이블.getId(), Collections.singletonList(주문_항목));

                given(menuDao.countByIdIn(anyList())).willReturn(1L);
                given(orderTableDao.findById(eq(빈_주문_테이블.getId()))).willReturn(Optional.ofNullable(빈_주문_테이블));

                // when / then
                assertThatThrownBy(() -> orderService.create(주문)).isInstanceOf(IllegalArgumentException.class);
            }
        }
    }

    @DisplayName("주문 상태 변경")
    @Nested
    class 주문_상태_변경 {
        @DisplayName("생성된 주문의 상태를 변경할 수 있다.")
        @Test
        void 주문_상태_변경_성공() {
            OrderLineItem 주문_항목 = new OrderLineItem(1L, 1);

            OrderTable 주문_테이블 = new OrderTable(1L, 5);
            Order 주문 = new Order(1L, 주문_테이블.getId(), OrderStatus.COOKING.name(), Collections.singletonList(주문_항목));

            given(orderDao.findById(eq(주문.getId()))).willReturn(Optional.of(
                    new Order(1L, 주문_테이블.getId(), OrderStatus.MEAL.name(), Collections.singletonList(주문_항목))));

            // when
            Order 변경된_주문 = orderService.changeOrderStatus(주문.getId(), 주문);

            // then
            assertThat(변경된_주문.getOrderStatus()).isEqualTo(주문.getOrderStatus());
        }

        @DisplayName("생성되지 않은 주문의 주문 상태를 변경할 수 없다.")
        @Test
        void 생성되지_않은_주문의_주문_상태_변경() {
            Long 존재하지_않는_주문_아이디 = 9999L;
            Order 주문 = new Order(존재하지_않는_주문_아이디, 1L, OrderStatus.COOKING.name(), Collections.emptyList());

            given(orderDao.findById(eq(주문.getId()))).willReturn(Optional.empty());

            // when / then
            assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), 주문)).isInstanceOf(
                    IllegalArgumentException.class);
        }

        @DisplayName("계산 완료된 주문은 변경할 수 없다.")
        @Test
        void 계산_완료_주문_변경() {
            // given
            OrderLineItem 주문_항목 = new OrderLineItem(1L, 1);

            OrderTable 주문_테이블 = new OrderTable(1L, 5);
            Order 주문 = new Order(1L, 주문_테이블.getId(), OrderStatus.COOKING.name(), Collections.singletonList(주문_항목));

            given(orderDao.findById(eq(주문.getId()))).willReturn(Optional.of(
                    new Order(1L, 주문_테이블.getId(), OrderStatus.COMPLETION.name(), Collections.singletonList(주문_항목))));

            // when / then
            assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), 주문)).isInstanceOf(
                    IllegalArgumentException.class);
        }
    }
}