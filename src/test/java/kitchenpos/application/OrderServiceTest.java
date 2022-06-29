package kitchenpos.application;

import kitchenpos.dao.*;
import kitchenpos.domain.*;
import org.assertj.core.util.Lists;
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

import static kitchenpos.application.CommonTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    MenuDao menuDao;
    @Mock
    OrderDao orderDao;
    @Mock
    OrderLineItemDao orderLineItemDao;
    @Mock
    OrderTableDao orderTableDao;
    @InjectMocks
    OrderService orderService;

    private Product 싸이버거, 콜라;
    private MenuProduct 싱글세트_싸이버거, 싱글세트_콜라;
    private MenuGroup 맘스세트메뉴;
    private Menu 싱글세트;
    private OrderTable 주문_테이블;
    private OrderTable 빈_테이블;
    private OrderLineItem 주문항목;

    @BeforeEach
    void setUp() {
        싸이버거 = createProduct(1L, "싸이버거", 3500);
        콜라 = createProduct(2L, "콜라", 1500);
        싱글세트_싸이버거 = createMenuProduct(1, 1L, 싸이버거.getId(), 1);
        싱글세트_콜라 = createMenuProduct(2, 1L, 콜라.getId(), 1);
        맘스세트메뉴 = createMenuGroup(1L, "맘스세트메뉴");
        싱글세트 = createMenu(1L, 맘스세트메뉴.getId(), "싱글세트", 5000, Lists.newArrayList(싱글세트_싸이버거, 싱글세트_콜라));

        주문_테이블 = createOrderTable(1L, null, 4, false);
        빈_테이블 = createOrderTable(2L, null, 0, true);
        주문항목 = createOrderLineItem(1, 1L, 싱글세트.getId(), 1);
    }

    @DisplayName("주문을 등록한다.")
    @Test
    void create_success() {
        // given
        Order 주문 = createOrder(1L, 주문_테이블.getId(), OrderStatus.COOKING.name(), Lists.newArrayList(주문항목));
        given(menuDao.countByIdIn(anyList())).willReturn(1L);
        given(orderTableDao.findById(주문_테이블.getId())).willReturn(Optional.of(주문_테이블));
        given(orderDao.save(주문)).willReturn(주문);

        // when
        Order saved = orderService.create(주문);

        // then
        assertAll(
                () -> assertThat(saved).isNotNull(),
                () -> assertThat(saved).isEqualTo(주문),
                () -> assertThat(saved.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name())
        );
    }

    @DisplayName("주문을 등록에 실패한다. (주문항목이 비어있는 경우)")
    @Test
    void create_fail_empty_orderLineItem() {
        // given
        Order 주문 = createOrder(1L, 주문_테이블.getId(), OrderStatus.COOKING.name());

        // then
        assertThatThrownBy(() -> {
            orderService.create(주문);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 등록에 실패한다. (주문항목중 중복된 메뉴가 있는 경우)")
    @Test
    void create_fail_duplicated_menu() {
        // given
        Order 주문 = createOrder(1L, 주문_테이블.getId(), OrderStatus.COOKING.name(), Lists.newArrayList(주문항목, 주문항목));
        given(menuDao.countByIdIn(anyList())).willReturn(싱글세트.getId());

        // then
        assertThatThrownBy(() -> {
            orderService.create(주문);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 등록에 실패한다. (주문 테이블이 존재하지 않는 경우)")
    @Test
    void create_fail_empty_orderTable() {
        // given
        Order 주문 = createOrder(1L, 주문_테이블.getId(), OrderStatus.COOKING.name(), Lists.newArrayList(주문항목));
        given(menuDao.countByIdIn(anyList())).willReturn(싱글세트.getId());
        given(orderTableDao.findById(주문_테이블.getId())).willReturn(Optional.ofNullable(null));

        // then
        assertThatThrownBy(() -> {
            orderService.create(주문);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 등록에 실패한다. (주문 테이블이 빈 테이블인 경우)")
    @Test
    void create_fail_emptyTable() {
        // given
        Order 주문 = createOrder(1L, 빈_테이블.getId(), OrderStatus.COOKING.name(), Lists.newArrayList(주문항목));
        given(menuDao.countByIdIn(anyList())).willReturn(싱글세트.getId());
        given(orderTableDao.findById(빈_테이블.getId())).willReturn(Optional.of(빈_테이블));

        // then
        assertThatThrownBy(() -> {
            orderService.create(주문);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 목록을 조회한다.")
    @Test
    void list() {
        // given
        Order 주문 = createOrder(1L, 빈_테이블.getId(), OrderStatus.COOKING.name(), Lists.newArrayList(주문항목));
        given(orderDao.findAll()).willReturn(Arrays.asList(주문));

        // when
        List<Order> list = orderService.list();

        // then
        assertThat(list).containsExactly(주문);
    }

    @DisplayName("주문의 상태를 변경할 수 있다")
    @Test
    void changeOrderStatus_success() {
        // given
        Order 주문 = createOrder(1L, 빈_테이블.getId(), OrderStatus.COOKING.name(), Lists.newArrayList(주문항목));
        Order 주문_계산완료 = createOrder(1L, 빈_테이블.getId(), OrderStatus.COMPLETION.name(), Lists.newArrayList(주문항목));
        given(orderDao.findById(anyLong())).willReturn(Optional.of(주문));

        // when
        Order 변경된_주문 = orderService.changeOrderStatus(주문.getId(), 주문_계산완료);

        // then
        assertThat(변경된_주문.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

    @DisplayName("주문의 상태 변경에 실패한다. (존재하지 않는 주문인 경우)")
    @Test
    void changeOrderStatus_fail_emptyOrder() {
        // given
        Order 주문 = createOrder(1L, 빈_테이블.getId(), OrderStatus.COOKING.name(), Lists.newArrayList(주문항목));
        Order 주문_계산완료 = createOrder(1L, 빈_테이블.getId(), OrderStatus.COMPLETION.name(), Lists.newArrayList(주문항목));

        // then
        assertThatThrownBy(() -> {
            orderService.changeOrderStatus(주문.getId(), 주문_계산완료);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 상태 변경에 실패한다. (이미 계산완료된 주문인 경우)")
    @Test
    void changeOrderStatus_fail_completion() {
        // given
        Order 주문 = createOrder(1L, 빈_테이블.getId(), OrderStatus.COMPLETION.name(), Lists.newArrayList(주문항목));
        Order 주문_계산완료 = createOrder(1L, 빈_테이블.getId(), OrderStatus.COMPLETION.name(), Lists.newArrayList(주문항목));
        given(orderDao.findById(anyLong())).willReturn(Optional.of(주문));

        // then
        assertThatThrownBy(() -> {
            orderService.changeOrderStatus(주문.getId(), 주문_계산완료);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
