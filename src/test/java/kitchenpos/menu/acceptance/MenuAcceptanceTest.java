package kitchenpos.menu.acceptance;


import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.domain.MenuProduct;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static kitchenpos.menu.fixture.MenuTestFixture.*;
import static kitchenpos.menu.fixture.MenuGroupTestFixture.메뉴그룹_생성_요청;
import static kitchenpos.menu.fixture.MenuGroupTestFixture.메뉴그룹_생성됨;
import static kitchenpos.menu.fixture.ProductTestFixture.상품_생성_요청;
import static kitchenpos.menu.fixture.ProductTestFixture.상품_생성됨;

@DisplayName("메뉴 관련 인수 테스트")
public class MenuAcceptanceTest extends AcceptanceTest {

    /**
     * Given : 메뉴그룹과 상품이 생성되어 있다.
     * When : 메뉴 생성을 요청하면
     * Then : 메뉴가 생성된다.
     */
    @DisplayName("메뉴 생성 인수 테스트")
    @Test
    void createMenu() {
        // given
        Long productId = 상품_생성_요청("순살치킨", 9_000).jsonPath().getLong("id");
        Long menuGroupId = 메뉴그룹_생성_요청("세마리치킨").jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = 메뉴_생성_요청("순살세마리", new BigDecimal(27_000), menuGroupId, Lists.newArrayList(new MenuProduct(productId, 3)));

        // then
        메뉴_생성됨(response);
    }

    /**
     * Given : 메뉴그룹과 상품이 생성되어 있다.
     * When : 메뉴 가격이 0 미만인 경우 생성을 요청하면
     * Then : 메뉴가 생성이 실패한다
     */
    @DisplayName("메뉴 생성 실패(가격미달) 인수 테스트")
    @Test
    void createMenuFailed1() {
        // given
        Long productId = 상품_생성_요청("순살치킨", 9_000).jsonPath().getLong("id");
        Long menuGroupId = 메뉴그룹_생성_요청("세마리치킨").jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = 메뉴_생성_요청("순살세마리", new BigDecimal(-1), menuGroupId, Lists.newArrayList(new MenuProduct(productId, 3)));

        // then
        메뉴_생성_실패됨(response);
    }

    /**
     * Given : 메뉴그룹과 상품이 생성되어 있다.
     * When : 메뉴 가격이 구성되는 상품 가격의 총합보다 더 크면,
     * Then : 메뉴가 생성이 실패한다
     */
    @DisplayName("메뉴 생성 실패(상품가격 총합 초과) 인수 테스트")
    @Test
    void createMenuFailed2() {
        // given
        Long productId = 상품_생성_요청("순살치킨", 9_000).jsonPath().getLong("id");
        Long menuGroupId = 메뉴그룹_생성_요청("세마리치킨").jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = 메뉴_생성_요청("순살세마리", new BigDecimal(27_001), menuGroupId, Lists.newArrayList(new MenuProduct(productId, 3)));

        // then
        메뉴_생성_실패됨(response);
    }

    /**
     * Given : 메뉴가 생성되어 있다.
     * When : 메뉴 조회를 요청하면,
     * Then : 메뉴 목록을 응답한다.
     */
    @DisplayName("메뉴 조회 인수 테스트")
    @Test
    void findMenus() {
        // given
        Long productId = 상품_생성_요청("순살치킨", 9_000).jsonPath().getLong("id");
        Long menuGroupId = 메뉴그룹_생성_요청("세마리치킨").jsonPath().getLong("id");
        메뉴_생성_요청("순살세마리", new BigDecimal(27_000), menuGroupId, Lists.newArrayList(new MenuProduct(productId, 3)));

        // when
        ExtractableResponse<Response> response = 메뉴_조회_요청();

        // then
        메뉴_조회됨(response);
    }

    /**
     * Scenario : 메뉴를 생성
     * Given : 상품이 등록되어 있음
     * And : 메뉴그룹이 등록되어 있음
     * When : 메뉴를 생성한다.
     * Then : 메뉴가 생성된다.
     * And : 메뉴 목록을 조회한다.
     * And : 메뉴 목록이 조회된다.
     */
    @DisplayName("메뉴생성 관련 통합 인수테스트")
    @Test
    void menuIntegrationTest() {
        // given
        ExtractableResponse<Response> createProductResponse = 상품_생성_요청("순살치킨", 9_000);
        Long productId = createProductResponse.jsonPath().getLong("id");
        상품_생성됨(createProductResponse);

        // and
        ExtractableResponse<Response> createMenuGroupResponse = 메뉴그룹_생성_요청("세마리치킨");
        Long menuGroupId = createMenuGroupResponse.jsonPath().getLong("id");
        메뉴그룹_생성됨(createMenuGroupResponse);

        // when
        ExtractableResponse<Response> createMenuResponse = 메뉴_생성_요청("순살세마리", new BigDecimal(27_000), menuGroupId, Lists.newArrayList(new MenuProduct(productId, 3)));
        // and
        메뉴_생성됨(createMenuResponse);
        // and
        ExtractableResponse<Response> menuListResponse = 메뉴_조회_요청();
        // and
        메뉴_조회됨(menuListResponse);
    }
}
