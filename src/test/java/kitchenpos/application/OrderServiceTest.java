package kitchenpos.application;

import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderLineItemDao;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static kitchenpos.domain.MenuTest.메뉴_생성;
import static kitchenpos.domain.OrderLineItemTest.주문_목록_생성;
import static kitchenpos.domain.OrderTableTest.주문_테이블_생성;
import static kitchenpos.domain.OrderTest.주문_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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

    @DisplayName("주문에 주문 목록이 포함되어 있지 않으면 예외가 발생해야 한다")
    @Test
    void createOrderByNotIncludeOrderLinesTest() {
        // given
        Order 빈_주문_목록이_포함된_주문 = 주문_생성(0L, OrderStatus.COOKING, Collections.emptyList());

        // then
        주문_생성_실패됨(() -> orderService.create(빈_주문_목록이_포함된_주문));
    }

    @DisplayName("주문 목록의 저장된 메뉴 수와 요청한 주문의 주문 목록의 수가 다르면 예외가 발생해야 한다")
    @Test
    void createOrderByNotMatchedMenuCountAndOrderLineCountTest() {
        // given
        Order 주문 = 주문_생성(0L, OrderStatus.COOKING, Arrays.asList(
                주문_목록_생성(0L, 1L, 1),
                주문_목록_생성(0L, 2L, 1)
        ));
        when(menuDao.countByIdIn(any())).thenReturn(1L);

        // then
        주문_생성_실패됨(() -> orderService.create(주문));
    }

    @DisplayName("저장되지 않은 주문 테이블 정보로 주문이 생성되면 예외가 발생해야 한다")
    @Test
    void createOrderByNotSavedOrderTableTest() {
        // given
        Order 주문 = 주문_생성(0L, OrderStatus.COOKING, Collections.singletonList(주문_목록_생성(0L, 1L, 1)));
        when(menuDao.countByIdIn(any())).thenReturn(1L);
        when(orderTableDao.findById(any())).thenReturn(Optional.empty());

        // then
        주문_생성_실패됨(() -> orderService.create(주문));
    }

    @DisplayName("빈 주문 테이블에 주문을 추가하면 예외가 발생해야 한다")
    @Test
    void createOrderByEmptyOrderTableTest() {
        // given
        Order 주문 = 주문_생성(0L, OrderStatus.COOKING, Collections.singletonList(주문_목록_생성(0L, 1L, 1)));
        when(menuDao.countByIdIn(any())).thenReturn(1L);
        when(orderTableDao.findById(any())).thenReturn(Optional.of(주문_테이블_생성(0L, 1, true)));

        // then
        주문_생성_실패됨(() -> orderService.create(주문));
    }

    @DisplayName("정상 상태의 주문을 저장하면 정상 저장되어야 한다")
    @Test
    void createOrderTest() {
        // given
        Menu 메뉴 = 메뉴_생성("메뉴", 1_000, 0L, Collections.emptyList());
        OrderTable 주문_테이블 = 주문_테이블_생성(0L, 2, false);
        OrderLineItem 주문_목록 = 주문_목록_생성(0L, 메뉴.getId(), 1);
        Order 주문 = 주문_생성(0L, OrderStatus.COOKING, Collections.singletonList(주문_목록));
        when(menuDao.countByIdIn(any())).thenReturn(1L);
        when(orderTableDao.findById(any())).thenReturn(Optional.of(주문_테이블));
        when(orderDao.save(주문)).thenReturn(주문);
        when(orderLineItemDao.save(주문_목록)).thenReturn(주문_목록);

        // when
        Order 생성_주문 = orderService.create(주문);

        // then
        주문_정상_생성됨(생성_주문, 주문);
    }

    @DisplayName("주문 목록을 조회하면 정상 조회되어야 한다")
    @Test
    void findAllOrderTest() {
        // given
        List<Order> 주문_리스트 = Arrays.asList(
                주문_생성(0L, OrderStatus.COOKING, Collections.emptyList()),
                주문_생성(0L, OrderStatus.COOKING, Collections.emptyList()),
                주문_생성(0L, OrderStatus.COOKING, Collections.emptyList()),
                주문_생성(0L, OrderStatus.COOKING, Collections.emptyList())
        );
        when(orderDao.findAll()).thenReturn(주문_리스트);

        // when
        List<Order> 주문_목록_조회_리스트 = orderService.list();

        // then
        assertThat(주문_목록_조회_리스트.size()).isGreaterThanOrEqualTo(주문_리스트.size());
        assertThat(주문_목록_조회_리스트).containsAll(주문_리스트);
    }

    @DisplayName("없는 주문의 주문 상태를 변경하면 예외가 발생해야 한다")
    @Test
    void changeOrderStateByNotSavedOrderTest() {
        주문_상태_변경_실패됨(() -> orderService.changeOrderStatus(0L, any()));
    }

    @DisplayName("완료된 주문의 상태를 변경하면 예외가 발생해야 한다")
    @Test
    void changeOrderStatueByCompletionOrderTest() {
        // given
        Order 주문 = 주문_생성(0L, OrderStatus.COMPLETION, Collections.emptyList());
        when(orderDao.findById(any())).thenReturn(Optional.of(주문));

        // then
        주문_상태_변경_실패됨(() -> orderService.changeOrderStatus(주문.getId(), 주문));
    }

    @DisplayName("주문 상태 변경 시 변경한 상태로 정상 변경되어야 한다")
    @Test
    void changeOrderStatusTest() {
        // given
        Order 주문 = 주문_생성(0L, OrderStatus.COOKING, Collections.emptyList());
        Order 변경할_주문 = 주문_생성(0L, OrderStatus.MEAL, Collections.emptyList());
        when(orderDao.findById(any())).thenReturn(Optional.of(주문));

        // when
        Order 주문_상태_변경_결과 = orderService.changeOrderStatus(0L, 변경할_주문);

        // then
        주문_상태_변경_성공됨(주문_상태_변경_결과.getOrderStatus(), 변경할_주문.getOrderStatus());
    }

    void 주문_생성_실패됨(Runnable runnable) {
        assertThatIllegalArgumentException().isThrownBy(runnable::run);
    }

    void 주문_정상_생성됨(Order source, Order target) {
        assertThat(source.getOrderTableId()).isEqualTo(target.getOrderTableId());
        assertThat(source.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        assertThat(source.getOrderLineItems()).isEqualTo(target.getOrderLineItems());
    }

    void 주문_상태_변경_실패됨(Runnable runnable) {
        assertThatIllegalArgumentException().isThrownBy(runnable::run);
    }

    void 주문_상태_변경_성공됨(OrderStatus sourceStatus, OrderStatus targetSource) {
        assertThat(sourceStatus).isEqualTo(targetSource);
    }
}
