package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.application.OrderServiceTest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.MenuGroupResponse;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.acceptance.MenuGroupAcceptanceTest.메뉴_그룹_생성되어_있음;
import static kitchenpos.acceptance.ProductAcceptanceTest.상품_생성되어_있음;
import static kitchenpos.acceptance.TableAcceptanceTest.주문_테이블_생성되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 관련 기능")
public class OrderAcceptanceTest extends AcceptanceTest {

    Order order1;
    Order order2;

    @BeforeEach
    public void init() {
        super.init();

        OrderTable 주문_테이블 = 주문_테이블_생성되어_있음(null, false, 0).as(OrderTable.class);
        ProductResponse 샐러드 = 상품_생성되어_있음("샐러드", 100).as(ProductResponse.class);
        MenuGroupResponse 채식_메뉴_그룹 = 메뉴_그룹_생성되어_있음("채식").as(MenuGroupResponse.class);
        MenuProductRequest 샐러드_2인 = new MenuProductRequest(샐러드.getId(), 2);
        MenuResponse 기본_메뉴 = MenuAcceptanceTest.메뉴_생성되어_있음("기본 메뉴", 200, 채식_메뉴_그룹.getId(), Arrays.asList(샐러드_2인)).as(MenuResponse.class);
        OrderLineItem 주문_항목1 = OrderServiceTest.주문_항목_생성(기본_메뉴.getId(), 1);
        OrderLineItem 주문_항목2 = OrderServiceTest.주문_항목_생성(기본_메뉴.getId(), 2);
        order1 = OrderServiceTest.주문_생성(주문_테이블.getId(), null, null, 주문_항목1);
        order2 = OrderServiceTest.주문_생성(주문_테이블.getId(), null, null, 주문_항목2);
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void 주문_생성() {
        // when
        ExtractableResponse<Response> response = 주문_생성_요청(order1);

        // then
        주문_생성됨(response);
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void 주문_목록_조회() {
        // given
        ExtractableResponse<Response> createResponse1 = 주문_생성_요청(order1);
        ExtractableResponse<Response> createResponse2 = 주문_생성_요청(order2);

        // when
        ExtractableResponse<Response> response = 주문_목록_조회_요청();

        // then
        주문_목록_응답됨(response);
        주문_목록_포함됨(response, Arrays.asList(createResponse1, createResponse2));
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void 주문_상태_변경() {
        // given
        ExtractableResponse<Response> createResponse = 주문_생성_요청(order1);

        // when
        String 변경할_상태 = OrderStatus.MEAL.name();
        ExtractableResponse<Response> response = 주문_상태_변경_요청(createResponse, 변경할_상태);

        // then
        주문_상태_변경됨(response, 변경할_상태);
    }

    public static ExtractableResponse<Response> 주문_생성_요청(Order order) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(order)
                .when().post("/api/orders")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/orders")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_상태_변경_요청(ExtractableResponse<Response> response, String orderStatus) {
        String location = response.header("Location");
        Order order = response.as(Order.class);
        order.setOrderStatus(orderStatus);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(order)
                .when().put(location + "/order-status")
                .then().log().all()
                .extract();
    }

    public static void 주문_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 주문_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 주문_목록_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        List<Long> expectedLineIds = createdResponses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[3]))
                .collect(Collectors.toList());

        List<Long> resultLineIds = response.jsonPath().getList(".", Order.class).stream()
                .map(Order::getId)
                .collect(Collectors.toList());

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    public static void 주문_상태_변경됨(ExtractableResponse<Response> response, String orderStatus) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        Order order = response.as(Order.class);
        assertThat(order.getOrderStatus()).isEqualTo(orderStatus);
    }
}
