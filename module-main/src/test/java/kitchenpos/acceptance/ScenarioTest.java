package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.acceptance.application.AcceptanceTest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menuGroup.dto.MenuGroupRequest;
import kitchenpos.menuGroup.dto.MenuGroupResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.table.dto.*;
import kitchenpos.tableGroup.dto.TableGroupRequest;
import kitchenpos.tableGroup.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ScenarioTest extends AcceptanceTest {
    /**
     * 정상 시나리오
     * <p>
     * 1.
     * Given "추천메뉴"가 있고
     * When 메뉴 그룹에 등록 요청하면
     * Then "추천메뉴" 메뉴 그룹이 등록된다.
     * <p>
     * 2.
     * When 메뉴 그룹 리스트를 조회요청 하면
     * Then 메뉴 그룹 리스트가 조회된다
     * <p>
     * 3.
     * Given 상품 "진라면", "떡볶이" 가 있고
     * When 상품에 등록 요청을 하면
     * Then "진라면", "떡볶이" 상품이 등록된다.
     * <p>
     * 4.
     * When 상품 리스트를 조회 요청하면
     * Then 상품 리스트가 조회된다.
     * <p>
     * 5.
     * Given "추천메뉴", "진라면", "라면"이 있고
     * When 메뉴에 등록 요청하면
     * Then 메뉴에 등록된다
     * <p>
     * 6.
     * When 메뉴 리스트를 조회요청 하면
     * Then 메뉴 리스트가 조회된다
     * <p>
     * 7.
     * Given 손님 3, empty = false 가 있고
     * When 테이블 등록 요청을 하면
     * Then 테이블이 등록된다
     * <p>
     * 8.
     * Given 손님 4, empty = true 가 있고
     * When 테이블 등록 요청을 하면
     * Then 테이블이 등록된다
     * <p>
     * 9.
     * When 테이블 리스트를 조회요청 하면
     * Then 테이블 리스트가 조회된다
     * <p>
     * 10.
     * Given 고객 수 4명이 있고
     * When 테이블의 고객 수 변경 요청을 하면
     * Then 테이블의 고객 수는 4명이 된다
     * <p>
     * 11.
     * Given 테이블, 추천메뉴, 수량 1개가 있고
     * When 주문 등록요청 하면
     * Then 주문이 등록된다
     * <p>
     * 12.
     * When 주문 리스트를 조회요청 하면
     * Then 주문리스트가 조회된다
     * <p>
     * 13.
     * Given empty = false 를
     * When 테이블 변경 요청을 하면
     * Then 테이블의 empty 상태는 false가 된다
     * <p>
     * 14.
     * Given 주문상태 MEAL이 있고
     * When 주문상태를 변경요청 하면
     * Then 주문상태가 MEAL이 된다
     * <p>
     * 15.
     * Given 테이블이 두 개 있고, 테이블 empty가 false 이고
     * When 단체 테이블 요청을 하면
     * Then 단체 테이블이 등록된다
     * <p>
     * 16.
     * Given 주문상태 COMPLETE 있고
     * When 주문상태를 변경요청 하면
     * Then 주문상태가 COMPLETE 된다
     */
    @DisplayName("키친포스 등록/조회 시나리오")
    @Test
    void scenario() {
        // 1.
        // given & when
        ExtractableResponse<Response> menuGroupResponse1 = 메뉴_그룹_생성_요청("추천메뉴");
        // then
        메뉴_그룹_생성됨(menuGroupResponse1, "추천메뉴");

        // 2.
        // given & when
        ExtractableResponse<Response> menuGroupResponse2 = 메뉴_그룹_조회_요청();
        // then
        메뉴_그룹_조회됨(menuGroupResponse2, "추천메뉴");

        // 3.
        // given & when
        ExtractableResponse<Response> productResponse1 = 상품_생성_요청("진라면", 1_000L);
        ExtractableResponse<Response> productResponse2 = 상품_생성_요청("튀김", 2_000L);
        // then
        상품_생성됨(productResponse1, "진라면");
        상품_생성됨(productResponse2, "튀김");


        // 4.
        //given & when
        ExtractableResponse<Response> productResponse3 = 상품_조회_요청();
        //then
        상품_조회됨(productResponse3, 2);

        // 5.
        // given
        Long 추천메뉴_id = menuGroupResponse1.as(MenuGroupResponse.class).getId();
        Long 진라면_id = productResponse1.as(ProductResponse.class).getId();
        Long 튀김_id = productResponse2.as(ProductResponse.class).getId();
        List<MenuProductRequest> menuProductRequests = Arrays.asList(new MenuProductRequest(진라면_id, 1), new MenuProductRequest(튀김_id, 2));
        // when

        ExtractableResponse<Response> menuResponse = 메뉴_생성_요청("진라면+튀김", 추천메뉴_id, menuProductRequests, 2_900);
        // then
        메뉴_생성됨(menuResponse, "진라면+튀김");

        // 6.
        // given & when
        ExtractableResponse<Response> 메뉴_조회_요청 = 메뉴_조회_요청();
        // then
        메뉴_조회됨(메뉴_조회_요청, 1);

        // 7 & 8.
        // given & when
        ExtractableResponse<Response> orderTableResponse1 = 주문_테이블_생성_요청(3, false);
        ExtractableResponse<Response> orderTableResponse2 = 주문_테이블_생성_요청(4, true);
        // then
        주문_테이블_생성됨(orderTableResponse1, 3);
        주문_테이블_생성됨(orderTableResponse2, 4);

        // 9.
        // given & when
        ExtractableResponse<Response> orderTableResponse3 = 주문_테이블_조회_요청();
        // then
        주문_테이블_조회됨(orderTableResponse3, 2);

        // 10.
        // given
        Long 주문_테이블_id = orderTableResponse1.as(OrderTableResponse.class).getId();
        // when
        ExtractableResponse<Response> orderTableResponse4 = 테이블_고객수_변경_요청(주문_테이블_id, 10);
        // then
        테이블_고객수_변경됨(orderTableResponse4, 10);

        // 11.
        //given
        Long 메뉴_id = menuResponse.as(MenuResponse.class).getId();
        //when
        ExtractableResponse<Response> 주문_생성_응답 = 주문_생성_요청(주문_테이블_id, Collections.singletonList(new OrderLineItemRequest(메뉴_id, 3)));
        //then
        주문_생성됨(주문_생성_응답, OrderStatus.COOKING);

        // 12.
        // given & when
        ExtractableResponse<Response> 주문_조회_응답 = 주문_조회_요청();
        // then
        주문_조회됨(주문_조회_응답, 1);

        // 13.
        // given
        Long 주문_테이블_id_2 = orderTableResponse2.as(OrderTableResponse.class).getId();
        // when
        ExtractableResponse<Response> 테이블_자리상태_변경_응답 = 테이블_자리상태_변경_요청(주문_테이블_id_2, false);
        //then
        테이블_자리상태_변경됨(테이블_자리상태_변경_응답, false);

        // 14.
        // given
        Long 주문_id = 주문_생성_응답.as(OrderResponse.class).getId();
        // when
        ExtractableResponse<Response> 주문_상태_변경_응답 = 주문_상태_변경_요청(주문_id, OrderStatus.MEAL);
        // then
        주문_상태_변경됨(주문_상태_변경_응답, OrderStatus.MEAL);

        // 15.
        // given & when
        ExtractableResponse<Response> 단체_테이블_등록_응답 = 단체_테이블_등록_요청(Arrays.asList(new OrderTableIdRequest(주문_테이블_id), new OrderTableIdRequest(주문_테이블_id_2)));
        // then
        단체_테이블_등록됨(단체_테이블_등록_응답, 2);

        // 16.
        // when
        ExtractableResponse<Response> 주문_상태_변경_응답2 = 주문_상태_변경_요청(주문_id, OrderStatus.COMPLETION);
        // then
        주문_상태_변경됨(주문_상태_변경_응답2, OrderStatus.COMPLETION);

    }

    public ExtractableResponse<Response> 상품_생성_요청(String name, long price) {
        ProductRequest productRequest = new ProductRequest(name, BigDecimal.valueOf(price));

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(productRequest)
                .when()
                .post("/api/products")
                .then().log().all()
                .extract();
    }

    public void 상품_생성됨(ExtractableResponse<Response> response, String name) {
        ProductResponse productResponse = response.as(ProductResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(productResponse.getName()).isEqualTo(name);
    }

    public ExtractableResponse<Response> 상품_조회_요청() {

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/products")
                .then().log().all()
                .extract();
    }

    public void 상품_조회됨(ExtractableResponse<Response> response, int expectedSize) {
        List<ProductResponse> list = response.jsonPath().getList(".", ProductResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(list).hasSize(expectedSize);
    }


    public static ExtractableResponse<Response> 메뉴_그룹_생성_요청(String name) {
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest(name);
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menuGroupRequest)
                .when()
                .post("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    public static void 메뉴_그룹_생성됨(ExtractableResponse<Response> response, String name) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        MenuGroupResponse menuGroupResponse = response.as(MenuGroupResponse.class);
        assertThat(menuGroupResponse.getName()).isEqualTo(name);
    }

    public static ExtractableResponse<Response> 메뉴_그룹_조회_요청() {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/menu-groups")
                .then()
                .log().all()
                .extract();
    }

    public static void 메뉴_그룹_조회됨(ExtractableResponse<Response> response, String name) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<MenuGroupResponse> menuGroups = response.jsonPath().getList(".", MenuGroupResponse.class);
        assertThat(menuGroups.get(0).getName()).isEqualTo(name);
    }

    public ExtractableResponse<Response> 주문_테이블_생성_요청(int numberOfGuests, boolean empty) {
        OrderTableRequest menuRequest = new OrderTableRequest(numberOfGuests, empty);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menuRequest)
                .when()
                .post("/api/tables")
                .then().log().all()
                .extract();
    }

    public void 주문_테이블_생성됨(ExtractableResponse<Response> response, int numberOfGuests) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        OrderTableResponse orderTableResponse = response.as(OrderTableResponse.class);
        assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    public static ExtractableResponse<Response> 주문_테이블_조회_요청() {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/tables")
                .then()
                .log().all()
                .extract();
    }

    public static void 주문_테이블_조회됨(ExtractableResponse<Response> response, int expectedSize) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<OrderTableResponse> menuGroups = response.jsonPath().getList(".", OrderTableResponse.class);
        assertThat(menuGroups).hasSize(expectedSize);
    }

    public ExtractableResponse<Response> 메뉴_생성_요청(String name, Long menuGroupId, List<MenuProductRequest> products, long price) {
        MenuRequest menuRequest = new MenuRequest(name, BigDecimal.valueOf(price), menuGroupId, products);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menuRequest)
                .when()
                .post("/api/menus")
                .then().log().all()
                .extract();
    }

    public void 메뉴_생성됨(ExtractableResponse<Response> menuResponse, String name) {
        assertThat(menuResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        MenuResponse response = menuResponse.as(MenuResponse.class);
        assertThat(response.getName()).isEqualTo(name);
    }

    public static ExtractableResponse<Response> 메뉴_조회_요청() {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/menus")
                .then()
                .log().all()
                .extract();
    }

    public static void 메뉴_조회됨(ExtractableResponse<Response> response, int expectedSize) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<MenuResponse> menuGroups = response.jsonPath().getList(".", MenuResponse.class);
        assertThat(menuGroups).hasSize(expectedSize);
    }

    public ExtractableResponse<Response> 테이블_고객수_변경_요청(Long orderTableId, int numberOfGuests) {
        NumberOfGuestsRequest requestBody = new NumberOfGuestsRequest(numberOfGuests);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestBody)
                .when()
                .put("/api/tables/{id}/number-of-guests", orderTableId)
                .then().log().all()
                .extract();
    }

    public static void 테이블_고객수_변경됨(ExtractableResponse<Response> response, int expectedNumberOfGuests) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        OrderTableResponse orderTableResponse = response.as(OrderTableResponse.class);
        assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(expectedNumberOfGuests);
    }

    public ExtractableResponse<Response> 주문_생성_요청(Long orderTableId, List<OrderLineItemRequest> orderLineItemRequests) {
        OrderRequest requestBody = new OrderRequest(orderTableId, orderLineItemRequests);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestBody)
                .when()
                .post("/api/orders")
                .then().log().all()
                .extract();
    }

    public void 주문_생성됨(ExtractableResponse<Response> response, OrderStatus orderStatus) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        OrderResponse orderResponse = response.as(OrderResponse.class);
        assertThat(orderResponse.getOrderStatus()).isEqualTo(orderStatus.name());
    }

    public static ExtractableResponse<Response> 주문_조회_요청() {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/orders")
                .then()
                .log().all()
                .extract();
    }

    public static void 주문_조회됨(ExtractableResponse<Response> response, int expectedSize) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<OrderResponse> orderResponse = response.jsonPath().getList(".", OrderResponse.class);
        assertThat(orderResponse).hasSize(expectedSize);
    }

    public ExtractableResponse<Response> 테이블_자리상태_변경_요청(Long orderTableId, boolean empty) {
        OrderTableEmptyRequest requestBody = new OrderTableEmptyRequest(empty);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestBody)
                .when()
                .put("/api/tables/{id}/empty", orderTableId)
                .then().log().all()
                .extract();
    }

    public void 테이블_자리상태_변경됨(ExtractableResponse<Response> response, boolean expectedEmpty) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        OrderTableResponse orderTableResponse = response.as(OrderTableResponse.class);
        assertThat(orderTableResponse.isEmpty()).isEqualTo(expectedEmpty);
    }

    public ExtractableResponse<Response> 주문_상태_변경_요청(Long orderId, OrderStatus orderStatus) {
        OrderStatusRequest requestBody = new OrderStatusRequest(orderStatus);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestBody)
                .when()
                .put("/api/orders/{id}/order-status", orderId)
                .then().log().all()
                .extract();
    }

    public void 주문_상태_변경됨(ExtractableResponse<Response> response, OrderStatus expectedOrderStatus) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        OrderResponse orderResponse = response.as(OrderResponse.class);
        assertThat(orderResponse.getOrderStatus()).isEqualTo(expectedOrderStatus.name());
    }

    public ExtractableResponse<Response> 단체_테이블_등록_요청(List<OrderTableIdRequest> requests) {
        TableGroupRequest productRequest = new TableGroupRequest(requests);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(productRequest)
                .when()
                .post("/api/table-groups")
                .then().log().all()
                .extract();
    }

    public void 단체_테이블_등록됨(ExtractableResponse<Response> response, int expectedTableSize) {
        TableGroupResponse orderTableResponse = response.as(TableGroupResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(orderTableResponse.getOrderTableResponses().size()).isEqualTo(expectedTableSize);
    }


}
