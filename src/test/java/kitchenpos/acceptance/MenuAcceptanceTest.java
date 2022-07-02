package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static kitchenpos.acceptance.MenuAcceptanceFactory.메뉴_등록_성공;
import static kitchenpos.acceptance.MenuAcceptanceFactory.메뉴_등록_실패;
import static kitchenpos.acceptance.MenuAcceptanceFactory.메뉴_등록_요청;
import static kitchenpos.acceptance.MenuAcceptanceFactory.메뉴_조회_성공;
import static kitchenpos.acceptance.MenuAcceptanceFactory.메뉴_조회_요청;
import static kitchenpos.acceptance.MenuGroupAcceptanceFactory.메뉴그룹_등록_요청;
import static kitchenpos.acceptance.ProductAcceptanceFactory.상품_등록_요청;

@DisplayName("메뉴 관련")
public class MenuAcceptanceTest extends AcceptanceTest {

    private MenuGroup menuGroup;
    private MenuProduct menuProduct;
    private ProductResponse product;

    @BeforeEach
    public void setUp() {
        super.setUp();
        menuGroup = 메뉴그룹_등록_요청("두마리메뉴").as(MenuGroup.class);
        product = 상품_등록_요청("순살치킨", 17000).as(ProductResponse.class);
    }

    @Test
    void 메뉴를_등록할_수_있다() {
        MenuProductRequest menuProductRequest = new MenuProductRequest(product.getId(), 1L);

        ExtractableResponse<Response> 메뉴_등록_결과 = 메뉴_등록_요청(product.getName(), product.getPrice(), menuGroup.getId(), Arrays.asList(menuProductRequest));

        메뉴_등록_성공(메뉴_등록_결과);
    }

    @Test
    void 메뉴가격이_0원미만이면_메뉴를_등록할_수_없다() {
        int 음수인_가격 = -10000;
        MenuProductRequest menuProductRequest = new MenuProductRequest(product.getId(), 1L);

        ExtractableResponse<Response> 메뉴_등록_결과 = 메뉴_등록_요청(product.getName(), 음수인_가격, menuGroup.getId(), Arrays.asList(menuProductRequest));

        메뉴_등록_실패(메뉴_등록_결과);
    }

    @Test
    void 메뉴그룹이_존재하지않으면_메뉴를_등록할_수_없다() {
        MenuProductRequest menuProductRequest = new MenuProductRequest(product.getId(), 1L);

        ExtractableResponse<Response> 메뉴_등록_결과 = 메뉴_등록_요청(product.getName(), product.getPrice(), null, Arrays.asList(menuProductRequest));

        메뉴_등록_실패(메뉴_등록_결과);
    }

    @Test
    void 상품이_등록되지_않았으면_메뉴를_등록할_수_없다() {
        Long 등록되지않은_상품번호 = 999L;
        MenuProductRequest menuProductRequest = new MenuProductRequest(등록되지않은_상품번호, 1L);

        ExtractableResponse<Response> 메뉴_등록_결과 = 메뉴_등록_요청(product.getName(), product.getPrice(), menuGroup.getId(), Arrays.asList(menuProductRequest));

        메뉴_등록_실패(메뉴_등록_결과);
    }

    @Test
    void 상품가격의합보다_메뉴가격이_큰경우_메뉴를_등록할_수_없다() {
        Integer moreBigThanPrice = 20000;
        MenuProductRequest menuProduct = new MenuProductRequest(product.getId(), 1L);

        ExtractableResponse<Response> 메뉴_등록_결과 = 메뉴_등록_요청(product.getName(), moreBigThanPrice, menuGroup.getId(), Arrays.asList(menuProduct));

        메뉴_등록_실패(메뉴_등록_결과);
    }

    @Test
    void 메뉴를_조회할_수_있다() {
        ExtractableResponse<Response> 메뉴_조회_결과 = 메뉴_조회_요청();

        메뉴_조회_성공(메뉴_조회_결과);
    }
}
