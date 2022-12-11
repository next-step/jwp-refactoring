package kitchenpos.menu.acceptance;


import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 관련 인수 테스트")
public class MenuAcceptanceTest extends AcceptanceTest {
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    /**
     * When : 상품 생성을 요청하면
     * Then : 상품이 생성된다.
     */
    @DisplayName("상품을 생성한다")
    @Test
    void createProduct() {
        // when
        ExtractableResponse<Response> response = 상품_생성_요청("순살치킨", new BigDecimal(9000));

        // then
        상품_생성됨(response);
    }

    /**
     * When : 가격이 0 이하인 상품을 생성하면
     * Then : 상품 생성이 실패한다
     */
    @DisplayName("상품을 생성 실패")
    @Test
    void createProductFailed() {
        // when
        ExtractableResponse<Response> response = 상품_생성_요청("순살치킨", new BigDecimal(-1));

        // then
        상품_생성_실패됨(response);
    }

    /**
     * Given : 상품이 생성되어 있다
     * When : 상품 목록 조회를 요청하면
     * Then : 상품 목록을 응답한다.
     */
    @DisplayName("상품 목록을 조회한다")
    @Test
    void findProducts() {
        // given
        상품_생성_요청("신제품치킨", new BigDecimal(20000));

        // when
        ExtractableResponse<Response> response = 상품_목록_조회();

        // then
        상품_목록_조회됨(response);
    }

    /**
     * When : 메뉴 그룹 생성을 요청한다.
     * Then : 메뉴 그룹이 생성된다.
     */
    @DisplayName("메뉴 그룹 생성 인수 테스트")
    @Test
    void createMenuGroup() {
        // when
        ExtractableResponse<Response> response = 메뉴그룹_생성_요청("세마리 치킨");

        // then
        메뉴그룹_생성됨(response);
    }

    /**
     * Given : 메뉴그룹이 생성되어 있다.
     * When : 메뉴그룹 조회를 요청하면,
     * Then : 메뉴그룹 목록을 응답한다.
     */
    @DisplayName("메뉴그룹 조회 인수 테스트")
    @Test
    void findMenuGroups() {
        // when
        ExtractableResponse<Response> response = 메뉴그룹_조회_요청();

        // then
        메뉴그룹_조회됨(response);
    }

    /**
     * Given : 메뉴그룹과 상품이 생성되어 있다.
     * When : 메뉴 생성을 요청하면
     * Then : 메뉴가 생성된다.
     */
    @DisplayName("메뉴 생성 인수 테스트")
    @Test
    void createMenu() {
        // given
        Long productId = 상품_생성_요청("순살치킨", new BigDecimal(9000)).jsonPath().getLong("id");
        Long menuGroupId = 메뉴그룹_생성_요청("세마리치킨").jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = 메뉴_생성_요청("순살세마리", new BigDecimal(27_000), menuGroupId, Lists.newArrayList(new MenuProduct(productId, 3)));

        // then
        메뉴_생성됨(response);
    }

    /**
     * Given : 메뉴그룹과 상품이 생성되어 있다.
     * When : 메뉴 가격이 0 미만인 경우 생성을 요청하면
     * Then : 메뉴가 생성이 실패한다
     */
    @DisplayName("메뉴 생성 실패(가격미달) 인수 테스트")
    @Test
    void createMenuFailed1() {
        // given
        Long productId = 상품_생성_요청("순살치킨", new BigDecimal(9000)).jsonPath().getLong("id");
        Long menuGroupId = 메뉴그룹_생성_요청("세마리치킨").jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = 메뉴_생성_요청("순살세마리", new BigDecimal(-1), menuGroupId, Lists.newArrayList(new MenuProduct(productId, 3)));

        // then
        메뉴_생성_실패됨(response);
    }

    /**
     * Given : 메뉴그룹과 상품이 생성되어 있다.
     * When : 메뉴 가격이 구성되는 상품 가격의 총합보다 더 크면,
     * Then : 메뉴가 생성이 실패한다
     */
    @DisplayName("메뉴 생성 실패(상품가격 총합 초과) 인수 테스트")
    @Test
    void createMenuFailed2() {
        // given
        Long productId = 상품_생성_요청("순살치킨", new BigDecimal(9000)).jsonPath().getLong("id");
        Long menuGroupId = 메뉴그룹_생성_요청("세마리치킨").jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = 메뉴_생성_요청("순살세마리", new BigDecimal(27_001), menuGroupId, Lists.newArrayList(new MenuProduct(productId, 3)));

        // then
        메뉴_생성_실패됨(response);
    }

    /**
     * Given : 메뉴가 생성되어 있다.
     * When : 메뉴 조회를 요청하면,
     * Then : 메뉴 목록을 응답한다.
     */
    @DisplayName("메뉴 조회 인수 테스트")
    @Test
    void findMenus() {
        // when
        ExtractableResponse<Response> response = 메뉴_조회_요청();

        // then
        메뉴_조회됨(response);
    }

    public static void 메뉴_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> 메뉴_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/menus")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴_생성_요청(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menuRequest = new Menu(name, price, menuGroupId, menuProducts);
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menuRequest)
                .when().post("/api/menus")
                .then().log().all()
                .extract();
    }

    public static void 메뉴_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static ExtractableResponse<Response> 메뉴그룹_생성_요청(String name) {
        MenuGroup menuGroupRequest = new MenuGroup(name);
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menuGroupRequest)
                .when().post("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴그룹_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    public static void 메뉴그룹_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 메뉴그룹_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> 상품_생성_요청(String name, BigDecimal price) {
        Product productRequest = new Product(name, price);
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(productRequest)
                .when().post("/api/products")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 상품_목록_조회() {
        return RestAssured.given().log().all()
                .when().get("/api/products")
                .then().log().all()
                .extract();
    }

    public static void 상품_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 상품_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 상품_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 메뉴_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
