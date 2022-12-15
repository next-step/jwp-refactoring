//package kitchenpos.acceptance;
//
//import io.restassured.response.ExtractableResponse;
//import io.restassured.response.Response;
//import kitchenpos.domain.menu.Menu;
//import kitchenpos.domain.menu.MenuGroup;
//import kitchenpos.domain.menu.MenuProduct;
//import kitchenpos.domain.product.Product;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.http.HttpStatus;
//
//import java.math.BigDecimal;
//import java.util.Arrays;
//import java.util.List;
//
//import static kitchenpos.acceptance.MenuGroupAcceptanceStep.메뉴_그룹_등록되어_있음;
//import static kitchenpos.acceptance.MenuAcceptanceStep.*;
//import static kitchenpos.acceptance.ProductAcceptanceStep.상품_등록되어_있음;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertAll;
//
//@DisplayName("메뉴 관련 인수 테스트")
//class MenuAcceptanceTest extends AcceptanceTest {
//
//    private MenuGroup 프리미엄메뉴;
//    private Product 허니콤보;
//    private MenuProduct 허니콤보치킨_상품;
//
//    /**
//     * Given 메뉴 그룹이 등록되어 있음
//     * And 상품이 등록되어 있음
//     */
//    @Override
//    @BeforeEach
//    public void setUp() {
//        super.setUp();
//
//        프리미엄메뉴 = MenuGroup.of(1L, "프리미엄메뉴");
//        허니콤보 = Product.of(1L, "허니콤보", BigDecimal.valueOf(18000));
//        허니콤보치킨_상품 = MenuProduct.of(1L, null, 허니콤보.getId(), 2);
//
//        메뉴_그룹_등록되어_있음(프리미엄메뉴);
//        상품_등록되어_있음(허니콤보);
//    }
//
//    /**
//     * When 메뉴 생성 요청
//     * Then 메뉴 생성됨
//     */
//    @DisplayName("메뉴를 생성한다.")
//    @Test
//    void create() {
//        List<MenuProduct> 메뉴상품_목록 = Arrays.asList(허니콤보치킨_상품);
//        Menu 허니콤보치킨 = Menu.of(1L, "허니콤보치킨", BigDecimal.valueOf(18000), 프리미엄메뉴.getId(), 메뉴상품_목록);
//
//        ExtractableResponse<Response> response = 메뉴_생성_요청(허니콤보치킨);
//
//        메뉴_생성됨(response);
//    }
//
//    /**
//     * When 가격이 없는 메뉴 생성 요청
//     * Then 메뉴 생성 실패함
//     */
//    @DisplayName("메뉴 가격이 없으면(null) 메뉴 생성이 실패한다.")
//    @Test
//    void createFail() {
//        List<MenuProduct> 메뉴상품_목록 = Arrays.asList(허니콤보치킨_상품);
//        Menu 가격없는_허니콤보치킨 = Menu.of(1L, "허니콤보치킨", null, 프리미엄메뉴.getId(), 메뉴상품_목록);
//
//        ExtractableResponse<Response> response = 메뉴_생성_요청(가격없는_허니콤보치킨);
//
//        메뉴_생성_실패함(response);
//    }
//
//    /**
//     * When 가격이 음수인 메뉴 생성 요청
//     * Then 메뉴 생성 실패함
//     */
//    @DisplayName("메뉴 가격이 음수면 메뉴 생성이 실패한다.")
//    @Test
//    void createFail2() {
//        List<MenuProduct> 메뉴상품_목록 = Arrays.asList(허니콤보치킨_상품);
//        Menu 음수가격_허니콤보치킨 = Menu.of(1L, "허니콤보치킨", BigDecimal.valueOf(-1), 프리미엄메뉴.getId(), 메뉴상품_목록);
//
//        ExtractableResponse<Response> response = 메뉴_생성_요청(음수가격_허니콤보치킨);
//
//        메뉴_생성_실패함(response);
//    }
//
//    /**
//     * When 메뉴 그룹이 존재하지 않는 메뉴 생성 요청
//     * Then 메뉴 생성 실패함
//     */
//    @DisplayName("메뉴의 메뉴그룹이 존재하지 않으면 메뉴 생성 실패한다.")
//    @Test
//    void createFail3() {
//        List<MenuProduct> 메뉴상품_목록 = Arrays.asList(허니콤보치킨_상품);
//        Menu 메뉴그룹_없는_허니콤보치킨 = Menu.of(1L, "허니콤보치킨", BigDecimal.valueOf(18000), 0L, 메뉴상품_목록);
//
//        ExtractableResponse<Response> response = 메뉴_생성_요청(메뉴그룹_없는_허니콤보치킨);
//
//        메뉴_생성_실패함(response);
//    }
//
//    /**
//     * When 상품에 등록되지 않은 메뉴 상품으로 메뉴 생성 요청
//     * Then 메뉴 생성 실패함
//     */
//    @DisplayName("상품에 등록되지 않은 메뉴 상품으로 메뉴를 생성할 수 없다.")
//    @Test
//    void createFail4() {
//        MenuProduct 상품_등록되어있지_않은_메뉴_상품 = MenuProduct.of(1L, null, 0L, 2);
//        List<MenuProduct> 메뉴상품_목록 = Arrays.asList(상품_등록되어있지_않은_메뉴_상품);
//        Menu 허니콤보치킨 = Menu.of(1L, "허니콤보치킨", BigDecimal.valueOf(18000), 프리미엄메뉴.getId(), 메뉴상품_목록);
//
//        ExtractableResponse<Response> response = 메뉴_생성_요청(허니콤보치킨);
//
//        메뉴_생성_실패함(response);
//    }
//
//    /**
//     * When 메뉴의 가격이 메뉴 상품들의 가격의 합보다 큰 메뉴 생성 요청
//     * Then 메뉴 생성 실패함
//     */
//    @DisplayName("메뉴의 가격이 메뉴 상품들의 가격의 합보다 크면 메뉴를 생성할 수 없다.")
//    @Test
//    void createFail5() {
//        MenuProduct 허니콤보치킨_상품 = MenuProduct.of(1L, null, 허니콤보.getId(), 2);
//        List<MenuProduct> 메뉴상품_목록 = Arrays.asList(허니콤보치킨_상품);
//        Menu 허니콤보치킨 = Menu.of(1L, "허니콤보치킨", BigDecimal.valueOf(50000), 프리미엄메뉴.getId(), 메뉴상품_목록);
//
//        ExtractableResponse<Response> response = 메뉴_생성_요청(허니콤보치킨);
//
//        메뉴_생성_실패함(response);
//    }
//
//    /**
//     * Given 메뉴 등록되어 있음
//     * When 메뉴 목록 조회 요청
//     * Then 메뉴 목록 조회됨
//     */
//    @DisplayName("메뉴 목록을 조회한다.")
//    @Test
//    void list() {
//        List<MenuProduct> 메뉴상품_목록 = Arrays.asList(허니콤보치킨_상품);
//        Menu 허니콤보치킨 = Menu.of(1L, "허니콤보치킨", BigDecimal.valueOf(18000), 프리미엄메뉴.getId(), 메뉴상품_목록);
//        메뉴_등록되어_있음(허니콤보치킨);
//
//        ExtractableResponse<Response> response = 메뉴_목록_조회_요청();
//
//        메뉴_목록_조회됨(response);
//    }
//
//    private void 메뉴_생성됨(ExtractableResponse<Response> response) {
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
//    }
//
//    private void 메뉴_생성_실패함(ExtractableResponse<Response> response) {
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
//    }
//
//    private void 메뉴_목록_조회됨(ExtractableResponse<Response> response) {
//        List<Menu> menus = response.jsonPath().getList(".", Menu.class);
//        assertAll(
//                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
//                () -> assertThat(menus).hasSize(1),
//                () -> assertThat(menus.get(0).getMenuProducts()).containsExactly(허니콤보치킨_상품)
//        );
//    }
//}
