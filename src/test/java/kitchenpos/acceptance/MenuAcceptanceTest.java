package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
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
    private Product product;

    @BeforeEach
    public void setUp() {
        super.setUp();
        menuGroup = 메뉴그룹_등록_요청("두마리메뉴").as(MenuGroup.class);
        product = 상품_등록_요청("순살치킨", 17000).as(Product.class);
    }

    @Test
    void 메뉴등록_성공() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(1L);
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(1);

        ExtractableResponse<Response> 메뉴_등록_결과 = 메뉴_등록_요청(product.getName(), product.getPrice(), menuGroup.getId(), Arrays.asList(menuProduct));

        메뉴_등록_성공(메뉴_등록_결과);
    }

    @Test
    void 메뉴등록_실패_메뉴가격_0원미만() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(1L);
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(1);

        ExtractableResponse<Response> 메뉴_등록_결과 = 메뉴_등록_요청(product.getName(), BigDecimal.valueOf(-10000), menuGroup.getId(), Arrays.asList(menuProduct));

        메뉴_등록_실패(메뉴_등록_결과);
    }

    @Test
    void 메뉴등록_실패_메뉴그룹_존재하지않음() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(1L);
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(1);

        ExtractableResponse<Response> 메뉴_등록_결과 = 메뉴_등록_요청(product.getName(), product.getPrice(), null, Arrays.asList(menuProduct));

        메뉴_등록_실패(메뉴_등록_결과);
    }

    @Test
    void 메뉴등록_실패_상품이_등록되지않음() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(1L);
        menuProduct.setProductId(999L);
        menuProduct.setQuantity(1);

        ExtractableResponse<Response> 메뉴_등록_결과 = 메뉴_등록_요청(product.getName(), product.getPrice(), menuGroup.getId(), Arrays.asList(menuProduct));

        메뉴_등록_실패(메뉴_등록_결과);
    }

    @Test
    void 메뉴등록_실패_메뉴가격이_상품들가격합보다_큰경우() {
        BigDecimal moreBigThanPrice = BigDecimal.valueOf(20000);
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(1L);
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(1);

        ExtractableResponse<Response> 메뉴_등록_결과 = 메뉴_등록_요청(product.getName(), moreBigThanPrice, menuGroup.getId(), Arrays.asList(menuProduct));

        메뉴_등록_실패(메뉴_등록_결과);
    }

    @Test
    void 메뉴_조회() {
        ExtractableResponse<Response> 메뉴_조회_결과 = 메뉴_조회_요청();

        메뉴_조회_성공(메뉴_조회_결과);
    }
}
