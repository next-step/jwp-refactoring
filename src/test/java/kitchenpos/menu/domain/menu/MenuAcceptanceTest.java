package kitchenpos.menu.domain.menu;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.domain.menugroup.MenuGroup;
import kitchenpos.menu.dto.*;
import kitchenpos.utils.AcceptanceTest;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static kitchenpos.menu.domain.fixture.MenuDomainFixture.메뉴_생성_요청;
import static kitchenpos.menu.domain.fixture.MenuGroupDomainFixture.*;
import static kitchenpos.menu.domain.fixture.ProductDomainFixture.*;
import static kitchenpos.utils.AcceptanceFixture.응답_BAD_REQUEST;
import static kitchenpos.utils.AcceptanceFixture.응답_CREATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("메뉴 관리")
class MenuAcceptanceTest extends AcceptanceTest {

    private void 메뉴_생성됨(ExtractableResponse<Response> actual, MenuRequest menuRequest) {
        MenuResponse response = actual.as(MenuResponse.class);
        assertAll(
                () -> assertThat(response.getName()).isEqualTo(menuRequest.getName()),
                () -> assertThat(response.getMenuPrice().getPrice()).isEqualTo(menuRequest.getPrice()),
                () -> assertThat(response.getMenuGroup().getId()).isEqualTo(menuRequest.getMenuGroupId()),
                () -> assertThat(response.getMenuProducts()).hasSize(2)
        );
    }

    @Nested
    @DisplayName("인수 테스트 - 메뉴 생성")
    class CreateMenu {
        @Test
        @DisplayName("성공")
        public void create() {
            // given
            MenuProductRequest 사이다_한개 = MenuProductRequest.of(사이다_생성됨().getId(), 1);
            MenuProductRequest 양념소스_두개 = MenuProductRequest.of(양념소스_생성됨().getId(), 2);
            MenuRequest 후라이드_치킨 = MenuRequest.of("후라이드 치킨", BigDecimal.valueOf(15000),
                    일인_세트_생성됨().getId(), Lists.newArrayList(사이다_한개, 양념소스_두개));

            // when
            ExtractableResponse<Response> actual = 메뉴_생성_요청(후라이드_치킨);

            // then
            응답_CREATE(actual);
            메뉴_생성됨(actual, 후라이드_치킨);
        }

        @Test
        @DisplayName("실패 - 메뉴명 없음")
        public void failNameEmpty() {
            // given
            MenuProductRequest 사이다_한개 = MenuProductRequest.of(사이다_생성됨().getId(), 1);
            MenuProductRequest 양념소스_두개 = MenuProductRequest.of(양념소스_생성됨().getId(), 2);
            MenuRequest 후라이드_치킨 = MenuRequest.of("", BigDecimal.valueOf(15000),
                    일인_세트_생성됨().getId(), Lists.newArrayList(사이다_한개, 양념소스_두개));

            // when
            ExtractableResponse<Response> actual = 메뉴_생성_요청(후라이드_치킨);

            // then
            응답_BAD_REQUEST(actual);
        }

        @Test
        @DisplayName("실패 - 메뉴 그룹 없음")
        public void failMenuGroupEmpty() {
            // given
            MenuProductRequest 사이다_한개 = MenuProductRequest.of(사이다_생성됨().getId(), 1);
            MenuProductRequest 양념소스_두개 = MenuProductRequest.of(양념소스_생성됨().getId(), 2);
            MenuRequest 후라이드_치킨 = MenuRequest.of("후라이드 치킨", BigDecimal.valueOf(15000),
                    null, Lists.newArrayList(사이다_한개, 양념소스_두개));

            // when
            ExtractableResponse<Response> actual = 메뉴_생성_요청(후라이드_치킨);

            // then
            응답_BAD_REQUEST(actual);
        }

        @Test
        @DisplayName("실패 - 잘못된 메뉴 가격 입력")
        public void failPriceIllegal() {
            // given
            MenuProductRequest 사이다_한개 = MenuProductRequest.of(사이다_생성됨().getId(), 1);
            MenuProductRequest 양념소스_두개 = MenuProductRequest.of(양념소스_생성됨().getId(), 2);
            MenuRequest 후라이드_치킨 = MenuRequest.of("후라이드 치킨", BigDecimal.valueOf(-1),
                    일인_세트_생성됨().getId(), Lists.newArrayList(사이다_한개, 양념소스_두개));

            // when
            ExtractableResponse<Response> actual = 메뉴_생성_요청(후라이드_치킨);

            // then
            응답_BAD_REQUEST(actual);
        }
    }
}
