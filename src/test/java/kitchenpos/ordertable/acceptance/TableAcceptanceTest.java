package kitchenpos.ordertable.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.ordertable.dto.OrderTableEmptyRequest;
import kitchenpos.ordertable.dto.OrderTableNumberOfGuestsRequest;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.product.dto.ProductRequest;

@DisplayName("주문 테이블 인수 테스트")
public class TableAcceptanceTest extends AcceptanceTest {

    private static final String 메뉴_그룹_이름_국밥 = "국밥";
    private static final String 상품_이름_순대 = "순대";
    private static final int 상품_가격 = 8000;
    public static final String 메뉴_이름_순대국 = "순대국";
    public static final int 메뉴_가격 = 8000;
    public static final Long 상품_수량 = 1L;
    public static final int 고객_수_2명 = 2;
    public static final int 고객_수_5명 = 5;
    public static final boolean 비어있음 = true;
    public static final boolean 비어있지_않음 = false;

    private Long 메뉴_그룹_번호;
    private Long 상품_번호;
    private Long 주문_테이블_번호;
    private Long 메뉴_번호;
    private Long 주문_번호;

    @BeforeEach
    void setup() {
        ExtractableResponse<Response> 메뉴_그룹_생성_요청_응답 = 메뉴_그룹_생성_요청(메뉴_그룹_이름_국밥);
        메뉴_그룹_번호 = 메뉴_그룹_번호_추출(메뉴_그룹_생성_요청_응답);

        ExtractableResponse<Response> 상품_생성_요청_응답 = 상품_생성_요청(상품_이름_순대, 상품_가격);
        상품_번호 = 공통_번호_추출(상품_생성_요청_응답);

        ExtractableResponse<Response> 메뉴_생성_요청 = 메뉴_생성_요청(메뉴_이름_순대국, 메뉴_가격, 메뉴_그룹_번호, 상품_번호, 상품_수량);
        메뉴_번호 = 공통_번호_추출(메뉴_생성_요청);

        ExtractableResponse<Response> 주문_테이블_생성_요청_응답 = 주문_테이블_생성_요청(고객_수_2명, 비어있지_않음);
        주문_테이블_번호 = 공통_번호_추출(주문_테이블_생성_요청_응답);

        ExtractableResponse<Response> 주문_생성_요청_응답 = 주문_생성_요청(주문_테이블_번호, 메뉴_번호, 1);
        주문_번호 = 공통_번호_추출(주문_생성_요청_응답);
    }

    @DisplayName("사용자는 주문 테이블을 생성 할 수 있다.")
    @Test
    void create() {
        // given
        // when
        ExtractableResponse<Response> 주문_테이블_생성_요청_응답 = 주문_테이블_생성_요청(고객_수_2명, 비어있지_않음);
        // then
        주문_테이블_생성_요청_응답_확인(주문_테이블_생성_요청_응답);
    }

    @DisplayName("사용자는 주문 테이블 전체를 조회 할 수 있다.")
    @Test
    void list() {
        // given
        주문_테이블_생성_요청(고객_수_2명, 비어있지_않음);
        // when
        ExtractableResponse<Response> 주문_테이블_조회_요청_응답 = 주문_테이블_조회_요청();
        // then
        주문_테이블_조회_요청_응답_확인(주문_테이블_조회_요청_응답);
    }

    @DisplayName("사용자는 테이블 비어 있음을 변경 할 수 있다.")
    @Test
    void changeOrderTableEmpty() {
        // given
        주문_상태_변경_요청(주문_번호, OrderStatus.COMPLETION);
        // when
        ExtractableResponse<Response> 주문_테이블_비어있음_요청_응답 = 주문_테이블_비어있음_요청(주문_테이블_번호, 비어있음);
        // then
        주문_테이블_비어있음_요청_응답_확인(주문_테이블_비어있음_요청_응답);
    }

    @DisplayName("테이블 비어 있음 변경 실패 - 주문의 상태가 COOKING")
    @Test
    void changeOrderTableEmptyFailedByOrderStatus() {
        // given
        // when
        ExtractableResponse<Response> 주문_테이블_비어있음_요청_응답_실패 = 주문_테이블_비어있음_요청(주문_테이블_번호, 비어있음);
        // then
        주문_테이블_비어있음_요청_응답_실패_확인(주문_테이블_비어있음_요청_응답_실패);
    }

    @DisplayName("사용자는 고객수를 변경 할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        // given
        // when
        ExtractableResponse<Response> 주문_테이블_고객수_변경_요청_응답 = 주문_테이블_고객수_변경_요청(주문_테이블_번호, 고객_수_5명);
        // then
        주문_테이블_고객수_변경_요청_응답_확인(주문_테이블_고객수_변경_요청_응답);
    }

    @DisplayName("고객 수 변경 실패 - 고객 수가 음수")
    @Test
    void changeNumberOfGuestsFailedByNotFoundOrderTable() {
        // given
        // when
        ExtractableResponse<Response> 주문_테이블_고객수_변경_실패 = 주문_테이블_고객수_변경_요청(주문_테이블_번호, -5);
        // then
        주문_테이블_고객수_변경_실패_확인(주문_테이블_고객수_변경_실패);
    }

    @DisplayName("고객 수 변경 실패 - 주문 테이블이 비어 있음")
    @Test
    void changeNumberOfGuestsFailedByOrderTableEmpty() {
        // given
        주문_상태_변경_요청(주문_번호, OrderStatus.COMPLETION);
        주문_테이블_비어있음_요청(주문_테이블_번호, 비어있음);
        // when
        ExtractableResponse<Response> 주문_테이블_고객수_변경_실패 = 주문_테이블_고객수_변경_요청(주문_테이블_번호, 고객_수_5명);
        // then
        주문_테이블_고객수_변경_실패_확인(주문_테이블_고객수_변경_실패);
    }

    private ExtractableResponse<Response> 주문_상태_변경_요청(Long 주문_번호, OrderStatus orderStatus) {
        OrderStatusRequest orderStatusRequest = new OrderStatusRequest(orderStatus.name());
        return RestAssured.given().log().all()
                .body(orderStatusRequest)
                .contentType(ContentType.JSON)
                .when().put("/api/orders/" + 주문_번호 + "/order-status")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 메뉴_그룹_생성_요청(String 요청_메뉴_그룹) {
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest(요청_메뉴_그룹);
        return RestAssured.given().log().all()
                .body(menuGroupRequest)
                .contentType(ContentType.JSON)
                .when().post("/api/menu-groups/")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 상품_생성_요청(String 상품_이름, int 상품_가격) {
        ProductRequest productRequest = new ProductRequest(상품_이름, 상품_가격);
        return RestAssured.given().log().all()
                .body(productRequest)
                .contentType(ContentType.JSON)
                .when().post("/api/products")
                .then().log().all()
                .extract();
    }

    private Long 공통_번호_추출(ExtractableResponse<Response> 상품_생성_요청_응답) {
        String[] locationInfo = 상품_생성_요청_응답.header("Location").split("/");
        return Long.parseLong(locationInfo[3]);
    }

    private Long 메뉴_그룹_번호_추출(ExtractableResponse<Response> 메뉴_그룹_생성_요청_응답) {
        MenuGroupResponse 응답 = 메뉴_그룹_생성_요청_응답.as(MenuGroupResponse.class);
        return 응답.getId();
    }

    private ExtractableResponse<Response> 메뉴_생성_요청(String 메뉴_이름, int 가격, Long 메뉴_그룹_번호, Long 상품_번호, Long 상품_수량) {
        MenuProductRequest 메뉴_상품_요청 = new MenuProductRequest(상품_번호, 상품_수량);
        MenuRequest menuRequest = new MenuRequest(메뉴_이름, 가격, 메뉴_그룹_번호, Arrays.asList(메뉴_상품_요청));
        return RestAssured.given().log().all()
                .body(menuRequest)
                .contentType(ContentType.JSON)
                .when().post("/api/menus/")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 주문_테이블_생성_요청(int 고객_수, boolean 비어있음_여부) {
        OrderTableRequest orderTableRequest = new OrderTableRequest(고객_수, 비어있음_여부);
        return RestAssured.given().log().all()
                .body(orderTableRequest)
                .contentType(ContentType.JSON)
                .when().post("/api/tables")
                .then().log().all()
                .extract();
    }

    private void 주문_테이블_생성_요청_응답_확인(ExtractableResponse<Response> 주문_생성_요청_응답) {
        assertThat(주문_생성_요청_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 주문_테이블_조회_요청() {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/api/tables")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 주문_테이블_비어있음_요청(Long 주문_테이블_번호, boolean 비어있음_여부) {
        OrderTableEmptyRequest orderTableEmptyRequest = new OrderTableEmptyRequest(비어있음_여부);
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(orderTableEmptyRequest)
                .when().put("/api/tables/" + 주문_테이블_번호 + "/empty")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 주문_테이블_고객수_변경_요청(Long 주문_테이블_번호, int 고객수5명) {
        OrderTableNumberOfGuestsRequest orderTableNumberOfGuestsRequest = new OrderTableNumberOfGuestsRequest(고객수5명);
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(orderTableNumberOfGuestsRequest)
                .when().put("/api/tables/" + 주문_테이블_번호 + "/number-of-guests")
                .then().log().all()
                .extract();
    }

    private void 주문_테이블_조회_요청_응답_확인(ExtractableResponse<Response> 주문_테이블_조회_요청_응답) {
        List<OrderTableResponse> orderTableResponses = 주문_테이블_조회_요청_응답.jsonPath().getList("", OrderTableResponse.class);
        assertThat(orderTableResponses.get(0).isEmpty()).isFalse();
    }

    private void 주문_상태_변경_확인(ExtractableResponse<Response> 주문_상태_변경_요청_응답, OrderStatus orderStatus) {
        assertThat(주문_상태_변경_요청_응답.body().jsonPath().get("orderStatus").toString()).isEqualTo(orderStatus.name());
    }

    private void 주문_테이블_비어있음_요청_응답_실패_확인(ExtractableResponse<Response> 주문_테이블_비어있음_요청_응답_실패) {
        assertThat(주문_테이블_비어있음_요청_응답_실패.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 주문_테이블_고객수_변경_실패_확인(ExtractableResponse<Response> 주문_테이블_고객수_변경_실패) {
        assertThat(주문_테이블_고객수_변경_실패.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 주문_테이블_비어있음_요청_응답_확인(ExtractableResponse<Response> 주문_테이블_비어있음_요청_응답) {
        OrderTableResponse orderTableResponse = 주문_테이블_비어있음_요청_응답.as(OrderTableResponse.class);
        assertThat(orderTableResponse.isEmpty()).isTrue();
    }

    private ExtractableResponse<Response> 주문_생성_요청(Long 주문_테이블_번호, Long 메뉴_번호, int 메뉴_수량) {
        OrderRequest orderRequest = new OrderRequest(주문_테이블_번호, Arrays.asList(new OrderLineItemRequest(메뉴_번호, 메뉴_수량)));
        return RestAssured.given().log().all()
                .body(orderRequest)
                .contentType(ContentType.JSON)
                .when().post("/api/orders")
                .then().log().all()
                .extract();
    }

    private void 주문_테이블_고객수_변경_요청_응답_확인(ExtractableResponse<Response> 주문_테이블_고객수_변경_요청_응답) {
        OrderTableResponse orderTableResponse = 주문_테이블_고객수_변경_요청_응답.as(OrderTableResponse.class);
        assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(고객_수_5명);
    }


}
