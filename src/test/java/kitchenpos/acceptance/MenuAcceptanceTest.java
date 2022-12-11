package kitchenpos.acceptance;

import static kitchenpos.acceptance.MenuAcceptanceTestFixture.메뉴_등록되어_있음;
import static kitchenpos.acceptance.MenuAcceptanceTestFixture.메뉴_목록_조회_요청;
import static kitchenpos.acceptance.MenuAcceptanceTestFixture.메뉴_목록_조회됨;
import static kitchenpos.acceptance.MenuAcceptanceTestFixture.메뉴_목록_포함됨;
import static kitchenpos.acceptance.MenuAcceptanceTestFixture.메뉴_생성_요청;
import static kitchenpos.acceptance.MenuAcceptanceTestFixture.메뉴_생성됨;
import static kitchenpos.domain.MenuFixture.createMenu;
import static kitchenpos.domain.MenuProductFixture.createMenuProduct;
import static kitchenpos.menugroup.acceptance.MenuGroupAcceptanceTestFixture.메뉴_그룹_등록되어_있음;
import static kitchenpos.product.acceptance.ProductAcceptanceTestFixture.상품_등록_되어_있음;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroupFixture;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.product.domain.ProductFixture;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 관련 기능")
public class MenuAcceptanceTest extends AcceptanceTest {
    private ProductResponse 후라이드치킨;
    private ProductResponse 양념치킨;
    private ProductResponse 콜라;
    private MenuGroupResponse 추천메뉴;
    private MenuProduct 후라이드치킨상품;
    private MenuProduct 양념치킨상품;
    private MenuProduct 콜라상품;
    private Menu 두마리치킨;
    private Menu 양념세트;

    @BeforeEach
    void menuSetUp() {
        super.setUp();

        후라이드치킨 = 상품_등록_되어_있음(ProductFixture.후라이드치킨).as(ProductResponse.class);
        양념치킨 = 상품_등록_되어_있음(ProductFixture.양념치킨).as(ProductResponse.class);
        콜라 = 상품_등록_되어_있음(ProductFixture.콜라).as(ProductResponse.class);

        추천메뉴 = 메뉴_그룹_등록되어_있음(MenuGroupFixture.추천메뉴).as(MenuGroupResponse.class);

        후라이드치킨상품 = createMenuProduct(후라이드치킨.getId(), 1);
        양념치킨상품 = createMenuProduct(양념치킨.getId(), 1);
        콜라상품 = createMenuProduct(콜라.getId(), 1);

        두마리치킨 = createMenu("두마리치킨", new BigDecimal(3000), 추천메뉴.getId(), Arrays.asList(후라이드치킨상품, 양념치킨상품));
        양념세트 = createMenu("양념세트", new BigDecimal(2500), 추천메뉴.getId(), Arrays.asList(양념치킨상품, 콜라상품));
    }

    @DisplayName("메뉴를 등록한다.")
    @Test
    void crate() {
        ExtractableResponse<Response> response = 메뉴_생성_요청(두마리치킨);

        메뉴_생성됨(response);
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void getMenuList() {
        ExtractableResponse<Response> createResponse1 = 메뉴_등록되어_있음(두마리치킨);
        ExtractableResponse<Response> createResponse2 = 메뉴_등록되어_있음(양념세트);

        ExtractableResponse<Response> response = 메뉴_목록_조회_요청();

        메뉴_목록_조회됨(response);
        메뉴_목록_포함됨(response, Arrays.asList(createResponse1, createResponse2));
    }
}
