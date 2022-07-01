package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
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
    void 메뉴를_등록할_수_있다() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(1L);
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(1);

        ExtractableResponse<Response> 메뉴_등록_결과 = 메뉴_등록_요청(product.getName(), product.getPriceBigDecimal(), menuGroup.getId(), Arrays.asList(menuProduct));

        메뉴_등록_성공(메뉴_등록_결과);
    }

    @Test
    void 메뉴가격이_0원미만이면_메뉴를_등록할_수_없다() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(1L);
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(1);

        ExtractableResponse<Response> 메뉴_등록_결과 = 메뉴_등록_요청(product.getName(), BigDecimal.valueOf(-10000), menuGroup.getId(), Arrays.asList(menuProduct));

        메뉴_등록_실패(메뉴_등록_결과);
    }

    @Test
    void 메뉴그룹이_존재하지않으면_메뉴를_등록할_수_없다() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(1L);
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(1);

        ExtractableResponse<Response> 메뉴_등록_결과 = 메뉴_등록_요청(product.getName(), product.getPriceBigDecimal(), null, Arrays.asList(menuProduct));

        메뉴_등록_실패(메뉴_등록_결과);
    }

    @Test
    void 상품이_등록되지_않았으면_메뉴를_등록할_수_없다() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(1L);
        menuProduct.setProductId(999L);
        menuProduct.setQuantity(1);

        ExtractableResponse<Response> 메뉴_등록_결과 = 메뉴_등록_요청(product.getName(), product.getPriceBigDecimal(), menuGroup.getId(), Arrays.asList(menuProduct));

        메뉴_등록_실패(메뉴_등록_결과);
    }

    @Test
    void 상품가격의합보다_메뉴가격이_큰경우_메뉴를_등록할_수_없다() {
        BigDecimal moreBigThanPrice = BigDecimal.valueOf(20000);
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(1L);
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(1);

        ExtractableResponse<Response> 메뉴_등록_결과 = 메뉴_등록_요청(product.getName(), moreBigThanPrice, menuGroup.getId(), Arrays.asList(menuProduct));

        메뉴_등록_실패(메뉴_등록_결과);
    }

    @Test
    void 메뉴를_조회할_수_있다() {
        ExtractableResponse<Response> 메뉴_조회_결과 = 메뉴_조회_요청();

        메뉴_조회_성공(메뉴_조회_결과);
    }
}
