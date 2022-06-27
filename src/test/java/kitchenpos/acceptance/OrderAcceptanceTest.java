package kitchenpos.acceptance;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.order.consts.OrderStatus;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("주문 관련 인수테스트")
class OrderAcceptanceTest extends AcceptanceTest {


    private static final String ORDER_PATH = "/api/orders";

    private ProductResponse 뿌링클;
    private ProductResponse 투움바;
    private ProductResponse 로제떡볶이;
    private MenuGroupResponse 인기메뉴;
    private MenuResponse 메뉴_뿌링클;
    private MenuResponse 메뉴_투움바;
    private MenuResponse 메뉴_로제떡볶이;
    private MenuResponse 메뉴_등록안됨;
    private OrderTableResponse 주문테이블;

    @BeforeEach
    public void setUp(){
        super.setUp();
        //given
        뿌링클 = ProductAcceptanceTest.상품_등록_되어있음("뿌링클", 27000);
        투움바 = ProductAcceptanceTest.상품_등록_되어있음("투움바", 30000);
        로제떡볶이 = ProductAcceptanceTest.상품_등록_되어있음("로제떡볶이", 19000);
        
        인기메뉴 = MenuGroupAcceptanceTest.메뉴_그룹_등록_되어있음("인기 메뉴");

        메뉴_뿌링클 = MenuAcceptanceTest.메뉴_등록_되어있음("뿌링클", 27000, Arrays.asList(뿌링클), 인기메뉴);
        메뉴_투움바 = MenuAcceptanceTest.메뉴_등록_되어있음("투움바", 30000, Arrays.asList(투움바), 인기메뉴);
        메뉴_로제떡볶이 = MenuAcceptanceTest.메뉴_등록_되어있음("로제떡볶이", 19000, Arrays.asList(로제떡볶이), 인기메뉴);
        메뉴_등록안됨 = new MenuResponse(99999999L,null,null,null, null);
        주문테이블  = TableAcceptanceTest.주문_테이블_등록_되어있음(3);
    }

    /**
     * Feature: 주문 기능
     *
     *   Background
     *     Given 상품이 등록되어있다.
     *     And   메뉴 그룹이 등록되어있다.
     *     And   메뉴가 등록되어있다.
     *     And   주문테이블이 등록되어있다.
     *
     *   Scenario: 주문 기능을 관리
     *     Given  요청할 주문을 생성하고
     *     When   주문울 둥록하면
     *     Then   주문이 등록된다.
     *     Given  빈테이블이 등록되어있고
     *     And    빈테이블로 요청할 주문을 생성하고
     *     When   주문을 등록하면
     *     Then   주문 등록이 실패된다.
     *     Given  등록안된 메뉴를 포함하여 요청할 주문을 생성하고
     *     When   주문을 등록하면
     *     Then   주문 등록이 실패된다.
     *
     *     Given 주문이 등록되어있고
     *     When  주문 상태를 변경하면
     *     Then  주문 상태가 변경된다.
     *     When  주문 상태가 계산완료인 주문의 주문상태를 변경하면
     *     Then  주문 상태 변경이 실패된다.
     *
     * */
    @DisplayName("주문 기능을 관리한다.")
    @TestFactory
    Stream<DynamicTest> manageOrder() {
        return Stream.of(
                dynamicTest("주문을 등록 한다.", () -> {
                    //given
                    Map<String, Object> params1 = 요청할_주문_생성(주문테이블, Arrays.asList(메뉴_뿌링클, 메뉴_투움바));

                    //when
                    ExtractableResponse<Response> response1 = 주문_등록_요청(params1);

                    //then
                    주문_등록됨(response1, Arrays.asList(메뉴_뿌링클, 메뉴_투움바));

                    //given
                    OrderTableResponse 빈테이블 = TableAcceptanceTest.빈_테이블_등록_되어있음(3);
                    Map<String, Object> params2 = 요청할_주문_생성(빈테이블, Arrays.asList(메뉴_뿌링클, 메뉴_투움바));

                    //when
                    ExtractableResponse<Response> response2 = 주문_등록_요청(params2);

                    //then
                    주문_등록_실패됨(response2);
                    
                    //given
                    Map<String, Object> params3 = 요청할_주문_생성(주문테이블, Arrays.asList(메뉴_뿌링클, 메뉴_등록안됨));

                    //when
                    ExtractableResponse<Response> response3 = 주문_등록_요청(params3);

                    //then
                    주문_등록_실패됨(response3);

                }),

                dynamicTest("주문 상태를 업데이트 한다.", () -> {
                    //given
                    Map<String, Object> params1 = 요청할_주문_생성(주문테이블, Collections.singletonList(메뉴_로제떡볶이));
                    ExtractableResponse<Response> created = 주문_등록_요청(params1);

                    //when
                    ExtractableResponse<Response> response = 주문_상태_업데이트_요청(created, OrderStatus.COMPLETION);

                    //then
                    주문_상태_업데이트됨(response, OrderStatus.COMPLETION);

                    //when
                    ExtractableResponse<Response> response2 = 주문_상태_업데이트_요청(created, OrderStatus.MEAL);

                    //then
                    주문_상태_업데이트_실패됨(response2);


                }),

                dynamicTest("주문 목록을 조회한다.", () -> {
                    //when
                    ExtractableResponse<Response> response = 주문_목록_조회_요청();

                    //then
                    주문_목록_조회됨(response,Arrays.asList(OrderStatus.COOKING, OrderStatus.COMPLETION));

                })
        );

    }


    public static void 주문_동록_및_주문_상태_업데이트_되어있음(OrderTableResponse orderTable, List<MenuResponse> menus, OrderStatus orderStatus) {
        Map<String, Object> params = 요청할_주문_생성(orderTable, menus);
        주문_상태_업데이트_요청(주문_등록_요청(params), orderStatus);
    }

    private static Map<String, Object> 요청할_주문_생성(OrderTableResponse orderTable, List<MenuResponse> menus) {
        Map<String, Object> params = new HashMap<>();
        params.put("orderTableId", orderTable.getId());
        params.put("orderLineItems", 요청할_주문_항목_리스트_생성(menus));
        return params;
    }

    private static List<Map<String, Object>> 요청할_주문_항목_리스트_생성(List<MenuResponse> menus) {
        List<Map<String, Object>> params = new ArrayList<>();
        for (MenuResponse menu : menus) {
            Map<String, Object> orderLineItemParam = new HashMap<>();
            orderLineItemParam.put("menuId", menu.getId());
            orderLineItemParam.put("quantity", 10);
            params.add(orderLineItemParam);
        }
        return params;
    }

    private static ExtractableResponse<Response> 주문_상태_업데이트_요청(ExtractableResponse<Response> response,
                                                               OrderStatus orderStatus) {
        String location = response.header("Location");

        Map<String, Object> params = new HashMap<>();
        params.put("orderStatus",orderStatus);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put(location+"/order-status")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 주문_등록_요청(Map<String, Object> params) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post(ORDER_PATH)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 주문_목록_조회_요청() {
        return RestAssured.given().log().all()
                .when().get(ORDER_PATH)
                .then().log().all()
                .extract();
    }

    private void 주문_등록됨(ExtractableResponse<Response> response, List<MenuResponse> menus) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.body().jsonPath().getString("orderStatus")).isEqualTo(OrderStatus.COOKING.name());
        assertThat(response.body().jsonPath().getList("orderLineItems.menuId", Long.class))
                .containsExactlyInAnyOrderElementsOf(menus.stream().map(MenuResponse::getId).collect(Collectors.toList()));
    }

    private void 주문_등록_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private void 주문_상태_업데이트됨(ExtractableResponse<Response> response, OrderStatus orderStatus) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("orderStatus")).isEqualTo(orderStatus.name());
    }

    private void 주문_상태_업데이트_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private void 주문_목록_조회됨(ExtractableResponse<Response> response,List<OrderStatus> orderStatuses) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("orderStatus", String.class))
                .containsExactlyInAnyOrderElementsOf(orderStatuses.stream().map(OrderStatus::name).collect(Collectors.toList()));
    }



}
