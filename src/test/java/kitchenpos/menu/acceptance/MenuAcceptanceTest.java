package kitchenpos.menu.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.common.acceptance.AcceptanceTest;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.menu.acceptance.MenuAcceptanceStep.*;
import static kitchenpos.menugroup.acceptance.MenuGroupAcceptanceStep.등록된_메뉴_그룹;
import static kitchenpos.product.acceptance.ProductAcceptanceStep.등록된_상품;
import static kitchenpos.menugroup.fixture.MenuGroupTestFixture.메뉴그룹;
import static kitchenpos.menu.fixture.MenuProductTestFixture.*;
import static kitchenpos.menu.fixture.MenuTestFixture.메뉴세트요청;
import static kitchenpos.product.fixture.ProductTestFixture.*;
import static kitchenpos.product.fixture.ProductTestFixture.단무지요청;

@DisplayName("메뉴 관련 인수 테스트")
public class MenuAcceptanceTest extends AcceptanceTest {

    private MenuGroup 중국집1인메뉴세트그룹;
    private MenuProductRequest 짜장면메뉴상품요청;
    private MenuProductRequest 짬뽕메뉴상품요청;
    private MenuProductRequest 탕수육메뉴상품요청;
    private MenuProductRequest 단무지메뉴상품요청;
    private MenuRequest 짜장면_탕수육_1인_메뉴_세트;
    private MenuRequest 짬뽕_탕수육_1인_메뉴_세트;

    @BeforeEach
    public void setUp() {
        super.setUp();
        중국집1인메뉴세트그룹 = 등록된_메뉴_그룹(메뉴그룹("중국집1인메뉴세트그룹")).as(MenuGroup.class);
        Long 짜짱면상품ID = 등록된_상품(짜장면요청()).as(ProductResponse.class).getId();
        Long 짬뽕상품ID = 등록된_상품(짬뽕요청()).as(ProductResponse.class).getId();
        Long 탕수육상품ID = 등록된_상품(탕수육요청()).as(ProductResponse.class).getId();
        Long 단무지상품ID = 등록된_상품(단무지요청()).as(ProductResponse.class).getId();
        짜장면메뉴상품요청 = 짜장면메뉴상품요청(짜짱면상품ID);
        탕수육메뉴상품요청 = 탕수육메뉴상품요청(탕수육상품ID);
        짬뽕메뉴상품요청 = 짬뽕메뉴상품요청(짬뽕상품ID);
        단무지메뉴상품요청 = 단무지메뉴상품요청(단무지상품ID);
        짜장면_탕수육_1인_메뉴_세트 = 메뉴세트요청("짜장면_탕수육_1인_메뉴_세트", BigDecimal.valueOf(20000L),
                중국집1인메뉴세트그룹.getId(), Arrays.asList(짜장면메뉴상품요청, 탕수육메뉴상품요청, 단무지메뉴상품요청));
        짬뽕_탕수육_1인_메뉴_세트 = 메뉴세트요청("짬뽕_탕수육_1인_메뉴_세트", BigDecimal.valueOf(21000L),
                중국집1인메뉴세트그룹.getId(), Arrays.asList(짬뽕메뉴상품요청, 탕수육메뉴상품요청, 단무지메뉴상품요청));
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
