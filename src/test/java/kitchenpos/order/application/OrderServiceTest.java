package kitchenpos.order.application;

import static kitchenpos.menu.domain.MenuProductTest.메뉴상품_생성;
import static kitchenpos.menu.domain.MenuTest.메뉴_생성;
import static kitchenpos.menugroup.domain.MenuGroupTest.메뉴그룹_생성;
import static kitchenpos.order.domain.OrderLineItemTest.주문_항목_생성;
import static kitchenpos.order.domain.OrderMenuTest.주문메뉴_생성;
import static kitchenpos.order.domain.OrderTest.주문_생성;
import static kitchenpos.order.dto.OrderLineItemRequestTest.주문_항목_요청_객체_생성;
import static kitchenpos.order.dto.OrderRequestTest.주문_요청_객체_생성;
import static kitchenpos.product.domain.ProductTest.상품_생성;
import static kitchenpos.table.domain.OrderTableTest.주문_테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderService orderService;

    private Product 미역국;
    private Product 소머리국밥;
    private MenuGroup 식사;
    private MenuProduct 미역국_메뉴상품;
    private MenuProduct 소머리국밥_메뉴상품;
    private Menu 미역국_메뉴;
    private Menu 소머리국밥_메뉴;
    private OrderLineItem 주문_항목;
    private OrderTable 주문_테이블;
    private Order 주문;

    @BeforeEach
    public void setUp() {
        미역국 = 상품_생성(1L, "미역국", BigDecimal.valueOf(6000));
        소머리국밥 = 상품_생성(2L, "소머리국밥", BigDecimal.valueOf(8000));
        식사 = 메뉴그룹_생성(1L, "식사");

        미역국_메뉴상품 = 메뉴상품_생성(null, 미역국, 1L);
        미역국_메뉴 = 메뉴_생성(1L, "미역국", BigDecimal.valueOf(6000), 식사, Arrays.asList(미역국_메뉴상품));

        소머리국밥_메뉴상품 = 메뉴상품_생성(null, 소머리국밥, 1L);
        소머리국밥_메뉴 = 메뉴_생성(2L, "소머리국밥", BigDecimal.valueOf(8000), 식사, Arrays.asList(소머리국밥_메뉴상품));

        주문_항목 = 주문_항목_생성(1L, 주문메뉴_생성(소머리국밥_메뉴.getId(), 소머리국밥_메뉴.getNameValue(), 소머리국밥_메뉴.getPriceValue()), 2);
        주문_테이블 = 주문_테이블_생성(1L, null, 2, false);

        주문 = 주문_생성(1L, 주문_테이블.getId(), Arrays.asList(주문_항목));
    }

    @Test
    @DisplayName("주문 등록")
    void create() {
        // given
        when(menuRepository.findById(any())).thenReturn(Optional.of(미역국_메뉴));
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(주문_테이블));
        when(orderRepository.save(any(Order.class))).thenReturn(주문);

        OrderLineItemRequest 주문_항목_요청_객체 = 주문_항목_요청_객체_생성(미역국_메뉴.getId(), 2L);
        OrderRequest 주문_요청_객체 = 주문_요청_객체_생성(주문_테이블.getId(), null, Arrays.asList(주문_항목_요청_객체));

        // when
        OrderResponse 주문_생성_결과 = orderService.create(주문_요청_객체);

        // then
        assertThat(주문_생성_결과.getId()).isEqualTo(주문.getId());
    }

    @Test
    @DisplayName("주문 목록 조회")
    void list() {
        // given
        when(orderRepository.findAll()).thenReturn(Arrays.asList(주문));

        // when
        List<OrderResponse> 주문_목록_조회_결과 = orderService.list();

        // then
        assertThat(주문_목록_조회_결과).hasSize(1);
    }

    @Test
    @DisplayName("주문 상태 변경")
    void changeOrderStatus() {
        // given
        when(orderRepository.findById(any())).thenReturn(Optional.of(주문));
        OrderRequest 주문_요청_객체 = 주문_요청_객체_생성(null, OrderStatus.MEAL, null);

        // when
        OrderResponse 주문_상태_변경_결과 = orderService.changeOrderStatus(주문.getId(), 주문_요청_객체);

        // then
        assertThat(주문_상태_변경_결과.getOrderStatus()).isEqualTo(주문_요청_객체.getOrderStatus());
    }
}