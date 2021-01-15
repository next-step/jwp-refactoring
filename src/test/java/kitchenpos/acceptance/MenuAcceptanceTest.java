package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuAcceptanceTest extends AcceptanceTest {
    private Menu menu;
    private List<MenuProduct> menuProducts = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        super.setUp();
        menuProducts.add(new MenuProduct(2L,1));
        menuProducts.add(new MenuProduct(3L,1));
        menu = new Menu("강정치킨", new BigDecimal(17000), 3L, menuProducts);

    }

    @DisplayName("메뉴를 관리한다.")
    @Test
    void manageMenu() {
        //menu create
        ExtractableResponse<Response> createResponse = 메뉴_등록_요청(menu);
        메뉴_등록됨(createResponse);

        //menu find
        ExtractableResponse<Response> findResponse = 메뉴목록_조회_요청();
        메뉴목록_조회됨(findResponse);
    }

    private ExtractableResponse<Response> 메뉴_등록_요청(Menu menu) {
        return RestAssured.given().log().all().
                body(menu).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                when().post("/api/menus").
                then().log().all().
                extract();
    }

    private ExtractableResponse<Response> 메뉴목록_조회_요청() {
        return RestAssured.given().log().all().
                when().get("/api/menus").
                then().log().all().
                extract();
    }

    private void 메뉴_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.as(Menu.class).getName()).isEqualTo(menu.getName());
        assertThat(response.as(Menu.class).getPrice().compareTo(menu.getPrice())).isEqualTo(0);
    }

    private void 메뉴목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<Menu> menus = response.jsonPath().getList(".", Menu.class);
        List<String> menuNames = menus.stream().map(menu -> menu.getName()).collect(Collectors.toList());

        assertThat(menuNames).contains(menu.getName());
    }

}
