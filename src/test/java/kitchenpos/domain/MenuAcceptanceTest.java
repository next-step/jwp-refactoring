package kitchenpos.domain;

import static kitchenpos.domain.MenuAcceptanceTestMethod.메뉴_등록_요청;
import static kitchenpos.domain.MenuAcceptanceTestMethod.메뉴_등록되어_있음;
import static kitchenpos.domain.MenuAcceptanceTestMethod.메뉴_등록됨;
import static kitchenpos.domain.MenuAcceptanceTestMethod.메뉴_목록_응답됨;
import static kitchenpos.domain.MenuAcceptanceTestMethod.메뉴_목록_조회_요청;
import static kitchenpos.domain.MenuAcceptanceTestMethod.메뉴_목록_포함됨;
import static kitchenpos.domain.MenuGroupAcceptanceTestMethod.메뉴_그룹_등록되어_있음;
import static kitchenpos.domain.ProductAcceptanceTestMethod.상품_등록되어_있음;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import kitchenpos.AcceptanceTest;
import kitchenpos.application.fixture.MenuFixtureFactory;
import kitchenpos.application.fixture.MenuProductFixtureFactory;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 관련 기능 인수테스트")
class MenuAcceptanceTest extends AcceptanceTest {

    private MenuGroup 초밥_메뉴그룹;
    private Product 우아한_초밥_1;
    private Product 우아한_초밥_2;
    private MenuProduct A_우아한_초밥_1;
    private MenuProduct B_우아한_초밥_1;
    private Menu A메뉴;
    private Menu B메뉴;

    @BeforeEach
    public void setUp() {
        super.setUp();

        초밥_메뉴그룹 = 메뉴_그룹_등록되어_있음(MenuGroup.from("초밥_메뉴그룹")).as(MenuGroup.class);
        우아한_초밥_1 = 상품_등록되어_있음(Product.of("우아한_초밥_1", BigDecimal.valueOf(10_000))).as(Product.class);
        우아한_초밥_2 = 상품_등록되어_있음(Product.of("우아한_초밥_2", BigDecimal.valueOf(20_000))).as(Product.class);

        A메뉴 = MenuFixtureFactory.create("A", BigDecimal.valueOf(9_000), 초밥_메뉴그룹.getId());
        B메뉴 = MenuFixtureFactory.create("B", BigDecimal.valueOf(10_000), 초밥_메뉴그룹.getId());

        A_우아한_초밥_1 = MenuProductFixtureFactory.create(1L, A메뉴.getId(), 우아한_초밥_1.getId(), 1);
        B_우아한_초밥_1 = MenuProductFixtureFactory.create(2L, B메뉴.getId(), 우아한_초밥_2.getId(), 2);

        A메뉴.setMenuProducts(Lists.newArrayList(A_우아한_초밥_1));
        B메뉴.setMenuProducts(Lists.newArrayList(B_우아한_초밥_1));

    }

    @DisplayName("메뉴를 등록할 수 있다.")
    @Test
    void create01() {
        // when
        ExtractableResponse<Response> response = 메뉴_등록_요청(A메뉴);

        // then
        메뉴_등록됨(response);
    }

    @DisplayName("메뉴 목록을 조회할 수 있다.")
    @Test
    void find01() {
        // given
        ExtractableResponse<Response> createdResponse1 = 메뉴_등록되어_있음(A메뉴);
        ExtractableResponse<Response> createdResponse2 = 메뉴_등록되어_있음(B메뉴);

        // when
        ExtractableResponse<Response> response = 메뉴_목록_조회_요청();

        // then
        메뉴_목록_응답됨(response);
        메뉴_목록_포함됨(response, Lists.newArrayList(createdResponse1, createdResponse2));
    }
}