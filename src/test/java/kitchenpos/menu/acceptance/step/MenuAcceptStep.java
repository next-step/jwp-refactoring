package kitchenpos.menu.acceptance.step;

import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuProductResponse;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static kitchenpos.utils.RestAssuredUtil.get;
import static kitchenpos.utils.RestAssuredUtil.post;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class MenuAcceptStep {
    private static final String BASE_URL = "/api/menus";

    public static ExtractableResponse<Response> 메뉴_등록_요청(MenuRequest 등록_요청_데이터) {
        return post(BASE_URL, 등록_요청_데이터);
    }

    public static MenuResponse 메뉴가_등록되어_있음(String name, BigDecimal price, MenuGroupResponse menuGroupResponse, MenuProductRequest menuProductRequest) {
        MenuRequest 등록_요청_데이터 = MenuRequest.of(name, price, menuGroupResponse.getId(), Collections.singletonList(menuProductRequest));

        return 메뉴_등록_요청(등록_요청_데이터).as(MenuResponse.class);
    }

    public static MenuResponse 메뉴_등록_확인(ExtractableResponse<Response> 메뉴_등록_응답, MenuRequest 등록_요청_데이터) {
        MenuResponse 등록된_메뉴 = 메뉴_등록_응답.as(MenuResponse.class);

        assertAll(
                () -> assertThat(메뉴_등록_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(등록된_메뉴).satisfies(등록된_메뉴_확인(등록_요청_데이터))
        );

        return 등록된_메뉴;
    }

    private static Consumer<MenuResponse> 등록된_메뉴_확인(MenuRequest 등록_요청_데이터) {
        return menuResponse -> {
            assertThat(menuResponse.getId()).isNotNull();
            assertThat(menuResponse.getName()).isEqualTo(등록_요청_데이터.getName());
            assertThat(menuResponse.getPrice()).isEqualByComparingTo(등록_요청_데이터.getPrice());
            assertThat(menuResponse.getMenuGroupId()).isEqualTo(등록_요청_데이터.getMenuGroupId());
            메뉴_상품_확인(menuResponse.getMenuProducts(), 등록_요청_데이터.getMenuProducts());
        };
    }

    private static void 메뉴_상품_확인(List<MenuProductResponse> menuProducts, List<MenuProductRequest> expectedMenuProducts) {
        MenuProductRequest expectedMenuProduct = expectedMenuProducts.get(0);

        assertThat(menuProducts.size()).isOne();
        assertThat(menuProducts).first()
                .satisfies(menuProductResponse -> {
                    assertThat(menuProductResponse.getProductId()).isEqualTo(expectedMenuProduct.getProductId());
                    assertThat(menuProductResponse.getQuantity()).isEqualTo(expectedMenuProduct.getQuantity());
                });
    }

    public static ExtractableResponse<Response> 메뉴_목록_조회_요청() {
        return get(BASE_URL);
    }

    public static void 메뉴_목록_조회_확인(ExtractableResponse<Response> 메뉴_목록_조회_응답, MenuResponse 등록된_메뉴) {
        List<MenuResponse> 조회된_메뉴_목록 = 메뉴_목록_조회_응답.as(new TypeRef<List<MenuResponse>>() {
        });

        assertAll(
                () -> assertThat(메뉴_목록_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(조회된_메뉴_목록).satisfies(조회된_메뉴_목록_확인(등록된_메뉴))
        );
    }

    private static Consumer<List<? extends MenuResponse>> 조회된_메뉴_목록_확인(MenuResponse 등록된_메뉴) {
        return menuResponses -> {
            assertThat(menuResponses.size()).isOne();
            assertThat(menuResponses).first()
                    .satisfies(menuResponse -> {
                        assertThat(menuResponse.getId()).isEqualTo(등록된_메뉴.getId());
                        assertThat(menuResponse.getName()).isEqualTo(등록된_메뉴.getName());
                        assertThat(menuResponse.getPrice()).isEqualByComparingTo(등록된_메뉴.getPrice());
                    });
        };
    }
}
