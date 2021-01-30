package kitchenpos.order.application.order;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menu.domain.menu.Menu;
import kitchenpos.menu.domain.menu.MenuGroup;
import kitchenpos.menu.domain.menu.MenuProduct;
import kitchenpos.menu.domain.menu.MenuRepository;
import kitchenpos.menu.domain.product.Product;
import kitchenpos.order.domain.order.Order;
import kitchenpos.order.domain.order.OrderLineItem;
import kitchenpos.order.domain.order.OrderRepository;
import kitchenpos.order.domain.order.OrderStatus;
import kitchenpos.order.domain.ordertable.OrderTable;
import kitchenpos.order.domain.ordertable.OrderTableRepository;
import kitchenpos.order.dto.order.OrderLineItemRequest;
import kitchenpos.order.dto.order.OrderRequest;
import kitchenpos.order.dto.order.OrderResponse;

@DisplayName("애플리케이션 테스트 보호 - 주문 서비스")
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    private Product 후라이드치킨;
    private Product 양념치킨;
    private MenuGroup 치킨세트;
    private Menu 후라이드한마리_양념치킨한마리;
    private MenuProduct 후라이드치킨한마리;
    private MenuProduct 양념치킨한마리;

    private Order 주문;
    private OrderLineItem 주문_항목;
    private OrderTable 주문테이블_비어있지_않음;
    private OrderTable 주문테이블_비어있음;
    private List<OrderLineItem> 주문_항목_목록;
    private OrderRequest 주문_요청;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    public void setup() {
        후라이드치킨 = new Product(1L, "후라이드치킨", new BigDecimal(16000));
        양념치킨 = new Product(2L, "양념치킨", new BigDecimal(16000));

        치킨세트 = new MenuGroup("후라이드앙념치킨");

        후라이드한마리_양념치킨한마리 = new Menu("후라이드+양념", BigDecimal.valueOf(32000), 치킨세트);

        후라이드치킨한마리 = new MenuProduct(후라이드한마리_양념치킨한마리, 후라이드치킨, 1L);
        양념치킨한마리 = new MenuProduct(후라이드한마리_양념치킨한마리, 양념치킨, 1L);

        후라이드한마리_양념치킨한마리.addMenuProducts(Arrays.asList(후라이드치킨한마리, 양념치킨한마리));

        주문테이블_비어있음 = new OrderTable(0, true);
        주문테이블_비어있지_않음 = new OrderTable(2, false);
        주문_항목 = new OrderLineItem(후라이드한마리_양념치킨한마리, 1L);

        주문_항목_목록 = new ArrayList<>();
        주문_항목_목록.add(주문_항목);

        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(후라이드한마리_양념치킨한마리.getId(),1L);
        주문_요청 = new OrderRequest(1L, Collections.singletonList(orderLineItemRequest));
        주문 = Order.createToCook(주문테이블_비어있지_않음, 주문_항목_목록);

    }

    @DisplayName("주문을 생성한다")
    @Test
    void create() {
        final Long orderTableId = 1L;
        given(orderTableRepository.findById(orderTableId))
            .willReturn(Optional.of(주문테이블_비어있지_않음));
        given(menuRepository.findAllByIdIn(Collections.singletonList(주문_항목.getMenu().getId())))
            .willReturn(Collections.singletonList(후라이드한마리_양념치킨한마리));
        given(orderRepository.save(주문)).willReturn(주문);
        given(orderRepository.save(주문)).willReturn(주문);

        OrderResponse orderResponse = orderService.create(주문_요청);

        assertThat(orderResponse).isNotNull();
        assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(orderResponse.getOrderLineItemResponses()).hasSize(1);
    }

    @DisplayName("주문 생성 예외: 주문 항목 목록이 비어있음")
    @Test
    void createThrowExceptionWhenOrderLineItemsEmpty() {
        final Long orderTableId = 1L;
        주문_요청.setOrderLineItems(null);
        given(orderTableRepository.findById(orderTableId))
            .willReturn(Optional.of(주문테이블_비어있지_않음));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(주문_요청));
    }

    @DisplayName("주문 생성 예외: 주문 항목의 메뉴 갯수와 저장된 메뉴 갯수가 다름")
    @Test
    void createThrowExceptionWhenMenuCountDifferenceWithSavedMenuCount() {
        final Long orderTableId = 1L;
        given(orderTableRepository.findById(orderTableId))
            .willReturn(Optional.of(주문테이블_비어있지_않음));
        given(menuRepository.findAllByIdIn(Collections.singletonList(주문_항목.getMenu().getId())))
            .willReturn(new ArrayList<>());

        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(주문_요청));
    }

    @DisplayName("주문 생성 예외: 주문테이블이 빈 테이블임")
    @Test
    void createThrowExceptionWhenOrderTableIsEmpty() {
        final Long orderTableId = 1L;
        given(orderTableRepository.findById(orderTableId))
            .willReturn(Optional.of(주문테이블_비어있음));
        given(menuRepository.findAllByIdIn(Collections.singletonList(주문_항목.getMenu().getId())))
            .willReturn(Collections.singletonList(후라이드한마리_양념치킨한마리));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(주문_요청));
    }

    @DisplayName("주문 목록 조회")
    @Test
    void list() {
        given(orderRepository.findAll()).willReturn(Collections.singletonList(주문));

        List<OrderResponse> orderResponses = orderService.findAll();

        assertThat(orderResponses).isNotEmpty();
    }

}
