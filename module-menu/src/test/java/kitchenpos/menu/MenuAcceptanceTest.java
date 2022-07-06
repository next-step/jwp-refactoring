package kitchenpos.menu;

import static kitchenpos.menu.MenuAcceptanceAPI.메뉴_생성_요청;
import static kitchenpos.menu.MenuAcceptanceAPI.메뉴_조회_요청;
import static kitchenpos.menu.MenuAcceptanceAPI.상품_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class MenuAcceptanceTest extends AcceptanceTest {

    MenuGroup 한마리메뉴;
    MenuProduct 양념치킨메뉴;

    @BeforeEach
    public void setUp() {
        super.setUp();

        한마리메뉴 = MenuGroupAcceptanceTest.메뉴_그룹_생성되어_있음("추천메뉴").as(MenuGroup.class);
        상품_생성_요청("양념치킨상품", new BigDecimal(18000));

        양념치킨메뉴 = new MenuProduct(1L, 1L);
    }

    @Test
    @DisplayName("메뉴를 생성한다")
    void 메뉴를_생성한다() {
        // when
        ExtractableResponse<Response> response = 메뉴_생성_요청("청양마요치킨", new BigDecimal(18000), 한마리메뉴.getId(), 양념치킨메뉴);

        // then
        메뉴_생성됨(response);
    }

    @Test
    @DisplayName("메뉴를 조회한다")
    void 메뉴를_조회한다() {
        // given
        메뉴_생성_요청("청양마요치킨", new BigDecimal(18000), 한마리메뉴.getId(), 양념치킨메뉴);

        // when
        ExtractableResponse<Response> response = 메뉴_조회_요청();

        // then
        assertThat(response.jsonPath().getList("name")).hasSize(1);
    }

    public static void 메뉴_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }
}
