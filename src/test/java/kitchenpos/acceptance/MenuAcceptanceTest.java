package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.acceptance.MenuAcceptanceStep.*;
import static kitchenpos.acceptance.MenuGroupAcceptanceStep.등록된_메뉴_그룹;
import static kitchenpos.acceptance.ProductAcceptanceStep.등록된_상품;
import static kitchenpos.fixture.MenuGroupTestFixture.createMenuGroup;
import static kitchenpos.fixture.MenuProductTestFixture.createMenuProduct;
import static kitchenpos.fixture.MenuTestFixture.createMenu;
import static kitchenpos.fixture.ProductTestFixture.createProduct;

@DisplayName("메뉴 관련 인수 테스트")
public class MenuAcceptanceTest extends AcceptanceTest {

    private ProductResponse 짜장면;
    private ProductResponse 짬뽕;
    private ProductResponse 탕수육;
    private ProductResponse 단무지;
    private MenuGroup 중국집_1인_메뉴_세트;
    private MenuProduct 짜장면메뉴상품;
    private MenuProduct 짬뽕메뉴상품;
    private MenuProduct 탕수육메뉴상품;
    private MenuProduct 단무지메뉴상품;
    private Menu 짜장면_탕수육_1인_메뉴_세트;
    private Menu 짬뽕_탕수육_1인_메뉴_세트;

    @BeforeEach
    public void setUp() {
        super.setUp();
        중국집_1인_메뉴_세트 = 등록된_메뉴_그룹(createMenuGroup("중국집_1인_메뉴_세트")).as(MenuGroup.class);
        짜장면 = 등록된_상품(createProduct("짜장면", BigDecimal.valueOf(8000L))).as(ProductResponse.class);
        짬뽕 = 등록된_상품(createProduct("짬뽕", BigDecimal.valueOf(9000L))).as(ProductResponse.class);
        탕수육 = 등록된_상품(createProduct("탕수육", BigDecimal.valueOf(12000L))).as(ProductResponse.class);
        단무지 = 등록된_상품(createProduct("단무지", BigDecimal.valueOf(0L))).as(ProductResponse.class);
        짜장면메뉴상품 = createMenuProduct(1L, null, 짜장면.getId(), 1L);
        짬뽕메뉴상품 = createMenuProduct(2L, null, 짬뽕.getId(), 1L);
        탕수육메뉴상품 = createMenuProduct(3L, null, 탕수육.getId(), 1L);
        단무지메뉴상품 = createMenuProduct(4L, null, 단무지.getId(), 1L);
        짜장면_탕수육_1인_메뉴_세트 = createMenu("짜장면_탕수육_1인_메뉴_세트", BigDecimal.valueOf(20000L),
                중국집_1인_메뉴_세트.getId(), Arrays.asList(짜장면메뉴상품, 탕수육메뉴상품, 단무지메뉴상품));
        짬뽕_탕수육_1인_메뉴_세트 = createMenu("짬뽕_탕수육_1인_메뉴_세트", BigDecimal.valueOf(21000L),
                중국집_1인_메뉴_세트.getId(), Arrays.asList(짬뽕메뉴상품, 탕수육메뉴상품, 단무지메뉴상품));
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    void create() {
        // when
        ExtractableResponse<Response> response = 메뉴_생성_요청(짜장면_탕수육_1인_메뉴_세트);

        // then
        메뉴_생성됨(response);
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void list() {
        // given
        List<ExtractableResponse<Response>> 등록된_메뉴_목록 =
                Arrays.asList(등록된_메뉴(짜장면_탕수육_1인_메뉴_세트), 등록된_메뉴(짬뽕_탕수육_1인_메뉴_세트));

        // when
        ExtractableResponse<Response> response = 메뉴_목록_조회_요청();

        // then
        메뉴_목록_응답됨(response);
        메뉴_목록_포함됨(response, 등록된_메뉴_목록);
    }
}
