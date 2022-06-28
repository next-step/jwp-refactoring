package kitchenpos.acceptance.helper;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuDto;
import kitchenpos.product.domain.Product;

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

    public static ExtractableResponse<Response> 테이블_생성_요청(OrderTable orderTable) {
        return RestAssured
                .given().contentType(ContentType.JSON).log().all()
                .when().body(orderTable).post("/api/tables")
                .then().log().all()
                .extract();
    }

    public static OrderTable 테이블_생성됨(OrderTable orderTable) {
        return 테이블_생성_요청(orderTable).as(OrderTable.class);
    }

    public static ExtractableResponse<Response> 테이블_목록조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/tables")
                .then().log().all()
                .extract();
    }

    public static List<OrderTable> 테이블_목록조회() {
        ExtractableResponse<Response> response = KitchenPosBehaviors.테이블_목록조회_요청();
        return response.jsonPath().getList("$", OrderTable.class);
    }

    public static ExtractableResponse<Response> 테이블_공석여부_변경_요청(Long orderTableId, OrderTable param) {
        String uri = String.format("/api/tables/%d/empty", orderTableId);
        return RestAssured
                .given().contentType(ContentType.JSON).log().all()
                .when().body(param).put(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 테이블_인원수_변경_요청(Long orderTableId, OrderTable param) {
        String uri = String.format("/api/tables/%d/number-of-guests", orderTableId);
        return RestAssured
                .given().contentType(ContentType.JSON).log().all()
                .when().body(param).put(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 테이블그룹_생성_요청(TableGroup tableGroup) {
        return RestAssured
                .given().contentType(ContentType.JSON).log().all()
                .when().body(tableGroup).post("/api/table-groups/")
                .then().log().all()
                .extract();
    }

    public static TableGroup 테이블그룹_생성(TableGroup tableGroup) {
        return 테이블그룹_생성_요청(tableGroup).as(TableGroup.class);
    }

    public static ExtractableResponse<Response> 테이블그룹_해제_요청(Long tableGroupId) {
        String uri = String.format("/api/table-groups/%d", tableGroupId);
        return RestAssured
                .given().contentType(ContentType.JSON).log().all()
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_추가_요청(Order order) {
        return RestAssured
                .given().contentType(ContentType.JSON).log().all()
                .when().body(order).post("/api/orders/")
                .then().log().all()
                .extract();
    }

    public static Order 주문_추가(Order order) {
        return 주문_추가_요청(order).as(Order.class);
    }

    public static ExtractableResponse<Response> 주문상태변경_요청(Long orderId, Order param) {
        String uri = String.format("/api/orders/%d/order-status", orderId);

        return RestAssured
                .given().contentType(ContentType.JSON).log().all()
                .when().body(param).put(uri)
                .then().log().all()
                .extract();
    }
}
