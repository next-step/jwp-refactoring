package kitchenpos.menu.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menuGroup.dto.MenuGroupResponse;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.menuGroup.acceptance.MenuGroupAcceptanceTest.메뉴_그룹_생성되어_있음;
import static kitchenpos.product.acceptance.ProductAcceptanceTest.상품_생성되어_있음;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class MenuAcceptanceTest extends AcceptanceTest {
    private MenuGroupResponse 치킨_메뉴_그룹;
    private ProductResponse 후라이드치킨;
    private ProductResponse 양념치킨;
    private MenuRequest.ProductInfo 후라이트치킨_수량;
    private MenuRequest.ProductInfo 양념치킨_수량;
    private List<MenuRequest.ProductInfo> 상품_수량_정보;

    @BeforeEach
    void setup() {
        super.setUp();

        치킨_메뉴_그룹 = 메뉴_그룹_생성되어_있음("치킨세트").as(MenuGroupResponse.class);
        후라이드치킨 = 상품_생성되어_있음("후라이드치킨", 15_000).as(ProductResponse.class);
        양념치킨 = 상품_생성되어_있음("양념치킨", 16_000).as(ProductResponse.class);
        후라이트치킨_수량 = new MenuRequest.ProductInfo(후라이드치킨.getProductId(), 1);
        양념치킨_수량 = new MenuRequest.ProductInfo(양념치킨.getProductId(), 1);
        상품_수량_정보 = Arrays.asList(후라이트치킨_수량, 양념치킨_수량);
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    void createMenu() {
        // given
        MenuRequest menuRequest = new MenuRequest("치킨세트", BigDecimal.valueOf(30_000), 치킨_메뉴_그룹.getMenuGroupId(), 상품_수량_정보);

        // when
        ExtractableResponse<Response> response = 메뉴_생성_요청(menuRequest);

        // then
        메뉴_생성_요청_됨(response, menuRequest);
    }

    @DisplayName("메뉴의 가격은 0 이상이여야 한다.")
    @Test
    void createMenu1() {
        // given
        MenuRequest menuRequest = new MenuRequest("치킨세트", BigDecimal.valueOf(-30_000), 치킨_메뉴_그룹.getMenuGroupId(), 상품_수량_정보);

        // when
        ExtractableResponse<Response> response = 메뉴_생성_요청(menuRequest);

        // then
        메뉴_생성_요청_실패(response);
    }

    @ParameterizedTest(name = "상품 정보가 존재하지 않으면 안된다.")
    @EmptySource
    void createMenu2(List<MenuRequest.ProductInfo> 상품_수량_정보) {
        // given
        MenuRequest menuRequest = new MenuRequest("치킨세트", BigDecimal.valueOf(30_000), 치킨_메뉴_그룹.getMenuGroupId(), 상품_수량_정보);

        // when
        ExtractableResponse<Response> response = 메뉴_생성_요청(menuRequest);

        // then
        메뉴_생성_요청_실패(response);
    }

    @DisplayName("메뉴의 가격이 상품의 가격의 합보다 크면 안된다.")
    @Test
    void createMenu2() {
        // given
        MenuRequest menuRequest = new MenuRequest("치킨세트", BigDecimal.valueOf(32_000), 치킨_메뉴_그룹.getMenuGroupId(), 상품_수량_정보);

        // when
        ExtractableResponse<Response> response = 메뉴_생성_요청(menuRequest);

        // then
        메뉴_생성_요청_실패(response);
    }

    @DisplayName("메뉴 목록 조회")
    @Test
    void listMenu() {
        // given
        MenuRequest menuRequest = new MenuRequest("치킨세트", BigDecimal.valueOf(30_000), 치킨_메뉴_그룹.getMenuGroupId(), 상품_수량_정보);
        MenuResponse menuResponse = 메뉴_생성되어_있음(menuRequest).as(MenuResponse.class);
        List<MenuResponse> menuResponses = Collections.singletonList(menuResponse);

        // when
        ExtractableResponse<Response> response = 메뉴_목록_조회();

        // then
        메뉴_목록_조회_됨(response, menuResponses.size());

    }

    private static void 메뉴_생성_요청_됨(ExtractableResponse<Response> response, MenuRequest menuRequest) {
        MenuResponse menuResponse = response.as(MenuResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(menuResponse.getMenuId()).isNotNull(),
                () -> assertThat(menuResponse.getName()).isEqualTo(menuRequest.getName()),
                () -> assertThat(menuResponse.getPrice()).isEqualTo(menuRequest.getPrice()),
                () -> assertThat(menuResponse.getMenuProductInfos()).hasSize(menuRequest.getProductInfos().size())
        );
    }

    public static ExtractableResponse<Response> 메뉴_생성_요청(MenuRequest menuRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menuRequest)
                .when().post("/api/menus")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴_생성되어_있음(MenuRequest menuRequest) {
        return 메뉴_생성_요청(menuRequest);
    }

    private static void 메뉴_생성_요청_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private static ExtractableResponse<Response> 메뉴_목록_조회() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/menus")
                .then().log().all()
                .extract();
    }

    private static void 메뉴_목록_조회_됨(ExtractableResponse<Response> response, int size) {
        List<MenuResponse> menuResponses = response.body()
                .jsonPath()
                .getList(".", MenuResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(menuResponses).hasSize(size)
        );

    }
}
