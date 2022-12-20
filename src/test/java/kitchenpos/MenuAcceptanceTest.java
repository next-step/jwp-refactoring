package kitchenpos;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static kitchenpos.MenuGroupAcceptanceTest.메뉴_그룹_생성_요청;
import static kitchenpos.ProductAcceptanceTest.상품_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("메뉴 관련 기능")
public class MenuAcceptanceTest extends AcceptanceTest {

    private MenuGroup 신메뉴;
    private Product 파닭치킨;
    private Product 뿌링클치킨;

    @DisplayName("메뉴 관련 기능 테스트")
    @TestFactory
    Stream<DynamicNode> menu() {
        return Stream.of(
            dynamicTest("메뉴을 등록한다.", () -> {
                신메뉴 = 메뉴_그룹_생성_요청("신메뉴").as(MenuGroup.class);
                파닭치킨 = 상품_생성_요청("파닭치킨", BigDecimal.valueOf(15_000L)).as(Product.class);
                뿌링클치킨 = 상품_생성_요청("뿌링클치킨", BigDecimal.valueOf(15_000L)).as(Product.class);

                ExtractableResponse<Response> response = 메뉴_생성_요청("파닭치킨, 뿌링클치킨",
                    BigDecimal.valueOf(15_000L),
                    신메뉴.getId(), 파닭치킨, 뿌링클치킨);

                메뉴_생성됨(response);
            }),
            dynamicTest("가격이 0미만의 메뉴을 등록한다.", () -> {
                ExtractableResponse<Response> response = 메뉴_생성_요청("파닭치킨", BigDecimal.valueOf(-1),
                    신메뉴.getId(), 파닭치킨);

                메뉴_생성_실패됨(response);
            }),
            dynamicTest("이름이 없는 메뉴을 등록한다.", () -> {
                ExtractableResponse<Response> response = 메뉴_생성_요청(null, BigDecimal.valueOf(15_000L),
                    신메뉴.getId(), 파닭치킨);

                메뉴_생성_실패됨(response);
            }),
            dynamicTest("메뉴 그룹 없이 메뉴을 등록한다.", () -> {
                ExtractableResponse<Response> response = 메뉴_생성_요청("파닭치킨", BigDecimal.valueOf(15_000L),
                    null, 파닭치킨);

                메뉴_생성_실패됨(response);
            }),
            dynamicTest("상품 없이 메뉴을 등록한다.", () -> {
                ExtractableResponse<Response> response = 메뉴_생성_요청("파닭치킨", BigDecimal.valueOf(15_000L),
                    신메뉴.getId());

                메뉴_생성_실패됨(response);
            }),
            dynamicTest("존재하지 않는 상품이 포함된 메뉴을 등록한다.", () -> {
                Product 존재하지_않는_상품 = new Product(Long.MAX_VALUE, "noExistProduct", BigDecimal.valueOf(1L));

                ExtractableResponse<Response> response = 메뉴_생성_요청("파닭치킨", BigDecimal.valueOf(15_000L),
                    신메뉴.getId(), 존재하지_않는_상품);

                메뉴_생성_실패됨(response);
            }),
            dynamicTest("상품 가격보다 비싼 메뉴을 등록한다.", () -> {
                ExtractableResponse<Response> response = 메뉴_생성_요청("고급치킨", BigDecimal.valueOf(18_000L),
                    신메뉴.getId(), 파닭치킨);

                메뉴_생성_실패됨(response);
            }),

            dynamicTest("메뉴 목록을 조회한다.", () -> {
                ExtractableResponse<Response> response = 메뉴_목록_조회_요청();

                메뉴_목록_응답됨(response);
                메뉴_목록_확인됨(response, "파닭치킨, 뿌링클치킨");
                메뉴_목록_메뉴에_메뉴_상품이_포함됨(response, 파닭치킨, 뿌링클치킨);
            })
        );
    }

    public static ExtractableResponse<Response> 메뉴_생성_요청(String name, BigDecimal price, Long menuGroupId,
        Product... products) {
        Map<String, Object> request = new HashMap<>();
        request.put("name", name);
        request.put("price", price);
        request.put("menuGroupId", menuGroupId);
        request.put("menuProducts", toMenuProducts(products));
        return RestAssured
            .given().log().all()
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/api/menus")
            .then().log().all()
            .extract();
    }

    private static List<MenuProductRequest> toMenuProducts(Product... products) {
        return Arrays.stream(products)
            .map(p -> new MenuProductRequest(p.getId(), 1))
            .collect(Collectors.toList());
    }

    public static ExtractableResponse<Response> 메뉴_목록_조회_요청() {
        return RestAssured
            .given().log().all()
            .when().get("/api/menus")
            .then().log().all()
            .extract();
    }

    public static void 메뉴_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 메뉴_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 메뉴_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 메뉴_목록_확인됨(ExtractableResponse<Response> response, String... names) {
        List<MenuResponse> menuResponses = response.jsonPath().getList(".", MenuResponse.class);

        List<String> productNames = menuResponses.stream()
            .map(MenuResponse::getName)
            .collect(Collectors.toList());
        assertThat(productNames).containsExactly(names);
    }

    public static void 메뉴_목록_메뉴에_메뉴_상품이_포함됨(ExtractableResponse<Response> response, Product... products) {
        List<MenuResponse> menuResponses = response.jsonPath().getList(".", MenuResponse.class);

        List<Long> actualProductIds = menuResponses.stream()
            .flatMap(menu -> menu.getMenuProducts().stream())
            .map(x -> x.getProductId())
            .collect(Collectors.toList());

        List<Long> expectedProductIds = Arrays.stream(products)
            .map(Product::getId)
            .collect(Collectors.toList());

        assertThat(actualProductIds).containsExactlyElementsOf(expectedProductIds);
    }
}
