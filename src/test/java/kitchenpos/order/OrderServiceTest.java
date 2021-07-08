package kitchenpos.order;

import kitchenpos.application.OrderService;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.*;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private MenuDao menuDao;

    @Mock
    OrderDao orderDao;

    @Mock
    OrderLineItemDao orderLineItemDao;

    @Mock
    OrderTableDao orderTableDao;

    @InjectMocks
    OrderService orderService;

    private Order 첫번째_주문;
    private OrderTable 첫번째_테이블;
    private OrderLineItem 주문_항목_첫번째;
    private Menu 첫번째_메뉴;

    @BeforeEach
    void setUp() {
        첫번째_테이블 = new OrderTable();
        첫번째_테이블.setId(1L);

        첫번째_주문 = new Order();
        첫번째_주문.setId(1L);

        첫번째_메뉴 = new Menu();
        첫번째_메뉴.setId(1L);

        주문_항목_첫번째 = new OrderLineItem();
        주문_항목_첫번째.setMenuId(첫번째_메뉴.getId());
    }

    @DisplayName("주문을 등록할 수 있다")
    @Test
    void 주문_등록() {
        //Given
        첫번째_주문.setOrderLineItems(Arrays.asList(주문_항목_첫번째));
        첫번째_주문.setOrderTableId(첫번째_테이블.getId());

        when(menuDao.countByIdIn(Arrays.asList(첫번째_메뉴.getId()))).thenReturn((long) 첫번째_주문.getOrderLineItems().size());
        when(orderTableDao.findById(첫번째_테이블.getId())).thenReturn(Optional.of(첫번째_테이블));
        when(orderDao.save(첫번째_주문)).thenReturn(첫번째_주문);
        when(orderLineItemDao.save(주문_항목_첫번째)).thenReturn(주문_항목_첫번째);

        //When
        Order 생성된_주문 = orderService.create(첫번째_주문);

        //Then
        assertThat(생성된_주문.getId()).isNotNull();
    }

    @DisplayName("주문 항목없이 주문 생성시, 예외 발생한다")
    @Test
    void 주문항목_없이_주문_생성시_예외발생() {
        //Given
        첫번째_주문.setOrderLineItems(null);

        //When + Then
        assertThatThrownBy(() -> orderService.create(첫번째_주문))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("중복된 메뉴를 가진 주문항목들로 주문 생성시, 예외 발생한다")
    @Test
    void 중복된_메뉴의_주문항목들로_주문_생성시_예외발생() {
        //Given
        OrderLineItem 주문_항목_두번째 = new OrderLineItem();
        주문_항목_두번째.setMenuId(주문_항목_첫번째.getMenuId());

        첫번째_주문.setOrderLineItems(Arrays.asList(주문_항목_첫번째, 주문_항목_두번째));

        //When + Then
        assertThatThrownBy(() -> orderService.create(첫번째_주문))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되지 않은 주문 테이블로 주문 생성시, 예외 발생한다")
    @Test
    void 등록되지_않은_주문_테이블로_주문_생성시_예외발생() {
        //Given
        Long 생성되지_않은_주문테이블_id = 99L;
        첫번째_주문.setOrderLineItems(Arrays.asList(주문_항목_첫번째));
        첫번째_주문.setOrderTableId(생성되지_않은_주문테이블_id);
        when(menuDao.countByIdIn(Arrays.asList(첫번째_메뉴.getId()))).thenReturn((long) 첫번째_주문.getOrderLineItems().size());

        //When + Then
        assertThatThrownBy(() -> orderService.create(첫번째_주문))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("비어있는 주문 테이블로 주문 생성시, 예외 발생한다")
    @Test
    void 비어있는_주문_테이블로_주문_생성시_예외발생() {
        //Given
        첫번째_주문.setOrderLineItems(Arrays.asList(주문_항목_첫번째));
        첫번째_주문.setOrderTableId(첫번째_테이블.getId());
        when(menuDao.countByIdIn(Arrays.asList(첫번째_메뉴.getId()))).thenReturn((long) 첫번째_주문.getOrderLineItems().size());

        //When + Then
        assertThatThrownBy(() -> orderService.create(첫번째_주문))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 목록을 조회할 수 있다")
    @Test
    void 주문_목록_조회() {
        //Given
        첫번째_주문.setOrderLineItems(Arrays.asList(주문_항목_첫번째));
        when(orderDao.findAll()).thenReturn(Arrays.asList(첫번째_주문));

        //When
        List<Order> 조회된_주문 = orderService.list();

        //Then
        assertThat(조회된_주문).hasSize(1)
                .containsExactly(첫번째_주문);
    }

    @DisplayName("주문 상태를 변경할 수 있다")
    @Test
    void 주문상태_변경() {
        //Given
        Order 변경될_상태를_담은_오더 = new Order();
        변경될_상태를_담은_오더.setOrderStatus(OrderStatus.COOKING.name());

        when(orderDao.findById(첫번째_주문.getId())).thenReturn(Optional.of(첫번째_주문));

        //When
        Order 변경된_주문 = orderService.changeOrderStatus(첫번째_주문.getId(), 변경될_상태를_담은_오더);

        //Then
        assertThat(변경된_주문.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @DisplayName("등록되지 않은 주문의 상태 변경시, 예외발생한다")
    @Test
    void 등록되지_않은_주문의_주문상태_변경() {
        //Given
        Order 변경될_상태를_담은_오더 = new Order();
        변경될_상태를_담은_오더.setOrderStatus(OrderStatus.COOKING.name());

        when(orderDao.findById(첫번째_주문.getId())).thenReturn(Optional.empty());

        //When + Then
        assertThatThrownBy(() -> orderService.changeOrderStatus(첫번째_주문.getId(), 변경될_상태를_담은_오더))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("기존 주문 상태가 '완료'일 때 상태 변경시, 예외발생한다")
    @Test
    void 완료처리된_주문의_주문상태_변경() {
        //Given
        Order 변경될_상태를_담은_오더 = new Order();
        변경될_상태를_담은_오더.setOrderStatus(OrderStatus.COOKING.name());

        첫번째_주문.setOrderStatus(OrderStatus.COMPLETION.name());
        when(orderDao.findById(첫번째_주문.getId())).thenReturn(Optional.empty());

        //When + Then
        assertThatThrownBy(() -> orderService.changeOrderStatus(첫번째_주문.getId(), 변경될_상태를_담은_오더))
                .isInstanceOf(IllegalArgumentException.class);
    }
}