package kitchenpos.order;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.menu.MenuAcceptanceTest;
import kitchenpos.menugroup.MenuGroupAcceptanceTest;
import kitchenpos.ordertable.OrderTableAcceptanceTest;
import kitchenpos.product.ProductAcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 관련 기능")
public class OrderAcceptanceTest extends AcceptanceTest {

    private OrderTable orderTable;

    private Menu menu;

    @BeforeEach
    public void setUp() {
        super.setUp();

        orderTable = OrderTableAcceptanceTest.주문_테이블_등록_되어있음(3, false).as(OrderTable.class);

        MenuGroup 추천메뉴 = MenuGroupAcceptanceTest.메뉴_그룹_등록되어_있음("추천메뉴").as(MenuGroup.class);
        Product 고추치킨 = ProductAcceptanceTest.상품_등록되어_있음("고추치킨", "10000").as(Product.class);
        Product 마늘치킨 = ProductAcceptanceTest.상품_등록되어_있음("마늘치킨", "10000").as(Product.class);

        menu = MenuAcceptanceTest.메뉴_등록_되어있음(추천메뉴, Arrays.asList(고추치킨, 마늘치킨)).as(Menu.class);
    }

    @DisplayName("주문을 관리한다.")
    @Test
    void manage() {
        // given
        Map<String, Object> menuParams = new HashMap<>();
        menuParams.put("menuId", menu.getId());
        menuParams.put("quantity", 1);

        Map<String, Object> params = new HashMap<>();
        params.put("orderTableId", orderTable.getId());
        params.put("orderLineItems", Arrays.asList(menuParams));

        // when
        ExtractableResponse<Response> createResponse = 주문_생성_요청(params);

        // then
        주문_생성됨(createResponse);

        // when
        ExtractableResponse<Response> findResponse = 주문_목록_조회_요청();

        // then
        주문_응답됨(findResponse);
        주문_목록_포함됨(findResponse, Arrays.asList(createResponse));

        // when
        Map<String, String> updateParams = new HashMap<>();
        updateParams.put("orderStatus", "COMPLETION");
        ExtractableResponse<Response> updateResponse = 주문_상태_변경_요청(createResponse, updateParams);

        // then
        주문_응답됨(updateResponse);

        // when
        Map<String, String> wrongUpdateParams = new HashMap<>();
        updateParams.put("orderStatus", "MEAL");
        ExtractableResponse<Response> wrongResponse = 주문_상태_변경_요청(createResponse, wrongUpdateParams);

        // then
        주문_응답_실패됨(wrongResponse);
    }

    private ExtractableResponse<Response> 주문_생성_요청(final Map<String, Object> params) {
        return RestAssured
                .given().log().all().body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/orders")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/orders")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_상태_변경_요청(final ExtractableResponse<Response> createResponse,
                                                            final Map<String, String> params) {
        String location = createResponse.header("Location");
        return RestAssured
                .given().log().all().body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(location + "/order-status")
                .then().log().all()
                .extract();
    }

    private void 주문_생성됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 주문_응답됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 주문_응답_실패됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 주문_목록_포함됨(final ExtractableResponse<Response> response,
                                 final List<ExtractableResponse<Response>> createResponses) {
        List<Long> expectedIds = createResponses.stream()
                .map(create -> Long.parseLong(create.header("Location").split("/")[3]))
                .collect(Collectors.toList());

        List<Long> actualIds = response.jsonPath().getList("id", Long.class);
        assertThat(actualIds).containsAll(expectedIds);
    }
}
