package kitchenpos.menu.ui;

import static kitchenpos.common.AcceptanceFixture.*;
import static kitchenpos.fixture.MenuAcceptanceFixture.*;
import static kitchenpos.fixture.MenuGroupAcceptanceFixture.*;
import static kitchenpos.fixture.ProductAcceptanceFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.math.*;

import org.assertj.core.util.*;
import org.junit.jupiter.api.*;

import io.restassured.response.*;
import kitchenpos.common.*;
import kitchenpos.menu.dto.*;

@DisplayName("메뉴 관련(인수 테스트)")
class MenuAcceptanceTest extends AcceptanceTest {
    private MenuProductRequest 후라이드치킨_한개;
    private MenuProductRequest 양념치킨_두개;
    private MenuRequest 치킨세트;

    @BeforeEach
    public void setUp() {
        super.setUp();
        후라이드치킨_한개 = MenuProductRequest.of(후라이드치킨_생성됨().getId(), 1);
        양념치킨_두개 = MenuProductRequest.of(양념치킨_생성됨().getId(), 2);
        치킨세트 = MenuRequest.of("치킨세트", BigDecimal.valueOf(30000), 한마리_세트_생성됨().getId(), Lists.newArrayList(후라이드치킨_한개, 양념치킨_두개));
    }

    @DisplayName("메뉴 생성하기")
    @Test
    void createTest() {
        ExtractableResponse<Response> 메뉴_생성됨 = 메뉴_생성_요청(치킨세트);
        CREATE_응답_잘_받음(메뉴_생성됨);
        메뉴_생성_확인(메뉴_생성됨, 치킨세트);
    }

    @DisplayName("메뉴명 없으면 실패함")
    @Test
    void failTest1() {
        치킨세트 = MenuRequest.of("", BigDecimal.valueOf(30000), 한마리_세트_생성됨().getId(), Lists.newArrayList(후라이드치킨_한개, 양념치킨_두개));
        ExtractableResponse<Response> 메뉴_생성됨 = 메뉴_생성_요청(치킨세트);
        BAD_REQUEST_응답_잘_받았음(메뉴_생성됨);
    }

    @DisplayName("메뉴그룹 없으면 실패함")
    @Test
    void failTest2() {
        치킨세트 = MenuRequest.of("후라이드 치킨", BigDecimal.valueOf(30000),null, Lists.newArrayList(후라이드치킨_한개, 양념치킨_두개));
        ExtractableResponse<Response> 메뉴_생성됨 = 메뉴_생성_요청(치킨세트);
        BAD_REQUEST_응답_잘_받았음(메뉴_생성됨);
    }

    @DisplayName("메뉴가격이 유효하지 않으면 실패함")
    @Test
    void failTest3() {
        치킨세트 = MenuRequest.of("후라이드 치킨", BigDecimal.valueOf(-1),
                한마리_세트_생성됨().getId(), Lists.newArrayList(후라이드치킨_한개, 양념치킨_두개));
        ExtractableResponse<Response> 메뉴_생성됨 = 메뉴_생성_요청(치킨세트);
        BAD_REQUEST_응답_잘_받았음(메뉴_생성됨);
    }

    private void 메뉴_생성_확인(ExtractableResponse<Response> actual, MenuRequest menuRequest) {
        MenuResponse response = actual.as(MenuResponse.class);
        assertThat(response.getName()).isEqualTo(menuRequest.getName());
        assertThat(response.getPrice()).isEqualTo(menuRequest.getPrice());
        assertThat(response.getMenuGroup().getId()).isEqualTo(menuRequest.getMenuGroupId());
        assertThat(response.getMenuProducts()).hasSize(2);
    }
}
