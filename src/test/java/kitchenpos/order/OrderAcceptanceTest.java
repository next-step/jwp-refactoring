package kitchenpos.order;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.MenuAcceptanceTest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menuGroup.MenuGroupAcceptanceTest;
import kitchenpos.menuGroup.dto.MenuGroupResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.product.ProductAcceptanceTest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.table.TableAcceptanceTest;
import kitchenpos.table.domain.OrderTable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 관련 기능 테스트")
public class OrderAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("주문 관리 기능")
    void order() {
        ProductResponse product = ProductAcceptanceTest.상품_등록_요청("짜장면", new BigDecimal(5000)).as(ProductResponse.class);
        MenuGroupResponse menuGroup = MenuGroupAcceptanceTest.메뉴_그룹_등록_요청("중국음식").as(MenuGroupResponse.class);
        MenuProductRequest menuProductRequest = new MenuProductRequest(product.getId(), 1L);
        MenuResponse menuResponse = MenuAcceptanceTest.메뉴_등록_요청("짜장면", new BigDecimal(5000), menuGroup.getId(), Arrays.asList(menuProductRequest)).as(MenuResponse.class);

        // when
        ExtractableResponse<Response> orderTableResponse = TableAcceptanceTest.주문_테이블_등록_요청(false, 4);
        OrderTable createOrderTable = orderTableResponse.as(OrderTable.class);
        // then
        assertThat(orderTableResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // given
        OrderLineItemRequest orderLineItem = new OrderLineItemRequest(menuResponse.getId(), 1L);

        // when
        ExtractableResponse<Response> response = 주문_요청(createOrderTable.getId(), Arrays.asList(orderLineItem));

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when
        ExtractableResponse<Response> getResponse = 주문_조회();

        // then
        assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(getResponse.jsonPath().getList(".", OrderResponse.class)).hasSize(1);

        // when
        ExtractableResponse<Response> changeStatusResponse = 주문_상태_변경(response, OrderStatus.MEAL.name());

        // then
        assertThat(changeStatusResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(changeStatusResponse.as(OrderResponse.class).getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    private ExtractableResponse<Response> 주문_상태_변경(ExtractableResponse<Response> response, String orderStatus) {
        OrderRequest order = new OrderRequest(orderStatus);
        return RestAssured
                .given().log().all()
                .body(order)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/api/orders/{orderId}/order-status", response.as(OrderResponse.class).getId())
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 주문_조회() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/orders")
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 주문_요청(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        OrderRequest order = new OrderRequest(orderTableId, orderLineItems);
        return RestAssured
                .given().log().all()
                .body(order)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/orders")
                .then().log().all().extract();
    }
}
