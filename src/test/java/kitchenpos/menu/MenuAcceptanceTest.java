package kitchenpos.menu;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.dto.ProductResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static kitchenpos.menugroup.MenuGroupAcceptanceTest.메뉴그룹_등록되어있음;
import static kitchenpos.product.ProductAcceptanceTest.상품_등록되어_있음;
import static kitchenpos.menu.fixtures.MenuFixtures.후라이드두마리메뉴요청;
import static kitchenpos.menu.fixtures.MenuFixtures.후라이드반양념반메뉴요청;
import static kitchenpos.menugroup.fixtures.MenuGroupFixtures.*;
import static kitchenpos.menu.fixtures.MenuProductFixtures.메뉴상품등록요청;
import static kitchenpos.product.fixtures.ProductFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * packageName : kitchenpos.acceptance
 * fileName : MenuAcceptanceTest
 * author : haedoang
 * date : 2021-12-21
 * description :
 */
@DisplayName("메뉴 인수테스트")
public class MenuAcceptanceTest extends AcceptanceTest {
    private MenuRequest 후라이드두마리등록요청;
    private MenuRequest 양념반후라이드반등록요청;
    private MenuRequest 등록요청_등록되지않은메뉴그룹;
    private MenuRequest 등록요청_존재하지않는상품;

    @BeforeEach
    public void setUp() {
        super.setUp();

        MenuGroupResponse 두마리메뉴그룹 = 메뉴그룹_등록되어있음(두마리메뉴그룹요청());
        ProductResponse 후라이드 = 상품_등록되어_있음(후라이드요청());
        ProductResponse 양념치킨 = 상품_등록되어_있음(양념치킨요청());
        BigDecimal 후라이드두마리가격 = 후라이드.getPrice().multiply(new BigDecimal(2L));
        BigDecimal 후라이드반양념반가격 = 후라이드.getPrice().add(양념치킨.getPrice());

        후라이드두마리등록요청 = 후라이드두마리메뉴요청(
                후라이드두마리가격,
                두마리메뉴그룹.getId(),
                Lists.newArrayList(
                        메뉴상품등록요청(후라이드.getId(), 2L)
                )
        );

        양념반후라이드반등록요청 = 후라이드반양념반메뉴요청(
                후라이드반양념반가격,
                두마리메뉴그룹.getId(),
                Lists.newArrayList(
                        메뉴상품등록요청(후라이드.getId(), 1L),
                        메뉴상품등록요청(양념치킨.getId(), 1L)
                )
        );

        등록요청_등록되지않은메뉴그룹 = 후라이드반양념반메뉴요청(
                후라이드반양념반가격,
                Long.MAX_VALUE,
                Lists.newArrayList(
                        메뉴상품등록요청(후라이드.getId(), 1L),
                        메뉴상품등록요청(양념치킨.getId(), 1L)
                )
        );

        등록요청_존재하지않는상품 = 후라이드반양념반메뉴요청(
                후라이드반양념반가격,
                두마리메뉴그룹.getId(),
                Lists.newArrayList(
                        메뉴상품등록요청(Long.MAX_VALUE, 1L),
                        메뉴상품등록요청(양념치킨.getId(), 1L)
                )
        );
    }

    @Test
    @DisplayName("메뉴를 등록한다.")
    public void create() {
        // when
        ExtractableResponse<Response> response = 메뉴_등록_요청함(후라이드두마리등록요청);

        /// then
        메뉴_등록됨(response);
    }


    @Test
    @DisplayName("메뉴그룹 또는 상품번호가 존재하지 않을 경우 예외처리 한다.")
    public void createFailByMenuGroup() {
        // when
        ExtractableResponse<Response> 등록되지않은메뉴그룹_요청 = 메뉴_등록_요청함(등록요청_등록되지않은메뉴그룹);

        // then
        메뉴_등록_실패함(등록되지않은메뉴그룹_요청);

        // when
        ExtractableResponse<Response> 등록요청_존재하지않는상품_요청 = 메뉴_등록_요청함(등록요청_존재하지않는상품);

        // then
        메뉴_등록_실패함(등록요청_존재하지않는상품_요청);
    }

    @Test
    @DisplayName("메뉴 리스트를 조회한다.")
    public void list() throws Exception {
        // given
        메뉴_등록_되어있음(후라이드두마리등록요청);
        메뉴_등록_되어있음(양념반후라이드반등록요청);

        // when
        ExtractableResponse<Response> response = 메뉴_리스트_조회_요청함();

        // then
        메뉴_리스트_조회됨(response);
    }

    public static MenuResponse 메뉴_등록_되어있음(MenuRequest request) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/menus")
                .then().log().all()
                .extract();

        return response.jsonPath()
                .getObject("", MenuResponse.class);
    }

    private void 메뉴_리스트_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 메뉴_리스트_조회_요청함() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/menus")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 메뉴_등록_요청함(MenuRequest request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/menus")
                .then().log().all()
                .extract();
    }

    private void 메뉴_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotNull();
    }

    private void 메뉴_등록_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

}
