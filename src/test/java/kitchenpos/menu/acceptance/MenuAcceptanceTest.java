package kitchenpos.menu.acceptance;

import static kitchenpos.menu.acceptance.MenuAcceptanceTestHelper.*;
import static kitchenpos.menugroup.acceptance.MenuGroupAcceptanceTestHelper.*;
import static kitchenpos.product.acceptance.ProductAcceptanceTestHelper.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

@DisplayName("메뉴 관리")
public class MenuAcceptanceTest extends AcceptanceTest {

    private MenuProduct 제육볶음_메뉴;
    private MenuProduct 볶음밥_메뉴;

    private MenuGroup 분식;

    @BeforeEach
    public void setUp() {
        super.setUp();
        // given
        Product 제육볶음 = 상품_등록되어_있음("제육볶음", 8900).as(Product.class);
        Product 볶음밥 = 상품_등록되어_있음("볶음밥", 8900).as(Product.class);
        제육볶음_메뉴 = 메뉴(제육볶음.getId(), 1);
        볶음밥_메뉴 = 메뉴(볶음밥.getId(), 1);

        분식 = 메뉴그룹_등록되어_있음("분식").as(MenuGroup.class);
    }

    @DisplayName("메뉴를 등록한다")
    @Test
    void createMenu() {
        // when
        ExtractableResponse<Response> 메뉴_등록_요청_응답 = 메뉴_등록_요청("제육볶음", 7000, 분식.getId(), 제육볶음_메뉴);

        // then
        메뉴_등록됨(메뉴_등록_요청_응답);
    }

    @DisplayName("음수 가격으로 메뉴 등록")
    @Test
    void invalidCreateMenu_NegativePrice() {
        // given
        int 음수가격 = -5000;

        // when
        ExtractableResponse<Response> 메뉴_등록_요청_응답 = 메뉴_등록_요청("제육볶음", 음수가격, 분식.getId(), 제육볶음_메뉴);

        // then
        메뉴_등록_실패됨(메뉴_등록_요청_응답);
    }

    @DisplayName("메뉴 그룹이 등록되어 있지 않을 경우")
    @Test
    void invalidCreateMenu_UnregisteredMenuGroup() {
        // given
        Long 등록되어_있지_않은_메뉴그룹 = -1L;

        // when
        ExtractableResponse<Response> 메뉴_등록_요청_응답 = 메뉴_등록_요청("제육볶음", 7000, 등록되어_있지_않은_메뉴그룹, 제육볶음_메뉴);

        // then
        메뉴_등록_실패됨(메뉴_등록_요청_응답);
    }

    @DisplayName("상품이 등록되어 있지 않을 경우")
    @Test
    void invalidCreateMenu_UnregisteredProduct() {
        // given
        MenuProduct 등록되어_있지_않은_상품 = 메뉴(-1L, 1);

        // when
        ExtractableResponse<Response> 메뉴_등록_요청_응답 = 메뉴_등록_요청("제육볶음", 7000, 분식.getId(), 등록되어_있지_않은_상품);

        // then
        메뉴_등록_실패됨(메뉴_등록_요청_응답);
    }

    @DisplayName("상품가격의 합보다 메뉴 가격이 큰 경우")
    @Test
    void invalidCreateMenu_Expensive() {
        // given
        int 가격 = 999_999;

        // when
        ExtractableResponse<Response> 메뉴_등록_요청_응답 = 메뉴_등록_요청("제육볶음", 가격, 분식.getId(), 제육볶음_메뉴);

        // then
        메뉴_등록_실패됨(메뉴_등록_요청_응답);
    }

    @DisplayName("메뉴 목록 조회")
    @Test
    void list() {
        // when
        ExtractableResponse<Response> 메뉴_목록_조회_요청_응답 = 메뉴_목록_조회_요청();

        // then
        메뉴_목록_조회됨(메뉴_목록_조회_요청_응답);
    }
}
