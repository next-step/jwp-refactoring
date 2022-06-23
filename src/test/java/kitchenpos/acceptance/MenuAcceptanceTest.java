package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.application.MenuServiceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 관련 기능")
public class MenuAcceptanceTest extends AcceptanceTest {
    
    Menu menu1;
    Menu menu2;

    @BeforeEach
    public void init() {
        super.init();

        MenuGroup 양식 = MenuGroupAcceptanceTest.메뉴_그룹_생성되어_있음("양식").as(MenuGroup.class);
        Product 샐러드 = ProductAcceptanceTest.상품_생성되어_있음("샐러드", 100).as(Product.class);
        Product 스테이크 = ProductAcceptanceTest.상품_생성되어_있음("스테이크", 200).as(Product.class);
        MenuProduct 메뉴_샐러드 = MenuServiceTest.메뉴_상품_생성(샐러드.getId(), 1);
        MenuProduct 메뉴_스테이크 = MenuServiceTest.메뉴_상품_생성(스테이크.getId(), 2);
        menu1 = MenuServiceTest.메뉴_생성(양식.getId(), "스테이크 샐러드 세트 메뉴", 500, 메뉴_샐러드, 메뉴_스테이크);
        menu2 = MenuServiceTest.메뉴_생성(양식.getId(), "샐러드 메뉴", 100, 메뉴_샐러드);
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    void 메뉴_생성() {
        // when
        ExtractableResponse<Response> response = 메뉴_생성_요청(menu1);

        // then
        메뉴_생성됨(response);
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void 메뉴_목록_조회() {
        // given
        ExtractableResponse<Response> createResponse1 = 메뉴_생성_요청(menu1);
        ExtractableResponse<Response> createResponse2 = 메뉴_생성_요청(menu2);

        // when
        ExtractableResponse<Response> response = 메뉴_목록_조회_요청();

        // then
        메뉴_목록_응답됨(response);
        메뉴_목록_포함됨(response, Arrays.asList(createResponse1, createResponse2));
    }

    public static ExtractableResponse<Response> 메뉴_생성되어_있음(
            MenuGroup menuGroup,
            String menuName,
            int menuPrice,
            MenuProduct... menuProducts) {
        Menu menu = MenuServiceTest.메뉴_생성(menuGroup.getId(), menuName, menuPrice, menuProducts);
        return 메뉴_생성_요청(menu);
    }

    public static ExtractableResponse<Response> 메뉴_생성_요청(Menu menu) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menu)
                .when().post("/api/menus")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/menus")
                .then().log().all()
                .extract();
    }

    public static void 메뉴_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 메뉴_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 메뉴_목록_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        List<Long> expectedLineIds = createdResponses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[3]))
                .collect(Collectors.toList());

        List<Long> resultLineIds = response.jsonPath().getList(".", Menu.class).stream()
                .map(Menu::getId)
                .collect(Collectors.toList());

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }
}
