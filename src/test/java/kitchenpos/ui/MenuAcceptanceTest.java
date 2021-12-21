package kitchenpos.ui;

import static kitchenpos.utils.AcceptanceTestUtil.get;
import static kitchenpos.utils.AcceptanceTestUtil.post;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class MenuAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @DisplayName("메뉴를 생성한다")
    @Test
    void createMenu() {
        // given
        MenuProduct menuProduct1 = MenuProduct.of(1L, 1L, 1);
        MenuProduct menuProduct2 = MenuProduct.of(2L, 2L, 1);
        List<MenuProduct> menuProducts = Lists.newArrayList(menuProduct1, menuProduct2);
        Menu menu = Menu.of("양념치킨", BigDecimal.valueOf(18000), 1L, menuProducts);

        // when
        ExtractableResponse<Response> 메뉴_생성_응답 = 메뉴_생성(menu);

        // then
        메뉴_생성됨(메뉴_생성_응답);
    }

    @DisplayName("메뉴 목록을 조회한다")
    @Test
    void readMenus() {
        // when
        ExtractableResponse<Response> 메뉴목록_응답 = 메뉴목록_조회();

        // then
        메뉴목록_조회됨(메뉴목록_응답);
    }

    private ExtractableResponse<Response> 메뉴_생성(Menu menu) {
        return post("/api/menus", menu);
    }

    private void 메뉴_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
        Menu menu = response.as(Menu.class);
        assertThat(menu.getName()).isEqualTo("양념치킨");
        assertThat(menu.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(18000));
    }

    private ExtractableResponse<Response> 메뉴목록_조회() {
        return get("/api/menus");
    }

    private void 메뉴목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<Menu> menus = Lists.newArrayList(response.as(Menu[].class));
        assertThat(menus).hasSize(6);
        assertThat(menus).extracting(Menu::getName)
            .contains("후라이드치킨", "양념치킨", "반반치킨", "통구이", "간장치킨", "순살치킨");
    }


}
