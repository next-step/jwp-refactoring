package kitchenpos.menu.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.menu.acceptance.MenuGroupAcceptanceTest.*;
import static kitchenpos.product.acceptance.ProductAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 관련 기능")
public class MenuAcceptanceTest extends AcceptanceTest {
    public static final String MENU_NAME01 = "치킨세트";
    public static final BigDecimal MENU_PRICE01 = new BigDecimal(30000);

    @DisplayName("메뉴를 생성한다.")
    @Test
    public void create() {
        // when
        ExtractableResponse<Response> 등록된_메뉴 = 메뉴_등록되어_있음(테스트_메뉴_생성(MENU_NAME01, MENU_PRICE01));

        // then
        메뉴_생성_검증됨(등록된_메뉴);
    }

    @DisplayName("[예외] 가격 없이 메뉴를 생성한다.")
    @Test
    public void create_price_null() {
        // when
        ExtractableResponse<Response> 등록된_메뉴 = 메뉴_등록되어_있음(테스트_메뉴_생성(MENU_NAME01, null));

        // then
        메뉴_생성_실패함(등록된_메뉴);
    }

    @DisplayName("[예외] 0원 미만으로 메뉴를 생성한다.")
    @Test
    public void create_price_under_zero() {
        // when
        ExtractableResponse<Response> 등록된_메뉴 = 메뉴_등록되어_있음(테스트_메뉴_생성(MENU_NAME01, new BigDecimal(-1)));

        // then
        메뉴_생성_실패함(등록된_메뉴);
    }

    @DisplayName("[예외] 메뉴 그룹을 포함하지 않은 메뉴를 생성한다.")
    @Test
    public void create_without_menu_group() {
        // when
        ExtractableResponse<Response> 등록된_메뉴 = 메뉴_등록되어_있음(메뉴_그룹_없는_테스트_메뉴_생성(MENU_NAME01, MENU_PRICE01));

        // then
        메뉴_생성_실패함(등록된_메뉴);
    }

    @DisplayName("[예외] 상품을 포함하지 않은 메뉴를 생성한다")
    @Test
    public void create_without_product() {
        // when
        ExtractableResponse<Response> 등록된_메뉴 = 메뉴_등록되어_있음(상품_없는_테스트_메뉴_생성(MENU_NAME01, MENU_PRICE01));

        // then
        메뉴_생성_실패함(등록된_메뉴);
    }

    @DisplayName("[예외] 메뉴 상품보다 가격이 비싼 메뉴를 생성한다.")
    @Test
    public void create_expensive_than_menu_products() {
        // when
        ExtractableResponse<Response> 등록된_메뉴 = 메뉴_등록되어_있음(테스트_메뉴_생성(MENU_NAME01, new BigDecimal(100000)));

        // then
        메뉴_생성_실패함(등록된_메뉴);
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void list() {
        // given
        ExtractableResponse<Response> 등록된_메뉴 = 메뉴_등록되어_있음(테스트_메뉴_생성(MENU_NAME01, MENU_PRICE01));

        // when
        ExtractableResponse<Response> 메뉴_목록 = 메뉴_목록_조회_요청();

        // then
        메뉴_목록_검증됨(메뉴_목록);
        메뉴_목록_포함됨(메뉴_목록, Arrays.asList(등록된_메뉴));
    }

    public static MenuRequest 테스트_메뉴_생성(String menuName, BigDecimal menuPrice) {
        MenuGroup menuGroup = 메뉴_그룹_가져옴(메뉴_그룹_등록되어_있음(MENU_GROUP_NAME01)).toMenuGroup();
        ProductResponse productResponse = 상품_가져옴(상품_등록되어_있음(PRODUCT_NAME01, PRODUCT_PRICE01));
        MenuProductRequest request = new MenuProductRequest(productResponse.getId(), 1);
        return new MenuRequest(menuName, menuPrice, menuGroup.getId(), Collections.singletonList(request));
    }

    public static MenuRequest 메뉴_그룹_없는_테스트_메뉴_생성(String menuName, BigDecimal menuPrice) {
        ProductResponse productResponse = 상품_가져옴(상품_등록되어_있음(PRODUCT_NAME01, PRODUCT_PRICE01));
        MenuProductRequest request = new MenuProductRequest(productResponse.getId(), 1);
        return new MenuRequest(menuName, menuPrice, null, Collections.singletonList(request));
    }

    public static MenuRequest 상품_없는_테스트_메뉴_생성(String menuName, BigDecimal menuPrice) {
        MenuGroup menuGroup = 메뉴_그룹_가져옴(메뉴_그룹_등록되어_있음(MENU_GROUP_NAME01)).toMenuGroup();
        MenuProductRequest request = new MenuProductRequest(null, 1);
        return new MenuRequest(menuName, menuPrice, menuGroup.getId(), Collections.singletonList(request));
    }

    public static ExtractableResponse<Response> 메뉴_등록되어_있음(MenuRequest request) {
        return 메뉴_생성_요청(request);
    }

    public static ExtractableResponse<Response> 메뉴_생성_요청(MenuRequest request) {
        return RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/menus")
                .then().log().all()
                .extract();
    }

    public static void 메뉴_생성_검증됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 메뉴_생성_실패함(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
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
        List<Long> resultLineIds = response.jsonPath().getList(".", MenuResponse.class).stream()
                .map(MenuResponse::getId)
                .collect(Collectors.toList());

        List<Long> expectedLineIds = createdResponses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[3]))
                .collect(Collectors.toList());

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    public static MenuResponse 메뉴_가져옴(ExtractableResponse<Response> response) {
        return response.as(MenuResponse.class);
    }
}
