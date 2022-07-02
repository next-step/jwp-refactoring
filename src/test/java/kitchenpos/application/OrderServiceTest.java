package kitchenpos.application;

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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

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

    private OrderTable 저장된_주문테이블;
    private List<OrderLineItem> 주문항목_리스트;
    private OrderLineItem 첫번째_주문항목;
    private OrderLineItem 두번째_주문항목;

    private Order 생성할_주문;
    private Order 저장된_주문;

    @BeforeEach
    void setUp() {
        첫번째_주문항목 = new OrderLineItem(1L,2);
        두번째_주문항목 = new OrderLineItem(2L, 1);
        주문항목_리스트 = Arrays.asList(첫번째_주문항목, 두번째_주문항목);
        저장된_주문테이블 = new OrderTable(1L, null, 4, false);

        생성할_주문 = new Order(저장된_주문테이블.getId(), 주문항목_리스트);
        저장된_주문 = new Order(1L, 저장된_주문테이블.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), 주문항목_리스트);
    }

    @DisplayName("주문을 생성할 수 있다.")
    @Test
    void createOrder() {
        // given
        given(menuDao.countByIdIn(anyList()))
                .willReturn((long) 주문항목_리스트.size());
        given(orderTableDao.findById(생성할_주문.getOrderTableId()))
                .willReturn(Optional.of(저장된_주문테이블));
        given(orderDao.save(생성할_주문))
                .willReturn(저장된_주문);
        given(orderLineItemDao.save(eq(첫번째_주문항목)))
                .willReturn(첫번째_주문항목);
        given(orderLineItemDao.save(eq(두번째_주문항목)))
                .willReturn(두번째_주문항목);

        // when
        Order 생성된_주문 = orderService.create(생성할_주문);

        // then
        주문_생성_성공(생성된_주문, 생성할_주문);
    }


    @DisplayName("주문 항목이 존재하지 않으면 주문 생성에 실패한다.")
    @Test
    void createOrderFailsWhenEmptyOrderLineItems() {
        // given
        생성할_주문 = new Order(저장된_주문테이블.getId(), null);

        // when & then
        주문_생성_실패(생성할_주문);
    }

    @DisplayName("중복된 메뉴가 있으면 주문 생성에 실패한다.")
    @Test
    void createOrderFailsWhenDuplicateMenu() {
        // given
        Long 유니크한_메뉴개수 = 1L;
        given(menuDao.countByIdIn(anyList()))
                .willReturn(유니크한_메뉴개수);

        // when & then
        주문_생성_실패(생성할_주문);
    }

    @DisplayName("주문 테이블이 존재하지 않으면 주문 생성에 실패한다.")
    @Test
    void createOrderFailsWhenNoOrderTable() {
        // given
        Long 존재하지_않는_주문테이블ID = 1000L;
        생성할_주문 = new Order(존재하지_않는_주문테이블ID, 주문항목_리스트);
        given(menuDao.countByIdIn(anyList()))
                .willReturn((long) 주문항목_리스트.size());

        // when & then
        주문_생성_실패(생성할_주문);
    }

    @DisplayName("주문 테이블이 빈 테이블이면 주문 생성에 실패한다.")
    @Test
    void createOrderFailsWhenEmptyOrderTable() {
        // given
        OrderTable 빈_주문테이블 = new OrderTable(2L, null, 4, true);
        생성할_주문 = new Order(빈_주문테이블.getId(), 주문항목_리스트);

        given(menuDao.countByIdIn(anyList()))
                .willReturn((long) 주문항목_리스트.size());
        given(orderTableDao.findById(생성할_주문.getOrderTableId()))
                .willReturn(Optional.of(빈_주문테이블));

        // when & then
        주문_생성_실패(생성할_주문);
    }

    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void listOrders() {
        // given
        List<Order> 조회할_주문_목록 = Arrays.asList(저장된_주문);
        given(orderDao.findAll())
                .willReturn(조회할_주문_목록);
        given(orderLineItemDao.findAllByOrderId(anyLong()))
                .willReturn(주문항목_리스트);

        // when
        List<Order> 조회된_주문_목록 = orderService.list();

        // then
        주문_목록_조회_성공(조회된_주문_목록, 조회할_주문_목록);
    }

    @DisplayName("주문 테이블 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatus() {
        // given
        Long 상태_변경할_주문ID = 1L;
        Order 상태_변경_전_주문 = new Order(상태_변경할_주문ID, 저장된_주문테이블.getId(), OrderStatus.MEAL.name(), LocalDateTime.now(), 주문항목_리스트);
        Order 상태_변경_후_예상_주문 = new Order(상태_변경할_주문ID, 저장된_주문테이블.getId(), OrderStatus.COMPLETION.name(), LocalDateTime.now(), 주문항목_리스트);
        given(orderDao.findById(상태_변경할_주문ID))
                .willReturn(Optional.of(상태_변경_전_주문));
        given(orderDao.save(any()))
                .willReturn(상태_변경_후_예상_주문);
        given(orderLineItemDao.findAllByOrderId(상태_변경할_주문ID))
                .willReturn(주문항목_리스트);

        // when
        Order 상태_변경_완료_주문 = orderService.changeOrderStatus(상태_변경할_주문ID, new Order(OrderStatus.COMPLETION.name()));

        // then
        주문_상태_변경_성공(상태_변경_완료_주문, 상태_변경_후_예상_주문);
    }

    @DisplayName("주문이 존재하지 않으면 상태 변경에 실패한다.")
    @Test
    void changeOrderStatusFailsWhenNoOrder() {
        Long 존재하지_않는_주문ID = 1000L;

        // when & then
        주문_상태_변경_실패(존재하지_않는_주문ID, new Order(OrderStatus.COMPLETION.name()));
    }

    @DisplayName("'계산 완료' 상태인 경우 상태 변경에 실패한다.")
    @Test
    void changeOrderStatusFailsWhenCompleted() {
        // given
        Order 완료된_주문 = new Order(1L, 저장된_주문테이블.getId(), OrderStatus.COMPLETION.name(), LocalDateTime.now(), 주문항목_리스트);
        given(orderDao.findById(완료된_주문.getId()))
                .willReturn(Optional.of(완료된_주문));

        // when & then
        주문_상태_변경_실패(완료된_주문.getId(), new Order(OrderStatus.COMPLETION.name()));
    }

    private void 주문_생성_성공(Order 생성된_주문, Order 생성할_주문) {
        assertAll(
                () -> assertThat(생성된_주문.getId())
                        .isNotNull(),
                () -> assertThat(생성된_주문.getOrderedTime())
                        .isNotNull(),
                () -> assertThat(생성된_주문.getOrderStatus())
                        .isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(생성된_주문.getOrderTableId())
                        .isEqualTo(생성할_주문.getOrderTableId()),
                () -> assertThat(생성된_주문.getOrderLineItems())
                        .containsExactlyElementsOf(생성할_주문.getOrderLineItems())
        );
    }

    private void 주문_생성_실패(Order 생성할_주문) {
        assertThatThrownBy(() -> orderService.create(생성할_주문))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    private void 주문_목록_조회_성공(List<Order> 조회된_주문_목록, List<Order> 조회할_주문_목록) {
        assertThat(조회된_주문_목록)
                .containsExactlyElementsOf(조회할_주문_목록);
    }

    private void 주문_상태_변경_성공(Order 상태_변경_완료_주문, Order 상태_변경_후_예상_주문) {
        assertThat(상태_변경_완료_주문.getOrderStatus())
                .isEqualTo(상태_변경_후_예상_주문.getOrderStatus());
    }

    private void 주문_상태_변경_실패(Long 주문ID, Order 주문) {
        assertThatThrownBy(() -> orderService.changeOrderStatus(주문ID, 주문))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
