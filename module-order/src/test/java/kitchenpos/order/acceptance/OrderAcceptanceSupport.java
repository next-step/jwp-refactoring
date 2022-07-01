package kitchenpos.order.acceptance;

import static kitchenpos.order.OrderTestFixture.감자튀김_FIXTURE;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.menu.dto.request.MenuGroupRequest;
import kitchenpos.menu.dto.request.MenuProductRequest;
import kitchenpos.menu.dto.request.MenuRequest;
import kitchenpos.menu.dto.response.MenuGroupResponse;
import kitchenpos.menu.dto.response.MenuResponse;
import kitchenpos.order.OrderTestFixture;
import kitchenpos.product.dto.request.ProductRequest;
import kitchenpos.product.dto.response.ProductResponse;
import kitchenpos.table.dto.request.OrderTableRequest;
import kitchenpos.table.dto.response.OrderTableResponse;
import org.springframework.http.MediaType;

public class OrderAcceptanceSupport {

    public static MenuResponse 치킨세트_메뉴_등록함() {
        MenuRequest 치킨_메뉴 = 치킨세트_메뉴_가져오기();
        ExtractableResponse<Response> response = 메뉴등록을_요청(치킨_메뉴);

        return response.as(MenuResponse.class);
    }

    public static MenuRequest 치킨세트_메뉴_가져오기() {
        ProductResponse 후라이드_치킨 = 상품_등록요청(OrderTestFixture.후라이드_치킨_FIXTURE).as(ProductResponse.class);
        ProductResponse 감자튀김 = 상품_등록요청(감자튀김_FIXTURE).as(ProductResponse.class);

        MenuProductRequest 메뉴_상품_후라이드_치킨 = new MenuProductRequest(후라이드_치킨.getId(), 1);
        MenuProductRequest 메뉴_상품_감자튀김 = new MenuProductRequest(감자튀김.getId(), 1);
        MenuGroupResponse 치킨_메뉴_그룹 = 메뉴_그룹_등록요청(new MenuGroupRequest("치킨_메뉴")).as(MenuGroupResponse.class);

        MenuRequest 치킨_메뉴 = new MenuRequest("후라이드치킨 세트", BigDecimal.valueOf(18000L), 치킨_메뉴_그룹.getId(),
            Arrays.asList(메뉴_상품_후라이드_치킨, 메뉴_상품_감자튀김));

        return 치킨_메뉴;
    }

    public static OrderTableResponse 주문_테이블_등록됨(OrderTableRequest orderTableRequest) {
        ExtractableResponse<Response> response = 주문_테이블_등록요청(orderTableRequest);
        return response.as(OrderTableResponse.class);
    }

    public static ExtractableResponse<Response> 주문_테이블_등록요청(OrderTableRequest orderTable) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(orderTable)
            .when().post("/api/tables")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 메뉴등록을_요청(MenuRequest menuRequest) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(menuRequest)
            .when().post("/api/menus")
            .then().log().all().
            extract();
    }

    public static ExtractableResponse<Response> 상품_등록요청(ProductRequest productRequest) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(productRequest)
            .when().post("/api/products")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 메뉴_그룹_등록요청(MenuGroupRequest menuGroupRequest) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(menuGroupRequest)
            .when().post("/api/menu-groups")
            .then().log().all()
            .extract();
    }
}
