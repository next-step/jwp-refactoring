package kitchenpos.application;

import kitchenpos.application.fixture.MenuFixture;
import kitchenpos.application.fixture.MenuProductFixture;
import kitchenpos.application.fixture.OrderFixture;
import kitchenpos.application.fixture.OrderLineItemFixture;
import kitchenpos.application.fixture.OrderTableFixture;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.fixture.MenuGroupFixture;
import kitchenpos.product.domain.Product;
import kitchenpos.product.fixture.ProductFixture;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("주문 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
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

    private Menu 더블강정;
    private OrderTable 테이블;
    private OrderTable 빈_테이블;
    private OrderLineItem 생성된_주문_항목;
    private Order 생성된_주문;
    private Order 식사중인_주문;
    private Order 계산된_주문;

    @BeforeEach
    void setup() {
        Product 강정치킨 = ProductFixture.create(1L, "강정치킨", BigDecimal.valueOf(17_000));
        MenuGroup 추천메뉴 = MenuGroupFixture.create(1L, "추천메뉴");
        MenuProduct 메뉴_상품 = MenuProductFixture.create(강정치킨, 2);

        더블강정 = MenuFixture.create(1L, "더블강정", BigDecimal.valueOf(32_000), 추천메뉴, 메뉴_상품);
        테이블 = OrderTableFixture.create(1L, null, 4, false);
        빈_테이블 = OrderTableFixture.create(2L, null, 4, true);

        생성된_주문_항목 = OrderLineItemFixture.create(1L, 1L, 1L, 1L);
        생성된_주문 = OrderFixture.create(1L, 1L, "COOKING", 생성된_주문_항목);
        식사중인_주문 = OrderFixture.create(2L, 1L, "MEAL", 생성된_주문_항목);
        계산된_주문 = OrderFixture.create(3L, 1L, "COMPLETION", 생성된_주문_항목);
    }

    @DisplayName("주문 목록 조회 확인")
    @Test
    void 주문_목록_조회_확인() {
        // given
        given(orderDao.findAll()).willReturn(Collections.singletonList(생성된_주문));
        given(orderLineItemDao.findAllByOrderId(any())).willReturn(Collections.singletonList(생성된_주문_항목));

        // when
        List<Order> 주문_목록 = orderService.list();

        // then
        assertThat(주문_목록).containsExactly(생성된_주문);
    }

    @DisplayName("주문 생성 테스트")
    @Nested
    class TestCreateOrder {
        @DisplayName("주문 생성 확인")
        @Test
        void 주문_생성_확인() {
            // given
            OrderLineItem 주문_항목 = new OrderLineItem();
            주문_항목.setMenuId(더블강정.getId());
            주문_항목.setQuantity(1);

            Order 등록_요청_데이터 = new Order();
            등록_요청_데이터.setOrderTableId(테이블.getId());
            등록_요청_데이터.setOrderLineItems(Collections.singletonList(주문_항목));

            given(menuDao.countByIdIn(any())).willReturn(1L);
            given(orderTableDao.findById(any())).willReturn(Optional.of(테이블));
            given(orderDao.save(any())).willReturn(생성된_주문);
            given(orderLineItemDao.save(any())).willReturn(생성된_주문_항목);

            // when
            Order 등록된_주문 = orderService.create(등록_요청_데이터);

            // then
            assertThat(등록된_주문).isEqualTo(생성된_주문);
        }

        @DisplayName("주문 항목이 존재하지 않음")
        @Test
        void 주문_항목이_존재하지_않음() {
            // given
            Order 등록_요청_데이터 = new Order();
            등록_요청_데이터.setOrderTableId(테이블.getId());
            등록_요청_데이터.setOrderLineItems(Collections.emptyList());

            // when
            ThrowableAssert.ThrowingCallable 등록_요청 = () -> orderService.create(등록_요청_데이터);

            // then
            assertThatThrownBy(등록_요청).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 항목이 중복됨")
        @Test
        void 주문_항목이_중복됨() {
            // given
            OrderLineItem 주문_상품 = new OrderLineItem();
            주문_상품.setMenuId(더블강정.getId());
            주문_상품.setQuantity(1);

            Order 등록_요청_데이터 = new Order();
            등록_요청_데이터.setOrderTableId(테이블.getId());
            등록_요청_데이터.setOrderLineItems(Arrays.asList(주문_상품, 주문_상품));

            given(menuDao.countByIdIn(any())).willReturn(1L);

            // when
            ThrowableAssert.ThrowingCallable 등록_요청 = () -> orderService.create(등록_요청_데이터);

            // then
            assertThatThrownBy(등록_요청).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블이 존재하지 않음")
        @Test
        void 주문_테이블이_존재하지_않음() {
            // given
            OrderLineItem 주문_상품 = new OrderLineItem();
            주문_상품.setMenuId(더블강정.getId());
            주문_상품.setQuantity(1);

            Order 등록_요청_데이터 = new Order();
            등록_요청_데이터.setOrderTableId(테이블.getId());
            등록_요청_데이터.setOrderLineItems(Collections.singletonList(주문_상품));

            given(menuDao.countByIdIn(any())).willReturn(1L);
            given(orderTableDao.findById(any())).willReturn(Optional.empty());

            // when
            ThrowableAssert.ThrowingCallable 등록_요청 = () -> orderService.create(등록_요청_데이터);

            // then
            assertThatThrownBy(등록_요청).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("빈 테이블에 주문 요청")
        @Test
        void 빈_테이블에_주문_요청() {
            // given
            OrderLineItem 주문_상품 = new OrderLineItem();
            주문_상품.setMenuId(더블강정.getId());
            주문_상품.setQuantity(1);

            Order 등록_요청_데이터 = new Order();
            등록_요청_데이터.setOrderTableId(빈_테이블.getId());
            등록_요청_데이터.setOrderLineItems(Collections.singletonList(주문_상품));

            given(menuDao.countByIdIn(any())).willReturn(1L);
            given(orderTableDao.findById(any())).willReturn(Optional.of(빈_테이블));

            // when
            ThrowableAssert.ThrowingCallable 등록_요청 = () -> orderService.create(등록_요청_데이터);

            // then
            assertThatThrownBy(등록_요청).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("주문 상태 변경 테스트")
    @Nested
    class TestCreateOrderStatus {
        @DisplayName("주문 상태 변경 확인")
        @Test
        void 주문_상태_변경_확인() {
            // given
            Order 변경_요청_데이터 = new Order();
            변경_요청_데이터.setOrderStatus("MEAL");

            given(orderDao.findById(any())).willReturn(Optional.of(생성된_주문));
            given(orderDao.save(any())).willReturn(식사중인_주문);
            given(orderLineItemDao.findAllByOrderId(any())).willReturn(Collections.singletonList(생성된_주문_항목));

            // when
            Order 변경된_주문 = orderService.changeOrderStatus(1L, 변경_요청_데이터);

            // then
            assertThat(변경된_주문.getOrderStatus()).isEqualTo(변경_요청_데이터.getOrderStatus());
        }

        @DisplayName("존재하지 않는 주문에 변경 요청")
        @Test
        void 존재하지_않는_주문에_변경_요청() {
            // given
            Order 변경_요청_데이터 = new Order();
            변경_요청_데이터.setOrderStatus("MEAL");

            given(orderDao.findById(any())).willReturn(Optional.empty());

            // when
            ThrowableAssert.ThrowingCallable 변경_요청 = () -> orderService.changeOrderStatus(1L, 변경_요청_데이터);

            // then
            assertThatThrownBy(변경_요청).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("계산 완료 상태인 주문에 변경 요청")
        @Test
        void 계산_완료_상태인_주문에_변경_요청() {
            // given
            Order 변경_요청_데이터 = new Order();
            변경_요청_데이터.setOrderStatus("MEAL");

            given(orderDao.findById(any())).willReturn(Optional.of(계산된_주문));

            // when
            ThrowableAssert.ThrowingCallable 변경_요청 = () -> orderService.changeOrderStatus(1L, 변경_요청_데이터);

            // then
            assertThatThrownBy(변경_요청).isInstanceOf(IllegalArgumentException.class);
        }
    }
}
