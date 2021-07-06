package kitchenpos.order.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

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
import kitchenpos.menugroup.dto.MenuGroupListResponse;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderListResponse;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.product.dto.ProductRequest;

@DisplayName("주문 인수 테스트")
public class OrderAcceptanceTest extends AcceptanceTest {

    private static final String 메뉴_그룹_이름_국밥 = "국밥";
    private static final String 상품_이름_순대 = "순대";
    private static final int 상품_가격 = 8000;
    public static final String 메뉴_이름_순대국 = "순대국";
    public static final int 메뉴_가격 = 8000;
    public static final Long 상품_수량 = 1L;
    public static final int 손님_수 = 1;
    public static final boolean 비어있음_여부 = true;

    private Long 메뉴_그룹_번호;
    private Long 상품_번호;
    private Long 주문_테이블_번호;
    private Long 메뉴_번호;

    @BeforeEach
    void setup() {
        ExtractableResponse<Response> 메뉴_그룹_생성_요청_응답 = 메뉴_그룹_생성_요청(메뉴_그룹_이름_국밥);
        메뉴_그룹_번호 = 메뉴_그룹_번호_추출(메뉴_그룹_생성_요청_응답);

        ExtractableResponse<Response> 상품_생성_요청_응답 = 상품_생성_요청(상품_이름_순대, 상품_가격);
        상품_번호 = 공통_번호_추출(상품_생성_요청_응답);

        ExtractableResponse<Response> 주문_테이블_생성_요청_응답 = 주문_테이블_생성_요청(손님_수, 비어있음_여부);
        주문_테이블_번호 = 공통_번호_추출(주문_테이블_생성_요청_응답);

        ExtractableResponse<Response> 메뉴_생성_요청 = 메뉴_생성_요청(메뉴_이름_순대국, 메뉴_가격, 메뉴_그룹_번호, 상품_번호, 상품_수량);
        메뉴_번호 = 공통_번호_추출(메뉴_생성_요청);
    }

    @DisplayName("사용자는 주문을 생성 할 수 있다.")
    @Test
    void create() {
        // given
        // when
        ExtractableResponse<Response> 주문_생성_요청_응답 = 주문_셍성_요청(주문_테이블_번호, 메뉴_번호, 1);
        // then
        주문_생성_요청_응답_확인(주문_생성_요청_응답);
    }

    @DisplayName("사용자는 주문 전체를 조회 할 수 있다.")
    @Test
    void list() {
        // given
        주문_셍성_요청(주문_테이블_번호, 메뉴_번호, 1);
        // when
        ExtractableResponse<Response> 주문_조회_요청_응답 = 주문_조회_요청();
        // then
        주문_조회_요청_응답_확인(주문_조회_요청_응답);
    }

    private void 메뉴_그룹_조회_요청_응답_확인(ExtractableResponse<Response> 메뉴_그룹_조회_요청_응답) {
        MenuGroupListResponse menuGroupListResponse = 메뉴_그룹_조회_요청_응답.as(MenuGroupListResponse.class);
        assertThat(menuGroupListResponse.getMenuGroupResponses().get(0).getName()).isEqualTo("국밥");
    }

    private ExtractableResponse<Response> 메뉴_그룹_조회_요청() {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/api/menu-groups/")
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

    private void 메뉴_그룹_생성_요청_응답_확인(ExtractableResponse<Response> 메뉴_그룹_생성_요청_응답) {
        assertThat(메뉴_그룹_생성_요청_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
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

    private ExtractableResponse<Response> 주문_테이블_생성_요청(int 손님_수, boolean 비어있음_여부) {
        return RestAssured.given().log().all()
                .body(new OrderTable(손님_수, 비어있음_여부))
                .contentType(ContentType.JSON)
                .when().post("/api/tables")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 주문_셍성_요청(Long 주문_테이블_번호, Long 메뉴_번호, int 메뉴_수량) {
        OrderRequest orderRequest = new OrderRequest(주문_테이블_번호, Arrays.asList(new OrderLineItemRequest(메뉴_번호, 메뉴_수량)));
        return RestAssured.given().log().all()
                .body(orderRequest)
                .contentType(ContentType.JSON)
                .when().post("/api/orders")
                .then().log().all()
                .extract();
    }

    private void 주문_생성_요청_응답_확인(ExtractableResponse<Response> 주문_생성_요청_응답) {
        OrderResponse orderResponse = 주문_생성_요청_응답.as(OrderResponse.class);
        assertThat(주문_생성_요청_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }

    private ExtractableResponse<Response> 주문_조회_요청() {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/api/orders")
                .then().log().all()
                .extract();
    }

    private void 주문_조회_요청_응답_확인(ExtractableResponse<Response> 주문_조회_요청_응답) {
        OrderListResponse orderListResponse = 주문_조회_요청_응답.as(OrderListResponse.class);
        assertThat(orderListResponse.getOrderResponses().get(0).getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }
}
