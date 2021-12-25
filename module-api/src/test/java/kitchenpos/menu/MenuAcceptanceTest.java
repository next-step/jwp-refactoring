package kitchenpos.menu;

import static kitchenpos.menu.MenuGroupAcceptanceTest.메뉴그룹_등록_되어있음;
import static kitchenpos.product.ProductAcceptanceTest.상품_등록_되어있음;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("메뉴 관리 인수 테스트")
public class MenuAcceptanceTest extends AcceptanceTest {

    private static final String URL = "/api/menus";

    @Test
    @DisplayName("메뉴를 관리한다.")
    void manageMenu() {
        // 상품 등록 되어 있음
        ProductResponse 후라이드치킨 = 상품_등록_되어있음("후라이드치킨", 10000);
        ProductResponse 양념치킨 = 상품_등록_되어있음("양념치킨", 11000);

        // 메뉴그룹 등록 되어 있음
        MenuGroupResponse 치킨 = 메뉴그룹_등록_되어있음("치킨");

        // 메뉴 등록 요청
        List<MenuProductRequest> menuProducts = createMenuProducts(Arrays.asList(후라이드치킨, 양념치킨),
            Arrays.asList(1L, 1L));
        ExtractableResponse<Response> saveResponse = 메뉴_등록_요청("두마리세트", 20000, 치킨, menuProducts);
        // 메뉴 등록됨
        메뉴_등록_됨(saveResponse);

        // 메뉴 목록 조회 요청
        ExtractableResponse<Response> response = 메뉴_목록_조회_요청();
        // 메뉴 목록 조회됨
        메뉴_목록_조회됨(response, Arrays.asList(saveResponse.as(MenuResponse.class)));

    }

    public static ExtractableResponse<Response> 메뉴_등록_요청(String name, Integer price,
        MenuGroupResponse menuGroup, List<MenuProductRequest> menuProducts) {
        MenuRequest menuRequest = new MenuRequest(name, price, menuGroup.getId(), menuProducts);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(menuRequest)
            .when().post(URL)
            .then().log().all()
            .extract();
    }

    public static void 메뉴_등록_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 메뉴_목록_조회_요청() {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get(URL)
            .then().log().all()
            .extract();
    }

    public static void 메뉴_목록_조회됨(ExtractableResponse<Response> response,
        List<MenuResponse> expected) {
        List<MenuResponse> list = response.jsonPath().getList(".", MenuResponse.class);
        List<String> expectedNames = expected.stream()
            .map(MenuResponse::getName)
            .collect(Collectors.toList());

        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(list).extracting(MenuResponse::getName).containsAll(expectedNames);
        });
    }

    public static MenuResponse 메뉴등록되어있음(String name, Integer price, MenuGroupResponse menuGroup,
        List<ProductResponse> products) {
        List<MenuProductRequest> menuProducts = createMenuProducts(products, Arrays.asList(1L, 1L));
        return 메뉴_등록_요청(name, price, menuGroup, menuProducts).as(MenuResponse.class);
    }

    private static List<MenuProductRequest> createMenuProducts(List<ProductResponse> products,
        List<Long> productsQuantity) {
        return IntStream.range(0, products.size())
            .mapToObj(i -> new MenuProductRequest(products.get(i).getId(), productsQuantity.get(i)))
            .collect(Collectors.toList());
    }
}
