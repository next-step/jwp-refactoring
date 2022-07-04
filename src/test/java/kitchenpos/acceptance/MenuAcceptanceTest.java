package kitchenpos.acceptance;

import static kitchenpos.__fixture__.MenuProductTestFixture.메뉴_상품_1개_생성;
import static kitchenpos.acceptance.MenuGroupAcceptanceTest.메뉴_그룹_생성_요청;
import static kitchenpos.acceptance.ProductAcceptanceTest.상품_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("메뉴 인수 테스트")
public class MenuAcceptanceTest extends AcceptanceTest {

    private Long 두마리_메뉴_아이디;
    private Long 후라이드_아이디;
    private Long 양념_아이디;

    @BeforeEach
    public void setUp() {
        super.setUp();
        두마리_메뉴_아이디 = 메뉴_그룹_생성_요청("두마리메뉴").jsonPath().getLong("id");
        후라이드_아이디 = 상품_생성_요청("후라이드", 16_000).jsonPath().getLong("id");
        양념_아이디 = 상품_생성_요청("양념", 17_000).jsonPath().getLong("id");
    }

    @DisplayName("메뉴 생성에 성공한다.")
    @Test
    void createMenu() {
        //when
        final ExtractableResponse<Response> 결과 = 메뉴_생성_요청(
                "후라이드양념",
                31_000,
                두마리_메뉴_아이디,
                메뉴_상품_1개_생성(후라이드_아이디),
                메뉴_상품_1개_생성(양념_아이디)
        );

        //then
        assertThat(결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(결과.jsonPath().getString("name")).isEqualTo("후라이드양념");
        assertThat(결과.jsonPath().getLong("menuGroupId")).isEqualTo(두마리_메뉴_아이디);
        assertThat(결과.jsonPath().getList("menuProducts").size()).isEqualTo(2);
    }

    @DisplayName("메뉴의 가격이 음수일 경우 메뉴 생성에 실패한다.")
    @Test
    void createMenuFailedWhenPriceIsMinus() {
        //when
        final ExtractableResponse<Response> 결과 = 메뉴_생성_요청(
                "후라이드양념",
                -16_000,
                두마리_메뉴_아이디,
                메뉴_상품_1개_생성(후라이드_아이디),
                메뉴_상품_1개_생성(양념_아이디)
        );

        //then
        assertThat(결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("메뉴 그룹이 존재하지 않을 경우 메뉴 생성에 실패한다.")
    @Test
    void createMenuFailedWhenMenuGroupIsNotExists() {
        //given
        final Long 존재하지_않는_메뉴_그룹_아이디 = 2L;

        //when
        final ExtractableResponse<Response> 결과 = 메뉴_생성_요청(
                "후라이드양념",
                16_000,
                존재하지_않는_메뉴_그룹_아이디,
                메뉴_상품_1개_생성(후라이드_아이디),
                메뉴_상품_1개_생성(양념_아이디)
        );

        //then
        assertThat(결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("메뉴의 가격이 상품의 합계보다 클 경우 메뉴 생성에 실패한다.")
    @Test
    void createMenuFailedWhenPriceIsGreaterThanProductsPriceSum() {
        //when
        final ExtractableResponse<Response> 결과 = 메뉴_생성_요청(
                "후라이드양념",
                34_000,
                두마리_메뉴_아이디,
                메뉴_상품_1개_생성(후라이드_아이디),
                메뉴_상품_1개_생성(양념_아이디)
        );

        //then
        assertThat(결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("메뉴를 조회할 수 있다.")
    @Test
    void findMenus() {
        //given
        메뉴_생성_요청(
                "후라이드양념",
                31_000,
                두마리_메뉴_아이디,
                메뉴_상품_1개_생성(후라이드_아이디),
                메뉴_상품_1개_생성(양념_아이디)
        );

        //when
        final ExtractableResponse<Response> 메뉴_목록 = 메뉴_목록_조회();

        //then
        assertThat(메뉴_목록.jsonPath().getList(".").size()).isEqualTo(1);
        assertThat(메뉴_목록.jsonPath().getString("[0].name")).isEqualTo("후라이드양념");
    }

    public static ExtractableResponse<Response> 메뉴_생성_요청(final String name, final int price, final Long menuGroupId,
                                                         final MenuProduct... menuProducts) {
        final Menu menu = new Menu(name, BigDecimal.valueOf(price), menuGroupId, menuProducts);

        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(menu)
                .when().post("/api/menus")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴_목록_조회() {
        return RestAssured.given().log().all()
                .when().get("/api/menus")
                .then().log().all()
                .extract();
    }
}
