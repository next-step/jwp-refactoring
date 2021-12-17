package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.acceptance.step.MenuGroupAcceptStep;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.product.acceptance.step.ProductAcceptStep;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static kitchenpos.acceptance.step.MenuAcceptStep.메뉴_등록_요청;
import static kitchenpos.acceptance.step.MenuAcceptStep.메뉴_등록_확인;
import static kitchenpos.acceptance.step.MenuAcceptStep.메뉴_목록_조회_요청;
import static kitchenpos.acceptance.step.MenuAcceptStep.메뉴_목록_조회_확인;

@DisplayName("메뉴 인수테스트")
class MenuAcceptTest extends AcceptanceTest {
    private MenuGroup 추천메뉴;
    private ProductResponse 강정치킨;

    @BeforeEach
    void setup() {
        추천메뉴 = MenuGroupAcceptStep.메뉴_그룹이_등록되어_있음("추천메뉴");
        강정치킨 = ProductAcceptStep.상품이_등록되어_있음("강정치킨", BigDecimal.valueOf(17_000));
    }

    @DisplayName("메뉴를 관리한다")
    @Test
    void 메뉴를_관리한다() {
        // given
        MenuProduct 메뉴_상품 = new MenuProduct();
        메뉴_상품.setProductId(강정치킨.getId());
        메뉴_상품.setQuantity(2);

        Menu 메뉴_등록_요청_데이터 = new Menu();
        메뉴_등록_요청_데이터.setName("더블강정");
        메뉴_등록_요청_데이터.setPrice(BigDecimal.valueOf(32_000));
        메뉴_등록_요청_데이터.setMenuGroupId(추천메뉴.getId());
        메뉴_등록_요청_데이터.setMenuProducts(Collections.singletonList(메뉴_상품));

        // when
        ExtractableResponse<Response> 메뉴_등록_응답 = 메뉴_등록_요청(메뉴_등록_요청_데이터);

        // then
        Menu 등록된_메뉴 = 메뉴_등록_확인(메뉴_등록_응답, 메뉴_등록_요청_데이터);

        // when
        ExtractableResponse<Response> 메뉴_목록_조회_응답 = 메뉴_목록_조회_요청();

        // then
        메뉴_목록_조회_확인(메뉴_목록_조회_응답, 등록된_메뉴);
    }
}
