package kitchenpos.order.service;


import kitchenpos.application.OrderService;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.*;
import kitchenpos.menu.MenuFactory;
import kitchenpos.order.OrderFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

@DisplayName("주문 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @InjectMocks
    private OrderService orderService;

    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    private MenuGroup 튀김종류;
    private MenuGroup 중식종류;
    private Menu 치킨세트;
    private Menu 중식세트;
    private List<MenuProduct> 치킨세트_상품_리스트;
    private List<MenuProduct> 중식세트_상품_리스트;
    private Product 치킨;
    private Product 맥주;
    private Product 탕수육;
    private Product 고량주;
    private MenuProduct 치킨_상품메뉴;
    private MenuProduct 맥주_상품메뉴;
    private MenuProduct 탕수육_상품메뉴;
    private MenuProduct 고량주_상품메뉴;

    private OrderLineItem 첫번째_주문_치킨세트;
    private OrderLineItem 첫번째_주문_중식세트;

    private TableGroup 테이블_그룹_일번;
    private OrderTable 주문_테이블_일번;

    private Order 첫번째_주문;

    @BeforeEach
    void setUp() {
        메뉴_생성됨();
        주문_요청();
    }


    @DisplayName("주문을 생성한다.")
    @Test
    void 주문_생성() {
        given(menuDao.countByIdIn(Arrays.asList(치킨세트.getId(), 중식세트.getId()))).willReturn(2L);
        given(orderTableDao.findById(주문_테이블_일번.getId())).willReturn(Optional.ofNullable(주문_테이블_일번));
        given(orderDao.save(첫번째_주문)).willReturn(첫번째_주문);
        given(orderLineItemDao.save(첫번째_주문_치킨세트)).willReturn(첫번째_주문_치킨세트);
        given(orderLineItemDao.save(첫번째_주문_중식세트)).willReturn(첫번째_주문_중식세트);

        Order response = orderService.create(첫번째_주문);

        assertAll(
                () -> assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(response.getOrderLineItems()).containsExactly(첫번째_주문_치킨세트, 첫번째_주문_중식세트)
        );
    }

    @DisplayName("주문에 최소 1개 이상의 주문 라인 아이템이 존재해야 한다.")
    @Test
    void 주문에_주문_라인_아이템_미존재_예외() {
        // given
        첫번째_주문.setOrderLineItems(new ArrayList<>());

        // when
        Throwable thrown = catchThrowable(() -> orderService.create(첫번째_주문));

        // then
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 라인 아이템 갯수만큼 메뉴 테이블에 메뉴가 존재해야 한다.")
    @Test
    void 주문_라인_아이템_메뉴_갯수_불일치_예외() {
        // given
        given(menuDao.countByIdIn(Arrays.asList(첫번째_주문_치킨세트.getMenuId(), 첫번째_주문_중식세트.getMenuId()))).willReturn(1L);

        // when
        Throwable thrown = catchThrowable(() -> orderService.create(첫번째_주문));

        // then
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문에 해당하는 주문 테이블이 없을 경우 예외가 발생한다.")
    @Test
    void 주문에_해당하는_주문_테이블_미존재_예외() {
        // given
        given(menuDao.countByIdIn(Arrays.asList(치킨세트.getId(), 중식세트.getId()))).willReturn(2L);

        // when
        Throwable thrown = catchThrowable(() -> orderService.create(첫번째_주문));

        // then
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void 주문_목록_조회() {
        // given
        given(orderDao.findAll()).willReturn(Collections.singletonList(첫번째_주문));
        given(orderLineItemDao.findAllByOrderId(첫번째_주문.getId())).willReturn(Arrays.asList(첫번째_주문_치킨세트, 첫번째_주문_중식세트));

        // when
        List<Order> response = orderService.list();

        // then
        assertThat(response.size()).isEqualTo(1);
    }

    private void 메뉴_생성됨() {
        튀김종류 = MenuFactory.ofMenuGroup(1L, "튀김종류");
        중식종류 = MenuFactory.ofMenuGroup(2L, "중식종류");

        치킨세트 = MenuFactory.ofMenu(1L, "치킨세트_모형", 튀김종류.getId(), 24000);
        중식세트 = MenuFactory.ofMenu(2L, "중식세트_모형", 중식종류.getId(), 40000);

        치킨 = MenuFactory.ofProduct(1L, "치킨", 19000);
        맥주 = MenuFactory.ofProduct(2L, "맥주", 5000);

        탕수육 = MenuFactory.ofProduct(3L, "탕수육", 20000);
        고량주 = MenuFactory.ofProduct(4L, "고량주", 20000);

        치킨_상품메뉴 = MenuFactory.ofMenuProduct(1L, 치킨세트.getId(), 치킨.getId(), 1);
        맥주_상품메뉴 = MenuFactory.ofMenuProduct(2L, 치킨세트.getId(), 맥주.getId(), 1);

        탕수육_상품메뉴 = MenuFactory.ofMenuProduct(1L, 중식세트.getId(), 탕수육.getId(), 1);
        고량주_상품메뉴 = MenuFactory.ofMenuProduct(2L, 중식세트.getId(), 고량주.getId(), 1);

        치킨세트_상품_리스트 = MenuFactory.ofMenuProductList(Arrays.asList(치킨_상품메뉴, 맥주_상품메뉴));
        중식세트_상품_리스트 = MenuFactory.ofMenuProductList(Arrays.asList(탕수육_상품메뉴, 고량주_상품메뉴));

        치킨세트.setMenuProducts(치킨세트_상품_리스트);
        중식세트.setMenuProducts(중식세트_상품_리스트);
    }

    public void 주문_요청() {
        테이블_그룹_일번 = OrderFactory.ofTableGroup(1L, null, LocalDateTime.now());
        주문_테이블_일번 = OrderFactory.ofOrderTable(1L, 테이블_그룹_일번.getId(), false, 4);
        테이블_그룹_일번.setOrderTables(Collections.singletonList(주문_테이블_일번));

        첫번째_주문 = OrderFactory.ofOrder(1L, 주문_테이블_일번.getId(), null, Arrays.asList(첫번째_주문_치킨세트, 첫번째_주문_중식세트), null);

        첫번째_주문_치킨세트 = OrderFactory.ofOrderLineItem(1L, 첫번째_주문.getId(), 치킨세트.getId(), 1L);
        첫번째_주문_중식세트 = OrderFactory.ofOrderLineItem(2L, 첫번째_주문.getId(), 중식세트.getId(), 2L);

        첫번째_주문.setOrderLineItems(Arrays.asList(첫번째_주문_치킨세트, 첫번째_주문_중식세트));
    }
}
