package kitchenpos.application.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.application.acceptance.MenuGroupAcceptanceTest.메뉴_그룹_생성_요청;
import static kitchenpos.application.acceptance.ProductAcceptanceTest.상품_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DisplayName("메뉴 관련 기능")
class MenuAcceptanceTest extends BaseAcceptanceTest {

    private MenuGroup 메뉴_그룹;

    private Product 상품;

    private MenuProduct 메뉴_상품;

    private final String MENU_NAME = "순살치킨";

    @BeforeEach
    public void setUp() {
        super.setUp();


        메뉴_그룹 = 메뉴_그룹_생성_요청("신메뉴").as(MenuGroup.class);
        상품 = 상품_생성_요청(MENU_NAME, new BigDecimal(15000)).as(Product.class);
        메뉴_상품 = new MenuProduct();
        메뉴_상품.setProductId(상품.getId());
        메뉴_상품.setQuantity(1);
    }

    /**
     * When 메뉴를 생성하면
     * Then 저장된 메뉴 데이터가 리턴된다
     */
    @DisplayName("메뉴 생성에 성공한다.")
    @Test
    void createMenu() {
        // when
        ExtractableResponse<Response> 메뉴_생성_요청_응답 = 메뉴_생성_요청(MENU_NAME, new BigDecimal(15000), 메뉴_그룹.getId(), Arrays.asList(메뉴_상품));

        // then
        assertAll(
                () -> assertEquals(HttpStatus.CREATED.value(), 메뉴_생성_요청_응답.statusCode()),
                () -> assertNotNull(메뉴_생성_요청_응답.jsonPath().get("id")),
                () -> assertEquals(MENU_NAME, 메뉴_생성_요청_응답.jsonPath().get("name"))
        );
    }

    /**
     * When 가격을 음수로 지정하여 메뉴를 생성하면
     * Then 메뉴를 생성할 수 없다.
     */
    @DisplayName("가격이 음수인 경우 메뉴를 생성할 수 없다.")
    @Test
    void createMenuWithNegativePrice() {
        // when
        ExtractableResponse<Response> 메뉴_생성_요청_응답 = 메뉴_생성_요청(MENU_NAME, new BigDecimal(-15000), 메뉴_그룹.getId(), Arrays.asList(메뉴_상품));

        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), 메뉴_생성_요청_응답.statusCode());
    }

    /**
     * When 등록되지 않은 메뉴 그룹 ID 를 이용하여 메뉴를 생성하면
     * Then 메뉴를 생성할 수 없다.
     */
    @DisplayName("등록되지 않은 메뉴 그룹 ID 를 사용하면 메뉴를 생성할 수 없다.")
    @Test
    void createMenuWithInvalidMenuGroupId() {
        // when
        ExtractableResponse<Response> 메뉴_생성_요청_응답 = 메뉴_생성_요청(MENU_NAME, new BigDecimal(15000), 100L, Arrays.asList(메뉴_상품));

        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), 메뉴_생성_요청_응답.statusCode());
    }

    /**
     * When (메뉴상품의 수량 * 상품 가격) 의 곱보다 메뉴 가격이 크면
     * Then 메뉴를 생성할 수 없다.
     */
    @DisplayName("메뉴의 가격 조건이 맞지 않는 경우 메뉴를 생성할 수 없다.")
    @Test
    void createMenuWithInvalidPrice() {
        // when
        ExtractableResponse<Response> 메뉴_생성_요청_응답 = 메뉴_생성_요청(MENU_NAME, new BigDecimal(20000), 메뉴_그룹.getId(), Arrays.asList(메뉴_상품));

        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), 메뉴_생성_요청_응답.statusCode());
    }

    /**
     * When 메뉴를 생성하면
     * Then 저장된 메뉴 데이터와 메뉴 상품 목록이 함께 리턴된다
     */
    @DisplayName("메뉴 생성 결과 내 메뉴 상품 목록 추가하여 리턴한다.")
    @Test
    void createMenuWithMenuProduct() {
        // when
        ExtractableResponse<Response> 메뉴_생성_요청_응답 = 메뉴_생성_요청(MENU_NAME, new BigDecimal(15000), 메뉴_그룹.getId(), Arrays.asList(메뉴_상품));

        // then
        assertAll(
                () -> assertEquals(HttpStatus.CREATED.value(), 메뉴_생성_요청_응답.statusCode()),
                () -> assertThat(메뉴_생성_요청_응답.jsonPath().getList("menuProducts")).hasSize(1)
        );
    }

    /**
     * When 메뉴 목록을 조회하면
     * Then 메뉴 상품과 함께 조회된다
     */
    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void list() {
        // given
        메뉴_생성_요청(MENU_NAME, new BigDecimal(15000), 메뉴_그룹.getId(), Arrays.asList(메뉴_상품));

        // when
        ExtractableResponse<Response> 메뉴_목록_조회_요청_응답 = 메뉴_목록_조회_요청();

        // then
        assertThat(메뉴_목록_조회_요청_응답.jsonPath().getList("menuProducts")).hasSize(1);
    }

    public static ExtractableResponse<Response> 메뉴_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/menus")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴_생성_요청(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);

        return RestAssured
                .given().log().all()
                .body(menu)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/menus")
                .then().log().all()
                .extract();
    }
}
