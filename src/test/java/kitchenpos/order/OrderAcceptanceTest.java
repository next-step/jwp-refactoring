package kitchenpos.order;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderSaveRequest;
import kitchenpos.order.dto.OrderStatusUpdateRequest;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static kitchenpos.menu.MenuAcceptanceTest.메뉴_등록_되어있음;
import static kitchenpos.menugroup.MenuGroupAcceptanceTest.메뉴그룹_등록되어있음;
import static kitchenpos.table.OrderTableAcceptanceTest.주문_테이블_등록되어_있음;
import static kitchenpos.product.ProductAcceptanceTest.상품_등록되어_있음;
import static kitchenpos.menu.fixtures.MenuFixtures.메뉴등록요청;
import static kitchenpos.menugroup.fixtures.MenuGroupFixtures.한마리메뉴그룹요청;
import static kitchenpos.menu.fixtures.MenuProductFixtures.메뉴상품등록요청;
import static kitchenpos.order.fixtures.OrderFixtures.*;
import static kitchenpos.order.fixtures.OrderLineItemFixtures.주문정보_등록요청;
import static kitchenpos.table.fixtures.OrderTableFixtures.주문가능_다섯명테이블요청;
import static kitchenpos.table.fixtures.OrderTableFixtures.주문불가_다섯명테이블요청;
import static kitchenpos.product.fixtures.ProductFixtures.양념치킨요청;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * packageName : kitchenpos.acceptance
 * fileName : OrderAcceptanceTest
 * author : haedoang
 * date : 2021/12/22
 * description :
 */
@DisplayName("주문 인수테스트")
public class OrderAcceptanceTest extends AcceptanceTest {
    private Long 주문가능_5인테이블_ID;
    private Long 주문불가_5인테이블_ID;
    private Long 메뉴_ID;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        ProductResponse 양념치킨 = 상품_등록되어_있음(양념치킨요청());
        MenuGroupResponse 한마리메뉴그룹 = 메뉴그룹_등록되어있음(한마리메뉴그룹요청());

        MenuResponse 양념치킨하나메뉴 = 메뉴_등록_되어있음(
                메뉴등록요청(
                        "양념치킨하나",
                        양념치킨.getPrice(),
                        한마리메뉴그룹.getId(),
                        Lists.newArrayList(메뉴상품등록요청(양념치킨.getId(), 1L)
                        )
                )
        );

        주문가능_5인테이블_ID = 주문_테이블_등록되어_있음(주문가능_다섯명테이블요청()).getId();
        주문불가_5인테이블_ID = 주문_테이블_등록되어_있음(주문불가_다섯명테이블요청()).getId();
        메뉴_ID = 양념치킨하나메뉴.getId();
    }

    @Test
    @DisplayName("주믄을 등록한다.")
    public void create() throws Exception {
        // given
        OrderSaveRequest 주문등록요청 = 주문등록요청(주문가능_5인테이블_ID, Lists.newArrayList(주문정보_등록요청(메뉴_ID, 1L)));

        // when
        ExtractableResponse<Response> response = 주문_등록_요청함(주문등록요청);

        // then
        주문_등록됨(response);
    }

    @Test
    @DisplayName("등록되지 않은 테이블인 경우 주문을 등록할 수 없다.")
    public void createFailByUnknownTable() {
        // given
        Long 등록되지않은_테이블_ID = Long.MAX_VALUE;
        OrderSaveRequest 주문등록요청 = 주문등록요청(등록되지않은_테이블_ID, Lists.newArrayList(주문정보_등록요청(메뉴_ID, 1L)));

        // when
        ExtractableResponse<Response> response = 주문_등록_요청함(주문등록요청);

        // then
        주문_등록_실패함(response);
    }

    @Test
    @DisplayName("존재하지 않은 메뉴인 경우 주문을 등록할 수 없다.")
    public void createFailByUnknownMenu() {
        // given
        Long 존재하지_않는_메뉴_ID = Long.MAX_VALUE;
        OrderSaveRequest 주문등록요청 = 주문등록요청(주문가능_5인테이블_ID, Lists.newArrayList(주문정보_등록요청(존재하지_않는_메뉴_ID, 1L)));

        // when
        ExtractableResponse<Response> response = 주문_등록_요청함(주문등록요청);

        // then
        주문_등록_실패함(response);
    }

    @Test
    @DisplayName("빈 테이블인 경우 주문을 등록할 수 없다.")
    public void createFailByTableEmpty() {
        // given
        OrderSaveRequest 주문등록요청 = 주문등록요청(주문불가_5인테이블_ID, Lists.newArrayList(주문정보_등록요청(메뉴_ID, 1L)));

        // when
        ExtractableResponse<Response> response = 주문_등록_요청함(주문등록요청);

        // then
        주문_등록_실패함(response);
    }

    @Test
    @DisplayName("주문 상태를 변경할 수 있다.")
    public void changeOrderStatus() {
        // given
        OrderResponse savedOrder = 주문등록되어있음(주문등록요청(주문가능_5인테이블_ID, Lists.newArrayList(주문정보_등록요청(메뉴_ID, 1L))));

        // when
        ExtractableResponse<Response> response = 주문_상태_변경요청함(savedOrder.getId(), 식사완료로_변경요청());

        // then
        상태변경됨(response, 식사완료로_변경요청().getOrderStatus());
    }

    @Test
    @DisplayName("주문 상태가 완료인 경우 변경할 수 없다")
    public void changeOrderStatusFail() {
        // given
        OrderResponse savedOrder = 주문등록되어있음(주문등록요청(주문가능_5인테이블_ID, Lists.newArrayList(주문정보_등록요청(메뉴_ID, 1L))));
        주문_상태_변경요청함(savedOrder.getId(), 식사완료로_변경요청());

        // when
        ExtractableResponse<Response> response = 주문_상태_변경요청함(savedOrder.getId(), 식사중으로_변경요청());

        // then
        상태변경_실패함(response);
    }

    private void 상태변경_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 상태변경됨(ExtractableResponse<Response> response, OrderStatus expectedStatus) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getObject("", OrderResponse.class).getOrderStatus()).isEqualTo(expectedStatus);
    }

    public static ExtractableResponse<Response> 주문_상태_변경요청함(Long id, OrderStatusUpdateRequest request) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .put("/api/orders/{id}/order-status", id)
                .then().log().all()
                .extract();
    }

    public static OrderResponse 주문등록되어있음(OrderSaveRequest request) {
        ExtractableResponse<Response> response = 주문_등록_요청함(request);
        return response.jsonPath().getObject("", OrderResponse.class);
    }


    public static ExtractableResponse<Response> 주문_등록_요청함(OrderSaveRequest 주문등록요청) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(주문등록요청)
                .when()
                .post("/api/orders")
                .then().log().all()
                .extract();
    }

    private void 주문_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotNull();
    }

    private void 주문_등록_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
