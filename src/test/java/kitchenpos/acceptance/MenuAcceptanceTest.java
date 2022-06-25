package kitchenpos.acceptance;

import static kitchenpos.acceptance.MenuGroupAcceptanceTest.메뉴그룹_등록_요청;
import static kitchenpos.acceptance.ProductAcceptanceTest.상품_등록_요청;
import static kitchenpos.fixture.DomainFactory.createMenuGroup;
import static kitchenpos.fixture.DomainFactory.createProduct;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("메뉴 관련 기능")
public class MenuAcceptanceTest extends AcceptanceTest {
    private MenuGroup 빅맥세트;
    private Product 토마토;
    private Product 양상추;
    private Menu 빅맥버거;

    /**
     * When 메뉴 그룹이 있다.
     * <p>
     * And 메뉴에 들어갈 상품이 있다.
     * <p>
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        빅맥세트 = createMenuGroup(1L, "빅맥세트");
        메뉴그룹_등록_요청(빅맥세트);

        토마토 = createProduct(1L, "토마토", 1000);
        상품_등록_요청(토마토);

        양상추 = createProduct(2L, "양상추", 500);
        상품_등록_요청(양상추);

        빅맥버거 = createMenu(1L, "빅맥버거", 3000, 빅맥세트.getId(),
                Arrays.asList(createMenuProduct(1L, 1L, 토마토.getId(), 1), createMenuProduct(2L, 1L, 양상추.getId(), 4)));
    }

    /**
     * Feature: 메뉴를 관리한다.
     * <p>
     * Scenario: 메뉴 관리
     * <p>
     * When 메뉴 등록 요청
     * <p>
     * Then 메뉴 등록됨
     * <p>
     * When 메뉴 목록 조회 요청
     * <p>
     * Then 메뉴 목록이 조회됨
     */
    @Test
    void 메뉴관리() {
        ExtractableResponse<Response> response;
        // when 메뉴그룹 등록 요청
        response = 메뉴_등록_요청(빅맥버거);
        // then 메뉴그룹 등록됨
        메뉴_등록됨(response);

        // when 메뉴그룹 목록 조회 요청
        response = 메뉴_목록_조회();
        // then 메뉴그룹 목록이 조회됨
        메뉴_목록_조회됨(response);
        // then 메뉴그룹 목록이 조회됨
        메뉴_목록_포함됨(response, Arrays.asList(빅맥버거));
    }

    public static ExtractableResponse<Response> 메뉴_등록_요청(Menu menu) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menu)
                .when().post("/api/menus")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴_목록_조회() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/menus")
                .then().log().all()
                .extract();
    }


    public static void 메뉴_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 메뉴_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 메뉴_목록_포함됨(ExtractableResponse<Response> response, List<Menu> expectedMenus) {
        List<Long> resultMenuIds = response.jsonPath().getList(".", Menu.class).stream()
                .map(Menu::getId)
                .collect(Collectors.toList());

        List<Long> expectedMenuIds = expectedMenus.stream()
                .map(Menu::getId)
                .collect(Collectors.toList());

        assertThat(resultMenuIds).containsAll(expectedMenuIds);
    }

    private Menu createMenu(Long id, String name, long price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(price));
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);

        return menu;
    }

    public static MenuProduct createMenuProduct(Long seq, Long menuId, Long productId, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);

        return menuProduct;
    }
}
