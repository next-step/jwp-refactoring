package kitchenpos.acceptance;

import static kitchenpos.acceptance.MenuAcceptanceTestFixture.메뉴_등록되어_있음;
import static kitchenpos.acceptance.MenuAcceptanceTestFixture.메뉴_목록_조회_요청;
import static kitchenpos.acceptance.MenuAcceptanceTestFixture.메뉴_목록_조회됨;
import static kitchenpos.acceptance.MenuAcceptanceTestFixture.메뉴_목록_포함됨;
import static kitchenpos.acceptance.MenuAcceptanceTestFixture.메뉴_생성_요청;
import static kitchenpos.acceptance.MenuAcceptanceTestFixture.메뉴_생성됨;
import static kitchenpos.acceptance.MenuGroupAcceptanceTestFixture.메뉴_그룹_등록되어_있음;
import static kitchenpos.acceptance.ProductAcceptanceTestFixture.상품_등록_되어_있음;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 관련 기능")
public class MenuAcceptanceTest extends AcceptanceTest {
    private Product 메모리;
    private Product 디스플레이;
    private MenuGroup 핸드폰;
    private MenuProduct 메모리상품;
    private MenuProduct 디스플레이상품;
    private Menu 아이폰;
    private Menu 갤럭시;

    @BeforeEach
    void menuSetUp() {
        super.setUp();

        메모리 = 상품_등록_되어_있음(new Product(null, "메모리", new BigDecimal(3000))).as(Product.class);
        디스플레이 = 상품_등록_되어_있음(new Product(null, "디스플레이", new BigDecimal(5000))).as(Product.class);
        핸드폰 = 메뉴_그룹_등록되어_있음(new MenuGroup(null, "애플")).as(MenuGroup.class);
        메모리상품 = new MenuProduct(null, null, 메모리.getId(), 1);
        디스플레이상품 = new MenuProduct(null, null, 디스플레이.getId(), 1);

        아이폰 = new Menu(null, "아이폰", new BigDecimal(7000), 핸드폰.getId(), new ArrayList<>());
        아이폰.setMenuProducts(Arrays.asList(메모리상품, 디스플레이상품));
        갤럭시 = new Menu(null, "갤럭시", new BigDecimal(5000), 핸드폰.getId(), new ArrayList<>());
        갤럭시.setMenuProducts(Arrays.asList(메모리상품, 디스플레이상품));
    }

    @DisplayName("메뉴를 등록한다.")
    @Test
    void createMenu() {
        ExtractableResponse<Response> response = 메뉴_생성_요청(아이폰);

        메뉴_생성됨(response);
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void getMenuList() {
        ExtractableResponse<Response> createResponse1 = 메뉴_등록되어_있음(아이폰);
        ExtractableResponse<Response> createResponse2 = 메뉴_등록되어_있음(갤럭시);

        ExtractableResponse<Response> response = 메뉴_목록_조회_요청();

        메뉴_목록_조회됨(response);
        메뉴_목록_포함됨(response, Arrays.asList(createResponse1, createResponse2));
    }
}
