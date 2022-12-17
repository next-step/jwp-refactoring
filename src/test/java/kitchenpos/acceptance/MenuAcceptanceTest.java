package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.acceptance.MenuAcceptanceUtils.*;
import static kitchenpos.acceptance.MenuGroupAcceptanceUtils.메뉴_그룹_등록되어_있음;
import static kitchenpos.acceptance.ProductAcceptanceUtils.상품_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("메뉴 관련 인수 테스트")
class MenuAcceptanceTest extends AcceptanceTest {

    private MenuGroupResponse premiumMenu;
    private ProductResponse honeycombo;
    private MenuProductRequest honeycomboChichkenProduct;

    /**
     * Given 메뉴 그룹이 등록되어 있음
     * And 상품이 등록되어 있음
     */
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        premiumMenu = 메뉴_그룹_등록되어_있음(MenuGroupRequest.from( "premiumMenu")).as(MenuGroupResponse.class);
        honeycombo = 상품_등록되어_있음(ProductRequest.of("honeycombo", BigDecimal.valueOf(18000))).as(ProductResponse.class);
        honeycomboChichkenProduct = MenuProductRequest.of(honeycombo.getId(), 2);
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    void create() {
        List<MenuProductRequest> 메뉴상품_목록 = Arrays.asList(honeycomboChichkenProduct);
        MenuRequest honeycomboChicken = MenuRequest.of("honeycomboChicken", BigDecimal.valueOf(18000), premiumMenu.getId(), 메뉴상품_목록);

        ExtractableResponse<Response> response = 메뉴_생성_요청(honeycomboChicken);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }


    @DisplayName("메뉴 가격이 없으면(null) 메뉴 생성이 실패한다.")
    @Test
    void createFail() {
        List<MenuProductRequest> 메뉴상품_목록 = Arrays.asList(honeycomboChichkenProduct);
        MenuRequest 가격없는_honeycomboChicken = MenuRequest.of("honeycomboChicken", null, premiumMenu.getId(), 메뉴상품_목록);

        ExtractableResponse<Response> response = 메뉴_생성_요청(가격없는_honeycomboChicken);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }


    @DisplayName("메뉴 가격이 음수면 메뉴 생성이 실패한다.")
    @Test
    void createFail2() {
        List<MenuProductRequest> 메뉴상품_목록 = Arrays.asList(honeycomboChichkenProduct);
        MenuRequest 음수가격_honeycomboChicken = MenuRequest.of("honeycomboChicken", BigDecimal.valueOf(-1), premiumMenu.getId(), 메뉴상품_목록);

        ExtractableResponse<Response> response = 메뉴_생성_요청(음수가격_honeycomboChicken);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }


    @DisplayName("메뉴의 메뉴그룹이 존재하지 않으면 메뉴 생성 실패한다.")
    @Test
    void createFail3() {
        List<MenuProductRequest> 메뉴상품_목록 = Arrays.asList(honeycomboChichkenProduct);
        MenuRequest 메뉴그룹_없는_honeycomboChicken = MenuRequest.of("honeycomboChicken", BigDecimal.valueOf(18000), 0L, 메뉴상품_목록);

        ExtractableResponse<Response> response = 메뉴_생성_요청(메뉴그룹_없는_honeycomboChicken);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());

    }


    @DisplayName("상품에 등록되지 않은 메뉴 상품으로 메뉴를 생성할 수 없다.")
    @Test
    void createFail4() {
        MenuProductRequest 상품_등록되어있지_않은_메뉴_상품 = MenuProductRequest.of(0L, 2);
        List<MenuProductRequest> 메뉴상품_목록 = Arrays.asList(상품_등록되어있지_않은_메뉴_상품);
        MenuRequest honeycomboChicken = MenuRequest.of("honeycomboChicken", BigDecimal.valueOf(18000), premiumMenu.getId(), 메뉴상품_목록);

        ExtractableResponse<Response> response = 메뉴_생성_요청(honeycomboChicken);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());

    }

    @DisplayName("메뉴의 가격이 메뉴 상품들의 가격의 합보다 크면 메뉴를 생성할 수 없다.")
    @Test
    void createFail5() {
        MenuProductRequest honeycomboChichkenProduct = MenuProductRequest.of(honeycombo.getId(), 2);
        List<MenuProductRequest> 메뉴상품_목록 = Arrays.asList(honeycomboChichkenProduct);
        MenuRequest honeycomboChicken = MenuRequest.of("honeycomboChicken", BigDecimal.valueOf(50000), premiumMenu.getId(), 메뉴상품_목록);

        ExtractableResponse<Response> response = 메뉴_생성_요청(honeycomboChicken);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }


    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void list() {
        List<MenuProductRequest> 메뉴상품_목록 = Arrays.asList(honeycomboChichkenProduct);
        MenuRequest honeycomboChicken = MenuRequest.of("premiumMenu", BigDecimal.valueOf(18000), premiumMenu.getId(), 메뉴상품_목록);
        메뉴_등록되어_있음(honeycomboChicken);

        ExtractableResponse<Response> response = 메뉴_목록_조회_요청();

        List<MenuResponse> menus = response.jsonPath().getList(".", MenuResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(menus).hasSize(1)
        );

    }
}
