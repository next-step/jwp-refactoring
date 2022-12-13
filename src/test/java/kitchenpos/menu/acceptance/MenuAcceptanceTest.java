package kitchenpos.menu.acceptance;



import static kitchenpos.menugroup.acceptance.MenuGroupAcceptanceTest.메뉴그룹_등록됨;
import static kitchenpos.product.acceptance.ProductAcceptanceTest.상품_등록됨;
import static kitchenpos.product.acceptance.ProductAcceptanceTest.상품_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kitchenpos.common.AcceptanceTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("메뉴 관련 기능")
public class MenuAcceptanceTest extends AcceptanceTest {

    private Menu 짜장_탕수_세트;

    @DisplayName("메뉴 기능 통합 테스트")
    @TestFactory
    Stream<DynamicNode> menu() {
        짜장_탕수_세트 = 짜장_탕수_메뉴();
        return Stream.of(
                dynamicTest("메뉴를 등록한다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 메뉴_생성_요청(짜장_탕수_세트);
                    // then
                    메뉴_정상_생성됨(response);
                }),
                dynamicTest("가격이 없는 메뉴를 등록할 수 없다.", () -> {
                    // given
                    짜장_탕수_세트.setPrice(null);
                    // when
                    ExtractableResponse<Response> response = 메뉴_생성_요청(짜장_탕수_세트);
                    // then
                    메뉴_생성_실패됨(response);
                }),
                dynamicTest("가격이 0 미만인 메뉴를 등록할 수 없다.", () -> {
                    // given
                    짜장_탕수_세트.setPrice(BigDecimal.valueOf(-1));
                    // when
                    ExtractableResponse<Response> response = 메뉴_생성_요청(짜장_탕수_세트);
                    // then
                    메뉴_생성_실패됨(response);
                }),
                dynamicTest("메뉴그룹이 없이 메뉴를 등록 할 수 없다.", () -> {
                    // given
                    짜장_탕수_세트.setMenuGroupId(null);
                    // when
                    ExtractableResponse<Response> response = 메뉴_생성_요청(짜장_탕수_세트);
                    // then
                    메뉴_생성_실패됨(response);

                }),
                dynamicTest("등록하려는 메뉴의 메뉴상품이 등록되지 않으면 등록할 수 없다.", () -> {
                    // given
                    Product 미등록_짬뽕 = 상품_생성("짬뽕", 7000L);
                    Product 미등록_양장피 = 상품_생성("양장피", 20000L);
                    MenuGroup 중식 = 메뉴그룹_등록됨("중식");
                    Menu 신규_메뉴 = 메뉴_생성("미등록_메뉴", 22000L, 중식, 미등록_짬뽕, 미등록_양장피);
                    // when
                    ExtractableResponse<Response> response = 메뉴_생성_요청(신규_메뉴);
                    // then
                    메뉴_생성_실패됨(response);
                }),
                dynamicTest("메뉴의 가격을 메뉴상품의 총금액보다 높게 책정할 수 없다.", () -> {
                    // given
                    짜장_탕수_세트.setPrice(BigDecimal.valueOf(30000));
                    // when
                    ExtractableResponse<Response> response = 메뉴_생성_요청(짜장_탕수_세트);
                    // then
                    메뉴_생성_실패됨(response);

                }),
                dynamicTest("메뉴의 목록을 조회할 수 있다.", () -> {
                    // given
                    짜장_탕수_메뉴_생성();
                    // when
                    ExtractableResponse<Response> 메뉴_목록 = 메뉴_목록_조회_요청();
                    // then
                    메뉴_목록_정상_응답됨(메뉴_목록, "짜장_탕수_세트");
                })
        );
    }

    public static Menu 짜장_탕수_메뉴(){
        MenuGroup 중식 = 메뉴그룹_등록됨("중식");
        Product 짜장면 = 상품_등록됨("짜장면", 9000L);
        Product 탕수육 = 상품_등록됨("탕수육", 12000L);
        return 메뉴_생성("짜장_탕수_세트", 15000L, 중식, 짜장면, 탕수육);
    }
    public static Menu 짜장_탕수_메뉴_생성(){
        return 메뉴_생성_요청(짜장_탕수_메뉴()).as(Menu.class);
    }
    private static Menu 메뉴_생성(String name, Long price, MenuGroup menuGroup, Product... products){
        List<MenuProduct> menuProducts = toMenuProductList(products);
        return new Menu(null, name, BigDecimal.valueOf(price), menuGroup.getId(), menuProducts);
    }
    private static List<MenuProduct> toMenuProductList(Product... products){
        return Arrays.asList(products)
                .stream()
                .map(MenuAcceptanceTest::toMenuProduct)
                .collect(Collectors.toList());
    }

    private static MenuProduct toMenuProduct(Product product){
        return new MenuProduct(null, null, product.getId(), 1L);
    }

    public static ExtractableResponse<Response> 메뉴_생성_요청(Menu menu) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menu)
                .when().post("/api/menus")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 메뉴_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/menus")
                .then().log().all()
                .extract();
    }

    private void 메뉴_정상_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
    private void 메뉴_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private void 메뉴_목록_정상_응답됨(ExtractableResponse<Response> response, String... 메뉴_목록) {
        List<String> 메뉴_조회_결과_목록 = response.jsonPath()
                .getList(".", Menu.class)
                .stream()
                .map(Menu::getName)
                .collect(Collectors.toList());

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(메뉴_조회_결과_목록).containsAll(Arrays.asList(메뉴_목록))
        );
    }


}
