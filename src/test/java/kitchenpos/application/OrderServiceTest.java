package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.*;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@DisplayName("주문 비즈니스 테스트")
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

    private OrderTable 첫번째_주문_테이블;
    private OrderTable 두번째_주문_테이블;
    private OrderLineItem 첫번째_주문_항목;
    private OrderLineItem 두번째_주문_항목;
    private Order 첫번째_주문;
    private Order 두번째_주문;

    @BeforeEach
    void setUp() {
        MenuGroup 한마리메뉴_메뉴그룹 = MenuGroup.of(1L, "한마리메뉴");
        Product 후라이드치킨_상품 = new Product(1L, "후라이드치킨", new Price(BigDecimal.valueOf(16_000L)));
        Product 콜라_상품 = new Product(2L, "콜라", new Price(BigDecimal.valueOf(2_000L)));
        Menu 후라이드치킨_메뉴 = Menu.of(1L, "후라이드치킨", 후라이드치킨_상품.getPrice().value(), 한마리메뉴_메뉴그룹.getId());
        Menu 콜라_메뉴 = Menu.of(2L, "콜라", 콜라_상품.getPrice().value(), 한마리메뉴_메뉴그룹.getId());

        첫번째_주문_테이블 = OrderTable.of(1L, null, 4, false);
        두번째_주문_테이블 = OrderTable.of(2L, null, 2, false);
        첫번째_주문 = Order.of(1L, 첫번째_주문_테이블.getId(), null, null, null);
        두번째_주문 = Order.of(2L, 두번째_주문_테이블.getId(), null, null, null);
        첫번째_주문_항목 = OrderLineItem.of(1L, 첫번째_주문.getId(), 후라이드치킨_메뉴.getId(), 1);
        두번째_주문_항목 = OrderLineItem.of(2L, 첫번째_주문.getId(), 콜라_메뉴.getId(), 1);
    }

    @DisplayName("주문 항목은 최소 1개 이상 있어야 한다.")
    @Test
    void 주문_항목은_최소_1개_이상_있어야_한다() {
        // given
        첫번째_주문.setOrderLineItems(Collections.emptyList());

        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(첫번째_주문));
    }

    @DisplayName("주문 항목 속 메뉴들은 모두 등록된 메뉴여야 한다.")
    @Test
    void 주문_항목_속_메뉴들은_모두_등록된_메뉴여야_한다() {
        // given
        첫번째_주문.setOrderLineItems(Arrays.asList(첫번째_주문_항목, 두번째_주문_항목));
        List<Long> menuIds = 첫번째_주문.getOrderLineItems().stream().map(OrderLineItem::getMenuId).collect(Collectors.toList());
        when(menuDao.countByIdIn(menuIds)).thenReturn(1L);

        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(첫번째_주문));
    }

    @DisplayName("주문 테이블은 등록된 테이블이어야 한다.")
    @Test
    void 주문_테이블은_등록된_테이블이어야_한다() {
        // given
        첫번째_주문.setOrderLineItems(Arrays.asList(첫번째_주문_항목, 두번째_주문_항목));
        when(menuDao.countByIdIn(anyList())).thenReturn(2L);
        when(orderTableDao.findById(첫번째_주문.getOrderTableId())).thenReturn(Optional.empty());

        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(첫번째_주문));
    }

    @DisplayName("주문 테이블이 빈 테이블이 아니어야 한다.")
    @Test
    void 주문_테이블이_빈_테이블이_아니어야_한다() {
        // given
        첫번째_주문_테이블.setEmpty(true);
        첫번째_주문.setOrderLineItems(Arrays.asList(첫번째_주문_항목, 두번째_주문_항목));
        when(menuDao.countByIdIn(anyList())).thenReturn(2L);
        when(orderTableDao.findById(첫번째_주문.getOrderTableId())).thenReturn(Optional.of(첫번째_주문_테이블));

        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(첫번째_주문));
    }

    @DisplayName("주문을 생성할 수 있다.")
    @Test
    void 주문을_생성할_수_있다() {
        // given
        첫번째_주문.setOrderLineItems(Arrays.asList(첫번째_주문_항목, 두번째_주문_항목));
        when(menuDao.countByIdIn(anyList())).thenReturn(2L);
        when(orderTableDao.findById(첫번째_주문.getOrderTableId())).thenReturn(Optional.of(첫번째_주문_테이블));
        when(orderDao.save(첫번째_주문)).thenReturn(첫번째_주문);
        when(orderLineItemDao.save(첫번째_주문_항목)).thenReturn(첫번째_주문_항목);
        when(orderLineItemDao.save(두번째_주문_항목)).thenReturn(두번째_주문_항목);

        // when
        Order 저장된_주문 = orderService.create(첫번째_주문);

        // then
        assertAll(() -> {
            assertThat(저장된_주문.getId()).isEqualTo(첫번째_주문.getId());
            assertThat(저장된_주문.getOrderTableId()).isEqualTo(첫번째_주문.getOrderTableId());
            assertThat(저장된_주문.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
            assertThat(저장된_주문.getOrderedTime()).isNotNull();
            assertThat(저장된_주문.getOrderLineItems()).hasSize(2);
        });
    }

    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void 주문_목록을_조회할_수_있다() {
        // given
        when(orderDao.findAll()).thenReturn(Arrays.asList(첫번째_주문, 두번째_주문));

        // when
        List<Order> 조회된_주문_목록 = orderService.list();

        // then
        assertAll(() -> {
            assertThat(조회된_주문_목록).hasSize(2);
            assertThat(조회된_주문_목록).containsExactly(첫번째_주문, 두번째_주문);
        });
    }

    @DisplayName("주문은 등록된 주문이어야 한다.")
    @Test
    void 주문은_등록된_주문이어야_한다() {
        // given
        when(orderDao.findById(첫번째_주문.getId())).thenReturn(Optional.empty());

        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.changeOrderStatus(첫번째_주문.getId(), 첫번째_주문));
    }

    @DisplayName("이미 완료된 주문은 상태를 수정할 수 없다.")
    @Test
    void 이미_완료된_주문은_상태를_수정할_수_없다() {
        // given
        첫번째_주문.setOrderStatus(OrderStatus.COMPLETION.name());
        when(orderDao.findById(첫번째_주문.getId())).thenReturn(Optional.of(첫번째_주문));

        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.changeOrderStatus(첫번째_주문.getId(), 첫번째_주문));
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @Test
    void 주문_상태를_변경할_수_있다() {
        // given
        첫번째_주문.setOrderStatus(OrderStatus.MEAL.name());
        when(orderDao.findById(첫번째_주문.getId())).thenReturn(Optional.of(첫번째_주문));
        when(orderDao.save(첫번째_주문)).thenReturn(첫번째_주문);

        // when
        Order 저장된_주문 = orderService.changeOrderStatus(첫번째_주문.getId(), 첫번째_주문);

        // then
        assertAll(() -> {
            assertThat(저장된_주문.getId()).isEqualTo(첫번째_주문.getId());
            assertThat(저장된_주문.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
        });
    }
}
