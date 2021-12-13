package kitchenpos.acceptance.step;

import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.function.Consumer;

import static kitchenpos.utils.RestAssuredUtil.get;
import static kitchenpos.utils.RestAssuredUtil.post;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class MenuAcceptStep {
    private static final String BASE_URL = "/api/menus";

    public static ExtractableResponse<Response> 메뉴_등록_요청(Menu 등록_요청_데이터) {
        return post(BASE_URL, 등록_요청_데이터);
    }

    public static Menu 메뉴_등록_확인(ExtractableResponse<Response> 메뉴_등록_응답, Menu 등록_요청_데이터) {
        Menu 등록된_메뉴 = 메뉴_등록_응답.as(Menu.class);

        assertAll(
                () -> assertThat(메뉴_등록_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(등록된_메뉴).satisfies(등록된_메뉴_확인(등록_요청_데이터))
        );

        return 등록된_메뉴;
    }

    private static Consumer<Menu> 등록된_메뉴_확인(Menu 등록_요청_데이터) {
        return menu -> {
            assertThat(menu.getId()).isNotNull();
            assertThat(menu.getName()).isEqualTo(등록_요청_데이터.getName());
            assertThat(menu.getPrice()).isEqualByComparingTo(등록_요청_데이터.getPrice());
            assertThat(menu.getMenuGroupId()).isEqualTo(등록_요청_데이터.getMenuGroupId());
            메뉴_상품_확인(menu.getMenuProducts(), 등록_요청_데이터.getMenuProducts());
        };
    }

    private static void 메뉴_상품_확인(List<MenuProduct> menuProducts, List<MenuProduct> expectedMenuProducts) {
        MenuProduct expectedMenuProduct = expectedMenuProducts.get(0);

        assertThat(menuProducts.size()).isOne();
        assertThat(menuProducts).first()
                .satisfies(menuProduct -> {
                    assertThat(menuProduct.getProductId()).isEqualTo(expectedMenuProduct.getProductId());
                    assertThat(menuProduct.getQuantity()).isEqualTo(expectedMenuProduct.getQuantity());
                });
    }

    public static ExtractableResponse<Response> 메뉴_목록_조회_요청() {
        return get(BASE_URL);
    }

    public static void 메뉴_목록_조회_확인(ExtractableResponse<Response> 메뉴_목록_조회_응답, Menu 등록된_메뉴) {
        List<Menu> 조회된_메뉴_목록 = 메뉴_목록_조회_응답.as(new TypeRef<List<Menu>>() {
        });

        assertAll(
                () -> assertThat(메뉴_목록_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(조회된_메뉴_목록).satisfies(조회된_상품_목록_확인(등록된_메뉴))
        );
    }

    private static Consumer<List<? extends Menu>> 조회된_상품_목록_확인(Menu 등록된_메뉴) {
        return menus -> {
            assertThat(menus.size()).isOne();
            assertThat(menus).first()
                    .satisfies(menu -> {
                        assertThat(menu.getId()).isEqualTo(등록된_메뉴.getId());
                        assertThat(menu.getName()).isEqualTo(등록된_메뉴.getName());
                        assertThat(menu.getPrice()).isEqualByComparingTo(등록된_메뉴.getPrice());
                    });
        };
    }
}
