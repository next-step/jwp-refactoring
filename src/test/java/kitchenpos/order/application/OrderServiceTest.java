package kitchenpos.order.application;

import static kitchenpos.menu.acceptance.MenuAcceptanceTest.메뉴_생성_요청;
import static kitchenpos.menugroup.acceptance.MenuGroupAcceptanceTest.메뉴그룹_생성_요청;
import static kitchenpos.product.acceptance.ProductAcceptanceTest.상품_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.common.AcceptanceTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.UpdateOrderStatusRequest;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.application.TableService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("주문 관련 비즈니스 테스트")
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest extends AcceptanceTest {
    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderService orderService;

    @InjectMocks
    private TableService tableService;

    private Product 치킨;
    private Product 스파게티;
    private MenuProduct 치킨_두마리;
    private MenuProduct 스파게티_이인분;
    private MenuProductRequest 치킨_두마리_요청;
    private MenuProductRequest 스파게티_이인분_요청;
    private Menu 치킨_스파게티_더블세트_메뉴;
    private MenuResponse 치킨_스파게티_더블세트_메뉴_응답;
    private MenuGroup 양식;
    private OrderTable 주문_테이블;
    private OrderLineItem 주문_항목;
    private Order 주문;
    private List<Long> menuIds;
    OrderLineItemRequest 주문_항목_요청;

    @BeforeEach
    public void setUp() {
        super.setUp();
        치킨 = 상품_생성_요청(new Product(1L, "치킨", BigDecimal.valueOf(20_000))).as(Product.class);
        스파게티 = 상품_생성_요청(new Product(1L, "스파게티", BigDecimal.valueOf(10_000))).as(Product.class);
        양식 = 메뉴그룹_생성_요청(new MenuGroup(1L, "양식")).as(MenuGroup.class);

        치킨_두마리 = new MenuProduct(1L, 2L, 치킨_스파게티_더블세트_메뉴, 치킨);
        스파게티_이인분 = new MenuProduct(2L, 2L, 치킨_스파게티_더블세트_메뉴, 스파게티);
        치킨_두마리_요청 = MenuProductRequest.of(치킨.getId(), 1L);
        스파게티_이인분_요청 = MenuProductRequest.of(스파게티.getId(), 1L);
        치킨_스파게티_더블세트_메뉴 = new Menu(1L, "치킨 스파게티 더블세트 메뉴", new BigDecimal(13_000), 양식);
        치킨_스파게티_더블세트_메뉴_응답 = 메뉴_생성_요청(MenuRequest.of(
                치킨_스파게티_더블세트_메뉴.getName(),
                치킨_스파게티_더블세트_메뉴.getPrice(),
                치킨_스파게티_더블세트_메뉴.getMenuGroup().getId(),
                Arrays.asList(치킨_두마리_요청, 스파게티_이인분_요청)
        )).as(MenuResponse.class);

        주문_테이블 = new OrderTable(1L, 0, false);
        주문 = new Order(주문_테이블, OrderStatus.COOKING, LocalDateTime.now(), new ArrayList<>());
        주문_항목 = new OrderLineItem(1L, 1L, 치킨_스파게티_더블세트_메뉴);
        주문.setOrderLineItems(Collections.singletonList(주문_항목));
        주문_항목_요청 = new OrderLineItemRequest(치킨_스파게티_더블세트_메뉴.getId(), 1L);

        menuIds = 주문.getOrderLineItems()
                .stream()
                .map(OrderLineItem::getMenu)
                .map(Menu::getId)
                .collect(Collectors.toList());
    }

    @Test
    void 주문을_등록할_수_있다() {
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 10L);
        OrderRequest orderRequest = OrderRequest.of(1L, Collections.singletonList(orderLineItemRequest));
        given(orderRepository.save(any(Order.class))).willReturn(주문);

        OrderResponse reslut = orderService.create(orderRequest);
        assertThat(reslut.getOrderLineItems()).isEqualTo(OrderStatus.COOKING);


    }

    private ExtractableResponse<Response> 주문_생성_요청(OrderRequest request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/orders")
                .then().log().all()
                .extract();
    }

    private void 주문_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void 주문_항목이_비어있는_경우_주문을_등록할_수_없다() {
        OrderRequest request = OrderRequest.of(주문_테이블.getId(), new ArrayList<>());

        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 등록되지_않은_주문_항목이_존재_할_경우_주문을_등록할_수_없다() {
        given(menuRepository.countByIdIn(menuIds)).willReturn(menuIds.size());
        OrderRequest request = OrderRequest.of(주문_테이블.getId(), Arrays.asList(주문_항목_요청));

        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_등록되어_있지_않은_경우_주문을_등록할_수_없다() {
        given(menuRepository.countByIdIn(menuIds)).willReturn(menuIds.size());
        given(orderTableRepository.findById(주문.getOrderTable().getId())).willReturn(Optional.empty());
        OrderRequest request = OrderRequest.of(주문_테이블.getId(), Arrays.asList(주문_항목_요청));

        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_비어있는_경우_주문을_등록할_수_없다() {
        주문_테이블.setEmpty(true);
        OrderRequest request = OrderRequest.of(주문_테이블.getId(), Arrays.asList());

        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_목록을_조회할_수_있다() {
        given(orderRepository.findAll()).willReturn(Collections.singletonList(주문));

        List<OrderResponse> orders = orderService.list();

        assertAll(
                () -> assertThat(orders).hasSize(1),
                () -> assertThat(orders.stream().map(OrderResponse::getId)).contains(주문.getId())
        );
    }

    @Test
    void 주문_상태를_변경할_수_있다() {
        OrderStatus expectedOrderStatus = OrderStatus.MEAL;
        UpdateOrderStatusRequest request = UpdateOrderStatusRequest.of(expectedOrderStatus.name());
        given(orderRepository.findById(주문.getId())).willReturn(Optional.of(주문));
        given(orderRepository.save(주문)).willReturn(주문);

        OrderResponse result = orderService.changeOrderStatus(주문.getId(), request);

        assertAll(
                () -> assertThat(result.getId()).isEqualTo(주문.getId()),
                () -> assertThat(result.getOrderStatus()).isEqualTo(expectedOrderStatus.name())
        );
    }

    @Test
    void 등록되지_않은_주문_상태를_변경할_수_없다() {
        UpdateOrderStatusRequest request = UpdateOrderStatusRequest.of(OrderStatus.COOKING.name());

        assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 계산_완료된_주문은_상태를_변경할_수_없다() {
        주문.setOrderStatus(OrderStatus.COMPLETION);
        UpdateOrderStatusRequest request = UpdateOrderStatusRequest.of(주문.getOrderStatus().name());

        assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
