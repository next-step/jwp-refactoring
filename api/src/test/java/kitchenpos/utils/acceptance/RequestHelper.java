package kitchenpos.utils.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.order.dto.OrderTableEmptyRequest;
import kitchenpos.order.dto.OrderTableNumberOfGuestsRequest;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.tablegroup.dto.TableGroupRequest;

public class RequestHelper {
    public static ExtractableResponse<Response> 테이블_그룹_생성_요청(Long 주문_테이블_고객_수_2명_번호, Long 주문_테이블_고객_수_5명_번호) {
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(주문_테이블_고객_수_2명_번호, 주문_테이블_고객_수_5명_번호));
        return RestAssured.given().log().all()
                .body(tableGroupRequest)
                .contentType(ContentType.JSON)
                .when().post("/api/table-groups")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_생성_요청(int 고객_수, boolean 비어있음_여부) {
        OrderTableRequest orderTableRequest = new OrderTableRequest(고객_수, 비어있음_여부);
        return RestAssured.given().log().all()
                .body(orderTableRequest)
                .contentType(ContentType.JSON)
                .when().post("/api/tables")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_비어있음_요청(Long 주문_테이블_번호, boolean 비어있음_여부) {
        OrderTableEmptyRequest orderTableEmptyRequest = new OrderTableEmptyRequest(비어있음_여부);
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(orderTableEmptyRequest)
                .when().put("/api/tables/" + 주문_테이블_번호 + "/empty")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_생성_요청(Long 주문_테이블_번호, Long 메뉴_번호, int 메뉴_수량) {
        OrderRequest orderRequest = new OrderRequest(주문_테이블_번호, Arrays.asList(new OrderLineItemRequest(메뉴_번호, 메뉴_수량)));
        return RestAssured.given().log().all()
                .body(orderRequest)
                .contentType(ContentType.JSON)
                .when().post("/api/orders")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 테이블_그룹_제거_요청(Long 테이블_그룹_번호) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("/api/table-groups/" + 테이블_그룹_번호)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_상태_변경_요청(Long 주문_번호, OrderStatus orderStatus) {
        OrderStatusRequest orderStatusRequest = new OrderStatusRequest(orderStatus.name());
        return RestAssured.given().log().all()
                .body(orderStatusRequest)
                .contentType(ContentType.JSON)
                .when().put("/api/orders/" + 주문_번호 + "/order-status")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴_생성_요청(String 메뉴_이름, int 가격, Long 메뉴_그룹_번호, Long 상품_번호, Long 상품_수량) {
        MenuProductRequest 메뉴_상품_요청 = new MenuProductRequest(상품_번호, 상품_수량);
        MenuRequest menuRequest = new MenuRequest(메뉴_이름, 가격, 메뉴_그룹_번호, Arrays.asList(메뉴_상품_요청));
        return RestAssured.given().log().all()
                .body(menuRequest)
                .contentType(ContentType.JSON)
                .when().post("/api/menus/")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_조회_요청() {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/api/tables")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_고객수_변경_요청(Long 주문_테이블_번호, int 고객수5명) {
        OrderTableNumberOfGuestsRequest orderTableNumberOfGuestsRequest = new OrderTableNumberOfGuestsRequest(고객수5명);
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(orderTableNumberOfGuestsRequest)
                .when().put("/api/tables/" + 주문_테이블_번호 + "/number-of-guests")
                .then().log().all()
                .extract();
    }

    public static void 주문_생성_요청_응답_확인(ExtractableResponse<Response> 주문_생성_요청_응답) {
        assertThat(주문_생성_요청_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(주문_생성_요청_응답.body().jsonPath().get("orderStatus").toString()).isEqualTo(OrderStatus.COOKING.name());
    }

    public static ExtractableResponse<Response> 주문_조회_요청() {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/api/orders")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴_그룹_조회_요청() {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/api/menu-groups/")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴_그룹_생성_요청(String 요청_메뉴_그룹) {
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest(요청_메뉴_그룹);
        return RestAssured.given().log().all()
                .body(menuGroupRequest)
                .contentType(ContentType.JSON)
                .when().post("/api/menu-groups/")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 상품_생성_요청(String 상품_이름, int 상품_가격) {
        ProductRequest productRequest = new ProductRequest(상품_이름, 상품_가격);
        return RestAssured.given().log().all()
                .body(productRequest)
                .contentType(ContentType.JSON)
                .when().post("/api/products")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴_조회_요청() {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/api/menus/")
                .then().log().all()
                .extract();
    }
}
