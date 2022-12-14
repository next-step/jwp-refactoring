package kitchenpos.Acceptance;

import static kitchenpos.Acceptance.MenuGroupTestFixture.메뉴_그룹_생성_요청함;
import static kitchenpos.Acceptance.MenuTestFixture.메뉴_생성_요청함;
import static kitchenpos.Acceptance.MenuTestFixture.메뉴_생성됨;
import static kitchenpos.Acceptance.MenuTestFixture.메뉴_조회_요청_응답됨;
import static kitchenpos.Acceptance.MenuTestFixture.메뉴_조회_요청함;
import static kitchenpos.Acceptance.MenuTestFixture.메뉴_조회_포함됨;
import static kitchenpos.Acceptance.ProductTestFixture.상품_생성_요청함;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuAcceptanceTest extends AcceptanceTest {

    private Product 후라이드;
    private Product 콜라;
    private MenuProduct 후라이드메뉴상품;
    private MenuProduct 콜라메뉴상품;
    private MenuGroup 메뉴분류세트;
    private Menu 후라이드한마리;

    @BeforeEach
    public void setUp() {
        super.setUp();
        메뉴분류세트 = 메뉴_그룹_생성_요청함(new MenuGroup("메뉴분류세트")).as(MenuGroup.class);
        후라이드 = 상품_생성_요청함(new Product("후라이드", BigDecimal.valueOf(15000))).as(Product.class);
        콜라 = 상품_생성_요청함(new Product("콜라", BigDecimal.valueOf(1000))).as(Product.class);
        후라이드메뉴상품 = new MenuProduct(1L, null, 후라이드.getId(), 1L);
        콜라메뉴상품 = new MenuProduct(2L, null, 콜라.getId(), 1L);
        후라이드한마리 = new Menu(null, "후라이드한미리", BigDecimal.valueOf(15000), 메뉴분류세트.getId(),
                Arrays.asList(후라이드메뉴상품, 콜라메뉴상품));
    }

    @DisplayName("메뉴를 등록한다.")
    @Test
    void create() {
        //when
        ExtractableResponse<Response> response = 메뉴_생성_요청함(후라이드한마리);
        //then
        메뉴_생성됨(response);
    }

    @DisplayName("메뉴를 조회한다.")
    @Test
    void list() {
        //given
        ExtractableResponse<Response> 후라이드한마리_response = 메뉴_생성_요청함(후라이드한마리);
        //when
        ExtractableResponse<Response> response = 메뉴_조회_요청함();
        //then
        메뉴_조회_요청_응답됨(response);
        메뉴_조회_포함됨(response, Collections.singletonList(후라이드한마리_response));
    }
}
