package kitchenpos.menu.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.menugroup.acceptance.MenuGroupAcceptanceTest.*;
import static kitchenpos.product.acceptance.ProductAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 관련 기능")
public class MenuAcceptanceTest extends AcceptanceTest {

    @DisplayName("메뉴를 생성한다.")
    @Test
    public void create() {
        // when
        ExtractableResponse<Response> 등록된_메뉴 = 메뉴_등록되어_있음(테스트_메뉴_생성());

        // then
        메뉴_생성_검증됨(등록된_메뉴);
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void list() {
        // given
        ExtractableResponse<Response> 등록된_메뉴 = 메뉴_등록되어_있음(테스트_메뉴_생성());

        // when
        ExtractableResponse<Response> 메뉴_목록 = 메뉴_목록_조회_요청();

        // then
        메뉴_목록_검증됨(메뉴_목록);
        메뉴_목록_포함됨(메뉴_목록, Arrays.asList(등록된_메뉴));
    }

    private static Menu 테스트_메뉴_생성() {
        MenuGroup menuGroup = 메뉴_그룹_가져옴(메뉴_그룹_등록되어_있음(MENU_GROUP_NAME01));
        Product product = 상품_가져옴(상품_등록되어_있음(PRODUCT_NAME01, PRODUCT_PRICE01));
        MenuProduct menuProduct = new MenuProduct(product.getId(), 1);
        return new Menu("바베큐치킨", new BigDecimal(30000), menuGroup.getId(), Collections.singletonList(menuProduct));
    }

    public static ExtractableResponse<Response> 메뉴_등록되어_있음(Menu menu) {
        return 메뉴_생성_요청(menu);
    }

    public static ExtractableResponse<Response> 메뉴_생성_요청(Menu menu) {
        return RestAssured
                .given().log().all()
                .body(menu)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/menus")
                .then().log().all()
                .extract();
    }

    public static void 메뉴_생성_검증됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static ExtractableResponse<Response> 메뉴_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/menus")
                .then().log().all()
                .extract();
    }

    public static void 메뉴_목록_검증됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 메뉴_목록_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        List<Long> resultLineIds = response.jsonPath().getList(".", Menu.class).stream()
                .map(Menu::getId)
                .collect(Collectors.toList());

        List<Long> expectedLineIds = createdResponses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[3]))
                .collect(Collectors.toList());

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }
}
