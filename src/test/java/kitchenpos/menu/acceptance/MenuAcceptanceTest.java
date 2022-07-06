package kitchenpos.menu.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.CreateMenuRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.acceptance.ProductAcceptanceTest;
import kitchenpos.product.dto.ProductResponse;
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
        final MenuGroupResponse 메뉴_그룹 = MenuGroupAcceptanceTest.메뉴_그룹_추가_되어_있음(MenuGroupAcceptanceTest.햄버거_메뉴);
        final ProductResponse 불고기버거 = ProductAcceptanceTest.제품_추가_되어_있음(ProductAcceptanceTest.불고기버거);
        final ProductResponse 새우버거 = ProductAcceptanceTest.제품_추가_되어_있음(ProductAcceptanceTest.새우버거);

        MenuProductRequest 불고기버거상품 = new MenuProductRequest(불고기버거.getId(), 5);
        MenuProductRequest 새우버거상품 = new MenuProductRequest(새우버거.getId(), 5);
        CreateMenuRequest createMenuRequest = new CreateMenuRequest("불고기버거 + 새우버거", BigDecimal.valueOf(5_000), 메뉴_그룹.getId(),
                 Arrays.asList(불고기버거상품, 새우버거상품));

        // when
        final ExtractableResponse<Response> 메뉴_추가_요청 = 메뉴_추가_요청(createMenuRequest);
        // then
        final MenuResponse 메뉴_추가_됨 = 메뉴_추가_됨(메뉴_추가_요청);

        // when
        final ExtractableResponse<Response> 전체_메뉴_조회_요청 = 전체_메뉴_조회_요청();
        // then
        final List<MenuResponse> menus = 메뉴_조회_됨(전체_메뉴_조회_요청);

    }

    public static ExtractableResponse<Response> 메뉴_추가_요청(final CreateMenuRequest createMenuRequest) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createMenuRequest)
                .when().post(MENU_URL)
                .then().log().all()
                .extract();
    }

    public static MenuResponse 메뉴_추가_됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response.as(MenuResponse.class);
    }

    public static MenuResponse 메뉴가_추가_되어_있음(final CreateMenuRequest createMenuRequest) {
        return 메뉴_추가_됨(메뉴_추가_요청(createMenuRequest));
    }

    public static ExtractableResponse<Response> 전체_메뉴_조회_요청() {
        return RestAssured.given().log().all()
                .when().get(MENU_URL)
                .then().log().all()
                .extract();
    }
    public List<MenuResponse> 메뉴_조회_됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response.jsonPath().getList(".", MenuResponse.class);
    }

}
