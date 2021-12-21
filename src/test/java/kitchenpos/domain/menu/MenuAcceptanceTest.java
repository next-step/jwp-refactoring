package kitchenpos.domain.menu;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertSame;

@DisplayName("메뉴 관련 기능")
public class MenuAcceptanceTest extends AcceptanceTest {

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    private MenuGroup 추천메뉴_그룹;

    private Product 후라이드;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        추천메뉴_그룹 = menuGroupDao.save(new MenuGroup("추천메뉴"));
        후라이드 = productDao.save(new Product("후라이드", BigDecimal.valueOf(9500)));
    }

    @Test
    @DisplayName("메뉴를 등록할 수 있다.")
    void createMenu() {
        // given
        Menu 후라이드_두마리 = new Menu("후라이드+후라이드", BigDecimal.valueOf(19000), 추천메뉴_그룹.getId(), Arrays.asList(new MenuProduct(후라이드.getId(), 2L)));

        // when
        ExtractableResponse<Response> 메뉴_등록_요청_응답 = 메뉴_등록을_요청(후라이드_두마리);

        // then
        메뉴_등록됨(메뉴_등록_요청_응답);
    }

    private void 메뉴_등록됨(ExtractableResponse<Response> response) {
        Menu 등록된_메뉴 = response.as(Menu.class);
        assertAll(
                () -> assertThat(등록된_메뉴.getId()).isNotNull(),
                () -> assertThat(등록된_메뉴.getName()).isEqualTo("후라이드+후라이드"),
                () -> assertThat(등록된_메뉴.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(19000)),
                () -> assertThat(등록된_메뉴.getMenuGroupId()).isEqualTo(추천메뉴_그룹.getId()),
                () -> assertThat(등록된_메뉴.getMenuProducts()).extracting("menuId").contains(등록된_메뉴.getId())
        );
    }

    public ExtractableResponse<Response> 메뉴_등록을_요청(Menu menu) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menu)
                .when().post("/api/menus")
                .then().log().all()
                .extract();
    }
}
