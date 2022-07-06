package kitchenpos.ui.menu;

import static kitchenpos.ui.menu.MenuGroupAcceptanceTest.메뉴_그룹_등록되어_있음;
import static kitchenpos.ui.product.ProductAcceptanceTest.상품_등록_되어있음;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import kitchenpos.AcceptanceTest;

import kitchenpos.dto.menu.MenuGroupResponse;
import kitchenpos.dto.menu.MenuProductRequest;
import kitchenpos.dto.menu.MenuProductResponse;
import kitchenpos.dto.menu.MenuRequest;
import kitchenpos.dto.menu.MenuResponse;
import kitchenpos.dto.product.ProductResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.*;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@DisplayName("메뉴 관련 기능 인수테스트")
public class MenuAcceptanceTest extends AcceptanceTest {
    private MenuGroupResponse 추천메뉴;
    private ProductResponse 허니콤보;

    @TestFactory
    @DisplayName("메뉴 관련 기능 정상 시나리오")
    Stream<DynamicTest> successTest() {
        return Stream.of(
                dynamicTest("메뉴 등록 요청하면 메뉴가 등록된다.", () -> {
                    추천메뉴 = 메뉴_그룹_등록되어_있음("추천메뉴");
                    허니콤보 = 상품_등록_되어있음("허니콤보", 20_000L);

                    ResponseEntity<MenuResponse> 메뉴_등록_응답_결과 = 메뉴_등록_요청(추천메뉴, "허니콤보",
                            20_000L, 허니콤보, 1L);

                    메뉴_등록됨(메뉴_등록_응답_결과);
                }),
                dynamicTest("메뉴 목록 조회 요청하면 메뉴 목록이 조회된다.", () -> {
                    ResponseEntity<List<MenuResponse>> 메뉴_목록_조회_응답_결과 = 메뉴_목록_조회_요청();

                    메뉴_목록_조회됨(메뉴_목록_조회_응답_결과, 허니콤보);
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

                    ResponseEntity<MenuResponse> 가격_0원_미만_메뉴_등록_응답_결과 = 메뉴_등록_요청(추천메뉴, "레드허니콤보", -1L, 허니콤보, 1L);

                    메뉴_등록_실패됨(가격_0원_미만_메뉴_등록_응답_결과);
                }),
                dynamicTest("상품 가격 총합보다 높은 가격으로 메뉴 등록 요청하면 메뉴 등록 실패한다.", () -> {
                    ResponseEntity<MenuResponse> 상품_가격_총합_초과_메뉴_등록_응답_결과 = 메뉴_등록_요청(추천메뉴, "허니레드콤보", 40_000L, 허니콤보, 1L);

                    메뉴_등록_실패됨(상품_가격_총합_초과_메뉴_등록_응답_결과);
                }),
                dynamicTest("등록되지 않은 메뉴 그룹에 메뉴 등록 요청하면 메뉴 등록 실패한다.", () -> {
                    MenuGroupResponse 등록되지_않은_메뉴그룹 = new MenuGroupResponse();

                    ResponseEntity<MenuResponse> 등록되지_않는_메뉴그룹_메뉴_등록_응답_결과 = 메뉴_등록_요청(등록되지_않은_메뉴그룹, "레드허니콤보", 39_000L, 허니콤보, 1L);

                    메뉴_등록_실패됨(등록되지_않는_메뉴그룹_메뉴_등록_응답_결과);
                }),
                dynamicTest("등록되지 않은 상품으로 메뉴 등록 요청하면 메뉴 등록 실패한다.", () -> {
                    ProductResponse 등록안된_상품 = new ProductResponse();

                    ResponseEntity<MenuResponse> 존재하지_않는_메뉴_등록_응답_결과 = 메뉴_등록_요청(추천메뉴, "레드허니콤보", 39_000L, 등록안된_상품, 1L);

                    메뉴_등록_실패됨(존재하지_않는_메뉴_등록_응답_결과);
                })
        );
    }

    public static MenuResponse 메뉴_등록_되어있음(MenuGroupResponse menuGroup, String name, long price, ProductResponse product, long quantity) {
        return 메뉴_등록_요청(menuGroup, name, price, product, quantity).getBody();
    }

    public static ResponseEntity<MenuResponse> 메뉴_등록_요청(MenuGroupResponse menuGroup, String name, long price, ProductResponse product, long quantity) {
        MenuProductRequest menuProductRequest = new MenuProductRequest(product.getId(), quantity);
        MenuRequest menuRequest = new MenuRequest(name, price, menuGroup.getId(), Lists.list(menuProductRequest));
        return testRestTemplate.postForEntity("/api/menus", menuRequest, MenuResponse.class);
    }

    private void 메뉴_목록_조회됨(ResponseEntity<List<MenuResponse>> response, ProductResponse... products) {
        List<Long> actualIds = 메뉴_상품_아이디_추출(response);
        List<Long> expectedIds = Arrays.stream(products)
                .map(ProductResponse::getId)
                .collect(Collectors.toList());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualIds).containsExactlyElementsOf(expectedIds);
    }

    private List<Long> 메뉴_상품_아이디_추출(ResponseEntity<List<MenuResponse>> response) {
        List<MenuProductResponse> menuProducts = 메뉴_상품_목록_조회(response);
        return menuProducts.stream()
                .map(MenuProductResponse::getProductId)
                .collect(Collectors.toList());
    }

    private List<MenuProductResponse> 메뉴_상품_목록_조회(ResponseEntity<List<MenuResponse>> response) {
        return response.getBody().stream()
                .map(MenuResponse::getMenuProducts)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private ResponseEntity<List<MenuResponse>> 메뉴_목록_조회_요청() {
        return testRestTemplate.exchange("/api/menus", HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<MenuResponse>>() {});
    }

    private void 메뉴_등록됨(ResponseEntity<MenuResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().get("Location")).isNotNull();
    }

    private void 메뉴_등록_실패됨(ResponseEntity<MenuResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
