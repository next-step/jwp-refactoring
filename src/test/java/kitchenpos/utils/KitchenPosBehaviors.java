package kitchenpos.utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuDto;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;

public class KitchenPosBehaviors {
    private KitchenPosBehaviors() {
    }

    public static ExtractableResponse<Response> 상품_생성_요청(String name, int price) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("price", price);

        return RestAssured
                .given().contentType(ContentType.JSON).log().all()
                .when().body(params).post("/api/products")
                .then().log().all()
                .extract();
    }

    public static Product 상품_생성됨(String name, int price) {
        return 상품_생성_요청(name, price).as(Product.class);
    }

    public static ExtractableResponse<Response> 상품목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/products")
                .then().log().all()
                .extract();
    }

    public static List<Product> 상품목록_조회() {
        return 상품목록_조회_요청().jsonPath().getList("$", Product.class);
    }

    public static ExtractableResponse<Response> 메뉴그룹_생성_요청(String name) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        return RestAssured
                .given().contentType(ContentType.JSON).log().all()
                .when().body(params).post("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    public static MenuGroup 메뉴그룹_생성됨(String name) {
        return 메뉴그룹_생성_요청(name).as(MenuGroup.class);
    }

    public static ExtractableResponse<Response> 메뉴그룹_목록조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    public static List<MenuGroup> 메뉴그룹_목록조회() {
        return 메뉴그룹_목록조회_요청().jsonPath().getList("$", MenuGroup.class);
    }

    public static ExtractableResponse<Response> 메뉴_생성_요청(Menu menu) {
        MenuDto param = MenuDto.of(menu);
        return RestAssured
                .given().contentType(ContentType.JSON).log().all()
                .when().body(param).post("/api/menus")
                .then().log().all()
                .extract();
    }

    public static MenuDto 메뉴_생성됨(Menu menu) {
        return 메뉴_생성_요청(menu).as(MenuDto.class);
    }

    public static ExtractableResponse<Response> 메뉴_목록조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/menus")
                .then().log().all()
                .extract();
    }

    public static List<MenuDto> 메뉴_목록조회() {
        return 메뉴_목록조회_요청().jsonPath().getList("$", MenuDto.class);
    }

    public static ExtractableResponse<Response> 테이블_생성_요청(OrderTableRequest orderTableRequest) {
        return RestAssured
                .given().contentType(ContentType.JSON).log().all()
                .when().body(orderTableRequest).post("/api/tables")
                .then().log().all()
                .extract();
    }

    public static OrderTableResponse 테이블_생성됨(OrderTableRequest orderTableRequest) {
        return 테이블_생성_요청(orderTableRequest).as(OrderTableResponse.class);
    }

    public static ExtractableResponse<Response> 테이블_목록조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/tables")
                .then().log().all()
                .extract();
    }

    public static List<OrderTableResponse> 테이블_목록조회() {
        ExtractableResponse<Response> response = KitchenPosBehaviors.테이블_목록조회_요청();
        return response.jsonPath().getList("$", OrderTableResponse.class);
    }

    public static ExtractableResponse<Response> 테이블_공석여부_변경_요청(Long orderTableId, OrderTableRequest param) {
        String uri = String.format("/api/tables/%d/empty", orderTableId);
        return RestAssured
                .given().contentType(ContentType.JSON).log().all()
                .when().body(param).put(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 테이블_인원수_변경_요청(Long orderTableId, OrderTableRequest param) {
        String uri = String.format("/api/tables/%d/number-of-guests", orderTableId);
        return RestAssured
                .given().contentType(ContentType.JSON).log().all()
                .when().body(param).put(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 테이블그룹_생성_요청(TableGroupRequest tableGroupRequest) {
        return RestAssured
                .given().contentType(ContentType.JSON).log().all()
                .when().body(tableGroupRequest).post("/api/table-groups/")
                .then().log().all()
                .extract();
    }

    public static TableGroupResponse 테이블그룹_생성(TableGroupRequest tableGroupRequest) {
        return 테이블그룹_생성_요청(tableGroupRequest).as(TableGroupResponse.class);
    }

    public static ExtractableResponse<Response> 테이블그룹_해제_요청(Long tableGroupId) {
        String uri = String.format("/api/table-groups/%d", tableGroupId);
        return RestAssured
                .given().contentType(ContentType.JSON).log().all()
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_추가_요청(OrderRequest orderRequest) {
        return RestAssured
                .given().contentType(ContentType.JSON).log().all()
                .when().body(orderRequest).post("/api/orders/")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문상태변경_요청(Long orderId, OrderRequest param) {
        String uri = String.format("/api/orders/%d/order-status", orderId);

        return RestAssured
                .given().contentType(ContentType.JSON).log().all()
                .when().body(param).put(uri)
                .then().log().all()
                .extract();
    }
}
