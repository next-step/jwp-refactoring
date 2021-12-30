package kitchenpos.ui;

import static kitchenpos.ui.MenuGroupAcceptanceTest.메뉴그룹_생성;
import static kitchenpos.ui.ProductAcceptanceTest.상품_생성;
import static kitchenpos.utils.AcceptanceTestUtil.get;
import static kitchenpos.utils.AcceptanceTestUtil.post;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.menu.dto.MenuGroupRequest;
import kitchenpos.domain.menu.dto.MenuGroupResponse;
import kitchenpos.domain.menu.dto.MenuProductRequest;
import kitchenpos.domain.menu.dto.MenuRequest;
import kitchenpos.domain.menu.dto.MenuResponse;
import kitchenpos.domain.product.dto.ProductRequest;
import kitchenpos.domain.product.dto.ProductResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class MenuAcceptanceTest extends AcceptanceTest {

    private MenuGroupResponse 추천메뉴;
    private ProductResponse 후라이드치킨;
    private ProductResponse 양념치킨;
    private MenuProductRequest menuProduct1;
    private MenuProductRequest menuProduct2;

    @BeforeEach
    public void setUp() {
        super.setUp();
        후라이드치킨 = 상품_생성(new ProductRequest("후라이드치킨", 17000)).as(ProductResponse.class);
        양념치킨 = 상품_생성(new ProductRequest("양념치킨", 18000)).as(ProductResponse.class);
        menuProduct1 = new MenuProductRequest(후라이드치킨.getId(), 1);
        menuProduct2 = new MenuProductRequest(양념치킨.getId(), 1);
        추천메뉴 = 메뉴그룹_생성(new MenuGroupRequest("추천메뉴")).as(MenuGroupResponse.class);
    }

    @DisplayName("메뉴를 생성한다")
    @Test
    void createMenu() {
        // given
        List<MenuProductRequest> menuProducts = Lists.newArrayList(menuProduct1, menuProduct2);
        MenuRequest menu = new MenuRequest("후라이드+양념치킨", 34000, 추천메뉴.getId(), menuProducts);

        // when
        ExtractableResponse<Response> 메뉴_생성_응답 = 메뉴_생성(menu);

        // then
        메뉴_생성됨(메뉴_생성_응답);
    }

    @DisplayName("메뉴 목록을 조회한다")
    @Test
    void readMenus() {
        // given
        List<MenuProductRequest> menuProductRequests1 = Lists.newArrayList(menuProduct1);
        List<MenuProductRequest> menuProductRequests2 = Lists.newArrayList(menuProduct2);
        MenuRequest 후라이드 = new MenuRequest("후라이드치킨", 17000, 추천메뉴.getId(), menuProductRequests1);
        MenuRequest 양념 = new MenuRequest("양념치킨", 18000, 추천메뉴.getId(), menuProductRequests2);
        메뉴_생성(후라이드);
        메뉴_생성(양념);

        // when
        ExtractableResponse<Response> 메뉴목록_응답 = 메뉴목록_조회();

        // then
        메뉴목록_조회됨(메뉴목록_응답);
    }

    public static ExtractableResponse<Response> 메뉴_생성(MenuRequest menu) {
        return post("/api/menus", menu);
    }

    private void 메뉴_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
        MenuResponse menu = response.as(MenuResponse.class);
        assertThat(menu.getName()).isEqualTo("후라이드+양념치킨");
        assertThat(menu.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(34000));
    }

    private ExtractableResponse<Response> 메뉴목록_조회() {
        return get("/api/menus");
    }

    private void 메뉴목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<MenuResponse> menus = Lists.newArrayList(response.as(MenuResponse[].class));
        assertThat(menus).hasSize(2);
        assertThat(menus).extracting(MenuResponse::getName)
            .contains("후라이드치킨", "양념치킨");
    }


}
