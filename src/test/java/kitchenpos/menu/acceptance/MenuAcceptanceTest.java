package kitchenpos.menu.acceptance;

import static kitchenpos.menu.acceptance.MenuGroupAcceptanceTest.메뉴_그룹_등록되어_있음;
import static kitchenpos.product.acceptance.ProductAcceptanceTest.상품_등록_되어있음;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

import org.junit.jupiter.api.*;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@DisplayName("메뉴 관련 기능 인수테스트")
public class MenuAcceptanceTest extends AcceptanceTest {
    private MenuGroup 추천메뉴;
    private Product 허니콤보;
    private Product 레드콤보;

    @TestFactory
    @DisplayName("메뉴 관련 기능 정상 시나리오")
    Stream<DynamicTest> successTest() {
        return Stream.of(
                dynamicTest("메뉴 등록 요청하면 메뉴가 등록된다.", () -> {
                    추천메뉴 = 메뉴_그룹_등록되어_있음("추천메뉴");
                    허니콤보 = 상품_등록_되어있음("허니콤보", 20_000L);
                    레드콤보 = 상품_등록_되어있음("레드콤보", 19_000L);

                    ResponseEntity<Menu> 메뉴_등록_응답_결과 = 메뉴_등록_요청(추천메뉴, "허니레드콤보", 39_000L, 허니콤보, 레드콤보);

                    메뉴_등록됨(메뉴_등록_응답_결과);
                }),
                dynamicTest("메뉴 목록 조회 요청하면 메뉴 목록이 조회된다.", () -> {
                    ResponseEntity<List<Menu>> 메뉴_목록_조회_응답_결과 = 메뉴_목록_조회_요청();

                    메뉴_목록_조회됨(메뉴_목록_조회_응답_결과, 허니콤보, 레드콤보);
                })
        );
    }

    @TestFactory
    @DisplayName("메뉴 관련 기능 예외 시나리오")
    Stream<DynamicTest> failTest() {
        return Stream.of(
                dynamicTest("0원 미만 가격으로 메뉴 등록 요청하면 메뉴 등록 실패한다.", () -> {
                    추천메뉴 = 메뉴_그룹_등록되어_있음("추천메뉴");
                    허니콤보 = 상품_등록_되어있음("허니콤보", 20_000L);
                    레드콤보 = 상품_등록_되어있음("레드콤보", 19_000L);

                    ResponseEntity<Menu> 가격_0원_미만_메뉴_등록_응답_결과 = 메뉴_등록_요청(추천메뉴, "레드허니콤보", -1L, 허니콤보, 레드콤보);

                    메뉴_등록_실패됨(가격_0원_미만_메뉴_등록_응답_결과);
                }),
                dynamicTest("상품 가격 총합보다 높은 가격으로 메뉴 등록 요청하면 메뉴 등록 실패한다.", () -> {
                    ResponseEntity<Menu> 상품_가격_총합_초과_메뉴_등록_응답_결과 = 메뉴_등록_요청(추천메뉴, "허니레드콤보", 40_000L, 허니콤보, 레드콤보);

                    메뉴_등록_실패됨(상품_가격_총합_초과_메뉴_등록_응답_결과);
                }),
                dynamicTest("등록되지 않은 메뉴 그룹에 메뉴 등록 요청하면 메뉴 등록 실패한다.", () -> {
                    MenuGroup 등록되지_않은_메뉴그룹 = new MenuGroup();

                    ResponseEntity<Menu> 등록되지_않는_메뉴그룹_메뉴_등록_응답_결과 = 메뉴_등록_요청(등록되지_않은_메뉴그룹, "레드허니콤보", 39_000L, 허니콤보, 레드콤보);

                    메뉴_등록_실패됨(등록되지_않는_메뉴그룹_메뉴_등록_응답_결과);
                }),
                dynamicTest("등록되지 않은 상품으로 메뉴 등록 요청하면 메뉴 등록 실패한다.", () -> {
                    Product 등록안된_상품 = new Product();

                    ResponseEntity<Menu> 존재하지_않는_메뉴_등록_응답_결과 = 메뉴_등록_요청(추천메뉴, "레드허니콤보", 39_000L, 등록안된_상품);

                    메뉴_등록_실패됨(존재하지_않는_메뉴_등록_응답_결과);
                })
        );
    }

    public static Menu 메뉴_등록_되어있음(MenuGroup menuGroup, String name, long price, Product... products) {
        return 메뉴_등록_요청(menuGroup, name, price, products).getBody();
    }

    public static ResponseEntity<Menu> 메뉴_등록_요청(MenuGroup menuGroup, String name, long price, Product... products) {
        List<MenuProduct> menuProducts = 메뉴_상품_생성(products);
        Menu menu = 메뉴_생성(menuGroup, name, price, menuProducts);
        return testRestTemplate.postForEntity("/api/menus", menu, Menu.class);
    }

    private void 메뉴_목록_조회됨(ResponseEntity<List<Menu>> response, Product... products) {
        List<Long> actualIds = 메뉴_상품_아이디_추출(response);
        List<Long> expectedIds = Arrays.stream(products)
                .map(Product::getId)
                .collect(Collectors.toList());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualIds).containsExactlyElementsOf(expectedIds);
    }

    private List<Long> 메뉴_상품_아이디_추출(ResponseEntity<List<Menu>> response) {
        List<MenuProduct> menuProducts = 메뉴_상품_목록_조회(response);
        return menuProducts.stream()
                .map(MenuProduct::getProductId)
                .collect(Collectors.toList());
    }

    private List<MenuProduct> 메뉴_상품_목록_조회(ResponseEntity<List<Menu>> response) {
        return response.getBody().stream()
                .map(Menu::getMenuProducts)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private ResponseEntity<List<Menu>> 메뉴_목록_조회_요청() {
        return testRestTemplate.exchange("/api/menus", HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Menu>>() {});
    }

    private void 메뉴_등록됨(ResponseEntity<Menu> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().get("Location")).isNotNull();
    }

    private void 메뉴_등록_실패됨(ResponseEntity<Menu> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static Menu 메뉴_생성(MenuGroup menuGroup, String name, long price, List<MenuProduct> menuProducts) {
        Menu request = new Menu();
        request.setMenuGroupId(menuGroup.getId());
        request.setName(name);
        request.setPrice(BigDecimal.valueOf(price));
        request.setMenuProducts(menuProducts);
        return request;
    }

    private static List<MenuProduct> 메뉴_상품_생성(Product[] products) {
        return Arrays.stream(products)
                .map(product -> {
                    MenuProduct menuProduct = new MenuProduct();
                    menuProduct.setProductId(product.getId());
                    menuProduct.setQuantity(1L);
                    return menuProduct;
                })
                .collect(Collectors.toList());
    }
}
