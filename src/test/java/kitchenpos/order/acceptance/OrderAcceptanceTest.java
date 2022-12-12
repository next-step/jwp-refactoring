package kitchenpos.order.acceptance;

import static java.util.Collections.singletonList;
import static kitchenpos.menu.acceptance.MenuGroupRestAssured.메뉴_그룹_등록되어_있음;
import static kitchenpos.menu.acceptance.MenuRestAssured.메뉴_등록되어_있음;
import static kitchenpos.menu.domain.MenuGroupTestFixture.generateMenuGroupRequest;
import static kitchenpos.menu.domain.MenuProductTestFixture.generateMenuProductRequest;
import static kitchenpos.menu.domain.MenuTestFixture.generateMenuRequest;
import static kitchenpos.order.acceptance.OrderRestAssured.주문_등록되어_있음;
import static kitchenpos.order.acceptance.OrderRestAssured.주문_목록_조회_요청;
import static kitchenpos.order.acceptance.OrderRestAssured.주문_상태_변경_요청;
import static kitchenpos.order.acceptance.OrderRestAssured.주문_생성_요청;
import static kitchenpos.order.acceptance.TableRestAssured.주문_테이블_등록되어_있음;
import static kitchenpos.order.domain.OrderLineItemTestFixture.generateOrderLineItemRequest;
import static kitchenpos.order.domain.OrderTableTestFixture.generateOrderTableRequest;
import static kitchenpos.order.domain.OrderTestFixture.generateOrderRequest;
import static kitchenpos.product.acceptance.ProductRestAssured.상품_등록되어_있음;
import static kitchenpos.product.domain.ProductTestFixture.generateProductRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.acceptance.AcceptanceTest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("주문 관련 인수 테스트")
public class OrderAcceptanceTest extends AcceptanceTest {

    private ProductResponse 감자튀김;
    private ProductResponse 불고기버거;
    private ProductResponse 콜라;
    private MenuGroupResponse 햄버거세트;
    private List<MenuProductRequest> 불고기버거상품요청 = new ArrayList<>();
    private MenuResponse 불고기버거세트;
    private OrderTableResponse 주문테이블A;
    private OrderTableResponse 주문테이블B;
    private OrderLineItemRequest 불고기버거세트2개주문요청;
    private OrderLineItemRequest 불고기버거세트1개주문요청;
    private OrderRequest 주문A요청;
    private OrderRequest 주문B요청;

    @BeforeEach
    public void setUp() {
        super.setUp();
        햄버거세트 = 메뉴_그룹_등록되어_있음(generateMenuGroupRequest("햄버거세트")).as(MenuGroupResponse.class);
        감자튀김 = 상품_등록되어_있음(generateProductRequest("감자튀김", BigDecimal.valueOf(3000L))).as(ProductResponse.class);
        콜라 = 상품_등록되어_있음(generateProductRequest("콜라", BigDecimal.valueOf(1500L))).as(ProductResponse.class);
        불고기버거 = 상품_등록되어_있음(generateProductRequest("불고기버거", BigDecimal.valueOf(4000L))).as(ProductResponse.class);
        불고기버거상품요청.add(generateMenuProductRequest(감자튀김.getId(), 1L));
        불고기버거상품요청.add(generateMenuProductRequest(콜라.getId(), 1L));
        불고기버거상품요청.add(generateMenuProductRequest(불고기버거.getId(), 1L));
        불고기버거세트 = 메뉴_등록되어_있음(generateMenuRequest("불고기버거세트", BigDecimal.valueOf(8500L), 햄버거세트.getId(), 불고기버거상품요청)).as(MenuResponse.class);
        주문테이블A = 주문_테이블_등록되어_있음(generateOrderTableRequest(5, false)).as(OrderTableResponse.class);
        주문테이블B = 주문_테이블_등록되어_있음(generateOrderTableRequest(4, false)).as(OrderTableResponse.class);
        불고기버거세트2개주문요청 = generateOrderLineItemRequest(불고기버거세트.getId(), 2);
        불고기버거세트1개주문요청 = generateOrderLineItemRequest(불고기버거세트.getId(), 1);
        주문A요청 = generateOrderRequest(주문테이블A.getId(), OrderStatus.COOKING, singletonList(불고기버거세트2개주문요청));
        주문B요청 = generateOrderRequest(주문테이블B.getId(), OrderStatus.COOKING, singletonList(불고기버거세트1개주문요청));
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void createOrder() {
        // when
        ExtractableResponse<Response> response = 주문_생성_요청(주문A요청);

        // then
        주문_생성됨(response);
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void findAllOrders() {
        // given
        ExtractableResponse<Response> 주문A_응답 = 주문_등록되어_있음(주문A요청);
        ExtractableResponse<Response> 주문B_응답 = 주문_등록되어_있음(주문B요청);

        // when
        ExtractableResponse<Response> response = 주문_목록_조회_요청();

        // then
        주문_목록_응답됨(response);
        주문_목록_포함됨(response, Arrays.asList(주문A_응답, 주문B_응답));
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        // given
        OrderStatus expectOrderStatus = OrderStatus.MEAL;
        OrderResponse orderResponse = 주문_등록되어_있음(주문A요청).as(OrderResponse.class);
        List<OrderLineItemRequest> orderLineItemRequests = orderResponse.getOrderLineItems()
                .stream()
                .map(orderLineItemResponse -> new OrderLineItemRequest(orderLineItemResponse.getMenuId(), orderLineItemResponse.getQuantity()))
                .collect(Collectors.toList());
        OrderRequest changeOrderRequest = generateOrderRequest(orderResponse.getOrderTableId(), expectOrderStatus, orderLineItemRequests);

        // when
        ExtractableResponse<Response> response = 주문_상태_변경_요청(orderResponse.getId(), changeOrderRequest);

        // then
        주문_상태_변경됨(response, expectOrderStatus);
    }

    private static void 주문_생성됨(ExtractableResponse<Response> response) {
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isNotBlank()
        );
    }

    private static void 주문_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static void 주문_목록_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        List<Long> expectedOrderIds = createdResponses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[3]))
                .collect(Collectors.toList());

        List<Long> resultOrderIds = response.jsonPath().getList(".", OrderResponse.class).stream()
                .map(OrderResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultOrderIds).containsAll(expectedOrderIds);
    }

    private static void 주문_상태_변경됨(ExtractableResponse<Response> response, OrderStatus expectOrderStatus) {
        String actualOrderStatus = response.jsonPath().getString("orderStatus");

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(actualOrderStatus).isEqualTo(expectOrderStatus.name())
        );
    }
}
