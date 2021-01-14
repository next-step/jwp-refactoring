package kitchenpos.menu;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menugroup.MenuGroupAcceptanceTest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.product.ProductAcceptanceTest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 관련 기능")
public class MenuAcceptanceTest extends AcceptanceTest {

    private MenuGroupResponse 추천메뉴;

    private ProductResponse 고추치킨;

    private ProductResponse 마늘치킨;

    @BeforeEach
    public void setUp() {
        super.setUp();

        추천메뉴 = MenuGroupAcceptanceTest.메뉴_그룹_등록되어_있음("추천메뉴").as(MenuGroupResponse.class);
        고추치킨 = ProductAcceptanceTest.상품_등록되어_있음("고추치킨", "10000").as(ProductResponse.class);
        마늘치킨 = ProductAcceptanceTest.상품_등록되어_있음("마늘치킨", "10000").as(ProductResponse.class);
    }

    @DisplayName("메뉴를 관리한다.")
    @Test
    void manage() {
        // given
        Map<String, Object> 고추치킨요청 = new HashMap<>();
        고추치킨요청.put("productId", 고추치킨.getId());
        고추치킨요청.put("quantity", 1);

        Map<String, Object> 마늘치킨요청 = new HashMap<>();
        마늘치킨요청.put("productId", 마늘치킨.getId());
        마늘치킨요청.put("quantity", 1);

        Map<String, Object> wrongPrice = new HashMap<>();
        wrongPrice.put("name", "고추마늘세트");
        wrongPrice.put("price", 25000);
        wrongPrice.put("menuGroupId", 추천메뉴.getId());
        wrongPrice.put("menuProducts", Arrays.asList(고추치킨요청, 마늘치킨요청));

        Map<String, Object> params = new HashMap<>();
        params.put("name", "고추마늘세트");
        params.put("price", 20000);
        params.put("menuGroupId", 추천메뉴.getId());
        params.put("menuProducts", Arrays.asList(고추치킨요청, 마늘치킨요청));

        // when
        ExtractableResponse<Response> wrongPriceResponse = 메뉴_등록_요청(wrongPrice);

        // then
        메뉴_생성_실패됨(wrongPriceResponse);

        // when
        ExtractableResponse<Response> createResponse = 메뉴_등록_요청(params);

        // then
        메뉴_생성됨(createResponse);

        // when
        ExtractableResponse<Response> findResponse = 메뉴_목록_조회_요청();

        // then
        메뉴_목록_응답됨(findResponse);
        메뉴_목록_포함됨(findResponse, Arrays.asList(createResponse));
    }

    public static ExtractableResponse<Response> 메뉴_등록_되어있음(final MenuGroupResponse menuGroup,
                                                           final List<ProductResponse> products) {
        List<Map<String, Object>> menuProducts = products.stream()
                .map(product -> {
                    Map<String, Object> request = new HashMap<>();
                    request.put("productId", product.getId());
                    request.put("quantity", 1);
                    return request;
                })
                .collect(Collectors.toList());

        BigDecimal sum = products.stream()
                .map(ProductResponse::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> params = new HashMap<>();
        params.put("name", "신메");
        params.put("price", sum.intValue());
        params.put("menuGroupId", menuGroup.getId());
        params.put("menuProducts", menuProducts);

        ExtractableResponse<Response> response = 메뉴_등록_요청(params);
        메뉴_생성됨(response);
        return response;
    }

    public static ExtractableResponse<Response> 메뉴_등록_요청(final Map<String, Object> params) {
        return RestAssured
                .given().log().all().body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/menus")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/menus")
                .then().log().all()
                .extract();
    }

    public static void 메뉴_생성됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 메뉴_생성_실패됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 메뉴_목록_응답됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 메뉴_목록_포함됨(final ExtractableResponse<Response> response,
                                 final List<ExtractableResponse<Response>> createResponses) {
        List<Long> expectedIds = createResponses.stream()
                .map(create -> Long.parseLong(create.header("Location").split("/")[3]))
                .collect(Collectors.toList());

        List<Long> actualIds = response.jsonPath().getList("id", Long.class);
        assertThat(actualIds).containsAll(expectedIds);
    }
}
