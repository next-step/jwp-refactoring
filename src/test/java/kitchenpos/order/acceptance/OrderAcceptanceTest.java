package kitchenpos.order.acceptance;

import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.BaseAcceptanceTest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.rest.MenuRestAssured;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.menugroup.fixture.MenuGroupFixture;
import kitchenpos.menugroup.rest.MenuGroupRestAssured;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.rest.OrderRestAssured;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.fixture.ProductFixture;
import kitchenpos.product.rest.ProductRestAssured;
import kitchenpos.table.dto.OrderTableCreateRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.rest.TableRestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class OrderAcceptanceTest extends BaseAcceptanceTest {

    private OrderCreateRequest orderCreateRequest;

    @BeforeEach
    public void setUp() {
        super.setUp();

        ProductResponse product = ProductRestAssured.상품_등록됨(ProductFixture.후라이드);
        MenuGroupResponse menuGroup = MenuGroupRestAssured.메뉴_그룹_등록됨(MenuGroupFixture.한마리메뉴);
        List<MenuProductRequest> menuProductRequests = Arrays.asList(new MenuProductRequest(product.getId(), 2L));
        MenuResponse menuResponse = MenuRestAssured.메뉴_등록됨("후라이드치킨", BigDecimal.valueOf(16_000L), menuGroup.getId(), menuProductRequests);

        List<OrderLineItemRequest> orderLineItemRequests = Arrays.asList(
                new OrderLineItemRequest(menuResponse.getId(), 1L)
        );
        OrderTableResponse orderTable = TableRestAssured.주문_테이블_등록됨(new OrderTableCreateRequest(1, false));
        this.orderCreateRequest = new OrderCreateRequest(orderTable.getId(), orderLineItemRequests);
    }

    @Test
    @DisplayName("신규 주문 정보가 주어진 경우 주문 등록 요청시 요청에 성공한다")
    void createOrderThenReturnOrderInfoResponseTest() {
        // when
        ExtractableResponse<Response> response = OrderRestAssured.주문_등록_요청(orderCreateRequest);

        // then
        OrderResponse orderResponse = response.as(OrderResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(orderResponse.getId()).isNotNull(),
                () -> assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.COOKING)
        );
    }

    @Test
    @DisplayName("주문 조회 목록 요청시 요청에 성공한다")
    void findAllOrdersThenReturnOrderInfoResponsesTest() {
        // given
        OrderResponse order = OrderRestAssured.주문_등록됨(orderCreateRequest);

        // when
        ExtractableResponse<Response> response = OrderRestAssured.주문_목록_조회_요청();

        // then
        List<OrderResponse> orders = response.as(new TypeRef<List<OrderResponse>>() {});
        List<Long> orderIds = orders.stream().map(OrderResponse::getId).collect(Collectors.toList());
        List<Long> expectedOrderIds = Stream.of(order).map(OrderResponse::getId).collect(Collectors.toList());
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(orderIds).containsAll(expectedOrderIds)
        );
    }

    @Test
    @DisplayName("주문 상태 변경 요청시 요청에 성공한다")
    void changeOrderStateTest() {
        // given
        OrderResponse order = OrderRestAssured.주문_등록됨(orderCreateRequest);

        // when
        ExtractableResponse<Response> response = OrderRestAssured.주문_상태_변경_요청(order.getId(), OrderStatus.COMPLETION);

        // then
        OrderResponse orderResponse = response.as(OrderResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION)
        );
    }
}
