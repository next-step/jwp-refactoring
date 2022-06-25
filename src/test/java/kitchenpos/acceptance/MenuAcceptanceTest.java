package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Menu;
import kitchenpos.dto.MenuGroupResponse;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.dto.ProductResponse;
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

    MenuRequest menuRequest1;
    MenuRequest menuRequest2;

    @BeforeEach
    public void init() {
        super.init();

        ProductResponse 샐러드 = ProductAcceptanceTest.상품_생성되어_있음("샐러드", 100).as(ProductResponse.class);
        ProductResponse 스테이크 = ProductAcceptanceTest.상품_생성되어_있음("스테이크", 200).as(ProductResponse.class);
        ProductResponse 에이드 = ProductAcceptanceTest.상품_생성되어_있음("에이드", 50).as(ProductResponse.class);
        MenuProductRequest 샐러드_1개 = new MenuProductRequest(샐러드.getId(), 1);
        MenuProductRequest 스테이크_1개 = new MenuProductRequest(스테이크.getId(), 1);
        MenuProductRequest 에이드_2개 = new MenuProductRequest(에이드.getId(), 2);
        MenuProductRequest 에이드_1개 = new MenuProductRequest(에이드.getId(), 1);
        MenuGroupResponse 양식 = MenuGroupAcceptanceTest.메뉴_그룹_생성되어_있음("양식").as(MenuGroupResponse.class);
        menuRequest1 = new MenuRequest(
                "커플 메뉴",
                400,
                양식.getId(),
                Arrays.asList(스테이크_1개, 샐러드_1개, 에이드_2개)
        );
        menuRequest2 = new MenuRequest(
                "싱글 메뉴",
                250,
                양식.getId(),
                Arrays.asList(스테이크_1개, 에이드_1개)
        );
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    void 메뉴_생성() {
        // when
        ExtractableResponse<Response> response = 메뉴_생성_요청(menuRequest1);

        // then
        메뉴_생성됨(response);
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void 메뉴_목록_조회() {
        // given
        ExtractableResponse<Response> createResponse1 = 메뉴_생성_요청(menuRequest1);
        ExtractableResponse<Response> createResponse2 = 메뉴_생성_요청(menuRequest2);

        // when
        ExtractableResponse<Response> response = 메뉴_목록_조회_요청();

        // then
        메뉴_목록_응답됨(response);
        메뉴_목록_포함됨(response, Arrays.asList(createResponse1, createResponse2));
    }

    public static ExtractableResponse<Response> 메뉴_생성되어_있음(
            String menuName,
            long menuPrice,
            Long menuGroupId,
            List<MenuProductRequest> menuProducts) {
        return 메뉴_생성_요청(new MenuRequest(menuName, menuPrice, menuGroupId, menuProducts));
    }

    public static ExtractableResponse<Response> 메뉴_생성_요청(MenuRequest menu) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menu)
                .when().post("/api/menus")
                .then().log().all()
                .extract();
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

        List<Long> resultLineIds = response.jsonPath().getList(".", MenuResponse.class).stream()
                .map(MenuResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }
}
