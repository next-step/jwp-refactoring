package kitchenpos.application;

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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
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

    private Order 접수_주문;
    private Order 완료_주문;
    private Menu 기본메뉴;
    private MenuGroup 조식메뉴그룹;
    private MenuProduct 기본메뉴_아메리카노;
    private MenuProduct 기본메뉴_스콘;
    private Product 아메리카노;
    private Product 스콘;
    private OrderTable 주문_테이블1;
    private OrderTable 주문_테이블2;
    private OrderLineItem 접수_주문_기본메뉴;
    private OrderLineItem 완료_주문_기본메뉴;

    @BeforeEach
    void setUp() {
        조식메뉴그룹 = new MenuGroup(1L, "조식메뉴그룹");
        아메리카노 = new Product(1L, "아메리카노", BigDecimal.valueOf(3000));
        스콘 = new Product(2L, "스콘", BigDecimal.valueOf(2000));
        기본메뉴_아메리카노 = new MenuProduct(1L, 1L, 아메리카노.getId(), 1);
        기본메뉴_스콘 = new MenuProduct(2L, 1L, 스콘.getId(), 1);
        기본메뉴 = new Menu(1L, "기본메뉴", BigDecimal.valueOf(6000), 조식메뉴그룹.getId(), Arrays.asList(기본메뉴_아메리카노, 기본메뉴_스콘));

        주문_테이블1 = new OrderTable(1L, null, 2, false);
        접수_주문 = new Order(1L, 주문_테이블1.getId(), OrderStatus.COOKING.name(), LocalDateTime.now());
        접수_주문_기본메뉴 = new OrderLineItem(1L, 접수_주문.getId(), 기본메뉴.getId(), 1);
        접수_주문.setOrderLineItems(Arrays.asList(접수_주문_기본메뉴));

        주문_테이블2 = new OrderTable(2L, null, 2, false);
        완료_주문 = new Order(2L, 주문_테이블2.getId(), OrderStatus.COMPLETION.name(), LocalDateTime.now());
        완료_주문_기본메뉴 = new OrderLineItem(2L, 완료_주문.getId(), 기본메뉴.getId(), 1);
        완료_주문.setOrderLineItems(Arrays.asList(완료_주문_기본메뉴));
    }

    @Test
    @DisplayName("주문을 등록한다.")
    void 주문_등록() {
        // given
        given(menuDao.countByIdIn(anyList())).willReturn(1L);
        given(orderTableDao.findById(주문_테이블1.getId())).willReturn(Optional.of(주문_테이블1));
        given(orderDao.save(접수_주문)).willReturn(접수_주문);

        // when
        Order saveOrder = orderService.create(접수_주문);

        // then
        assertThat(saveOrder.getId()).isEqualTo(접수_주문.getId());
        assertThat(saveOrder.getOrderTableId()).isEqualTo(접수_주문.getOrderTableId());
        assertThat(saveOrder.getOrderStatus()).isEqualTo(접수_주문.getOrderStatus());
        assertThat(saveOrder.getOrderedTime()).isEqualTo(접수_주문.getOrderedTime());
        assertThat(saveOrder.getOrderedTime()).isEqualTo(접수_주문.getOrderedTime());
    }

    @Test
    @DisplayName("주문 항목이 0개(1개 미만)이면 오류 발생한다.")
    void error_주문_등록_zero() {
        // given
        Order 주문항목zero = new Order(1L, 주문_테이블1.getId(), OrderStatus.COOKING.name(), LocalDateTime.now());

        // then
        assertThrows(IllegalArgumentException.class, () -> orderService.create(주문항목zero));
    }

    @Test
    @DisplayName("주문 항목 속 메뉴들이 등록되어 있지 않으면 오류 발생한다.")
    void error_주문_등록_NOT_REGISTER_안된_메뉴() {
        // given
        given(menuDao.countByIdIn(anyList())).willReturn(2L);

        // then
        assertThrows(IllegalArgumentException.class, () -> orderService.create(접수_주문));
    }

    @Test
    @DisplayName("주문 테이블이 등록되어 있지 않으면 오류 발생한다.")
    void error_주문_등록_NOT_REGISTER_주문_테이블() {
        // given
        given(menuDao.countByIdIn(anyList())).willReturn(1L);
        given(orderTableDao.findById(주문_테이블1.getId())).willReturn(Optional.ofNullable(null));

        // then
        assertThrows(IllegalArgumentException.class, () -> orderService.create(접수_주문));
    }

    @Test
    @DisplayName("주문 테이블이 비어있음이면 오류 발생한다.")
    void error_주문_등록_EMPTY_주문_테이블() {
        // given
        주문_테이블1.setEmpty(true);
        given(menuDao.countByIdIn(anyList())).willReturn(1L);
        given(orderTableDao.findById(접수_주문.getOrderTableId())).willReturn(Optional.of(주문_테이블1));

        // then
        assertThrows(IllegalArgumentException.class, () -> orderService.create(접수_주문));
    }

    @Test
    @DisplayName("주문 목록을 조회한다.")
    void 주문_목록_조회() {
        // given
        given(orderDao.findAll()).willReturn(Arrays.asList(접수_주문));
        given(orderLineItemDao.findAllByOrderId(접수_주문.getId())).willReturn(접수_주문.getOrderLineItems());

        // when
        List<Order> searchOrder = orderService.list();

        // then
        assertThat(searchOrder).containsExactly(접수_주문);
    }

    @Test
    @DisplayName("주문 상태를 수정한다.(COOKING -> MEAL)")
    void 주문_상태_수정() {
        // given
        given(orderDao.findById(anyLong())).willReturn(Optional.of(접수_주문));

        // when
        Order order = new Order(3L, 주문_테이블1.getId(), OrderStatus.MEAL.name(), LocalDateTime.now());
        Order changeOrder = orderService.changeOrderStatus(접수_주문.getId(), order);

        // then
        assertThat(changeOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @Test
    @DisplayName("주문이 등록이 되어 있지 않으면 주문 상태 수정 시, 오류 발생한다.")
    void error_주문_상태_수정_NOT_REGISTER_주문() {
        // given
        given(orderDao.findById(anyLong())).willReturn(Optional.ofNullable(null));

        // when
        Order order = new Order(3L, 주문_테이블1.getId(), OrderStatus.MEAL.name(), LocalDateTime.now());

        // then
        assertThrows(IllegalArgumentException.class, () -> orderService.changeOrderStatus(접수_주문.getId(), order));
    }

    @Test
    @DisplayName("주문이 상태가 완료면 주문 수정 시, 오류 발생한다.")
    void error_주문_상태_수정_주문_상태_완료() {
        // given
        given(orderDao.findById(anyLong())).willReturn(Optional.of(완료_주문));

        // when
        Order order = new Order(3L, 주문_테이블1.getId(), OrderStatus.MEAL.name(), LocalDateTime.now());

        // then
        assertThrows(IllegalArgumentException.class, () -> orderService.changeOrderStatus(완료_주문.getId(), order));
    }
}
