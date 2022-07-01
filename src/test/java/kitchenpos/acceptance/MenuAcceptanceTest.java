package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
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

import static org.assertj.core.api.Assertions.assertThat;

public class MenuAcceptanceTest extends AcceptanceTest {
    private static final String MENU_URL = "/api/menus";
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    /**
     * given 제품 그룹이 추가 됨 / 제품이 추가 됨.
     * <p>
     * when 메뉴를 추가 한다.
     * then 메뉴가 추가됨.
     * <p>
     * when 메뉴를 조회 한다.
     * then 추가된 메뉴가 조회됨.
     */
    @Test
    @DisplayName("메뉴 관리 테스트")
    void menu() {
        // given
        final MenuGroup 메뉴_그룹 = MenuGroupAcceptanceTest.메뉴_그룹_추가_되어_있음(MenuGroupAcceptanceTest.햄버거_메뉴);
        final Product 불고기버거 = ProductAcceptanceTest.제품_추가_되어_있음(ProductAcceptanceTest.불고기버거);
        final Product 새우버거 = ProductAcceptanceTest.제품_추가_되어_있음(ProductAcceptanceTest.새우버거);

        final MenuProduct 불고기 = new MenuProduct();
        불고기.setProductId(불고기버거.getId());
        불고기.setQuantity(1L);

        final MenuProduct 새우 = new MenuProduct();
        새우.setProductId(새우버거.getId());
        새우.setQuantity(1L);

        Menu menu = new Menu();
        menu.setName("불고기버거 + 새우버거");
        menu.setPrice(BigDecimal.valueOf(2000.0));
        menu.setMenuGroupId(메뉴_그룹.getId());
        menu.setMenuProducts(Arrays.asList(불고기, 새우));

        // when
        final ExtractableResponse<Response> 메뉴_추가_요청 = 메뉴_추가_요청(menu);
        // then
        final Menu 메뉴_추가_됨 = 메뉴_추가_됨(메뉴_추가_요청);

        // when
        final ExtractableResponse<Response> 전체_메뉴_조회_요청 = 전체_메뉴_조회_요청();
        // then
        final List<Menu> menus = 메뉴_조회_됨(전체_메뉴_조회_요청);
        final String 메뉴명 = menus.stream()
                .filter(it -> menu.getName().equals(it.getName()))
                .map(Menu::getName)
                .findFirst()
                .get();

        assertThat(메뉴명).isEqualTo(menu.getName());
    }

    public static ExtractableResponse<Response> 메뉴_추가_요청(final Menu menu) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menu)
                .when().post(MENU_URL)
                .then().log().all()
                .extract();
    }

    public static Menu 메뉴_추가_됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response.as(Menu.class);
    }

    public static Menu 메뉴가_추가_되어_있음(final Menu menu) {
        return 메뉴_추가_됨(메뉴_추가_요청(menu));
    }

    public static ExtractableResponse<Response> 전체_메뉴_조회_요청() {
        return RestAssured.given().log().all()
                .when().get(MENU_URL)
                .then().log().all()
                .extract();
    }
    public List<Menu> 메뉴_조회_됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response.jsonPath().getList(".", Menu.class);
    }

}
