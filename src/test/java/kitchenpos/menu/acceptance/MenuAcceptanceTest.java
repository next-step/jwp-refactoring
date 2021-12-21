package kitchenpos.menu.acceptance;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.menu.MenuFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Collections;

import static kitchenpos.menu.acceptance.MenuGroupAcceptanceTest.메뉴_그룹_생성됨;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 인수 테스트")
public class MenuAcceptanceTest extends AcceptanceTest {

    @DisplayName("메뉴를 생성한다.")
    @Test
    void createMenu() {
        // given
        MenuGroup 튀김종류 = 메뉴_그룹_생성됨(MenuFactory.ofMenuGroup("튀김종류"));
        Menu 치킨세트 = MenuFactory.ofMenu("치킨세트", 튀김종류.getId(), 35000);
        치킨세트.setMenuProducts(Collections.singletonList(MenuFactory.ofMenuProduct(1L, 치킨세트.getId(), 1L, 35000)));

        // when
        ExtractableResponse<Response> response = 메뉴_생성_요청(치킨세트);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("상품을 목록을 조회한다.")
    @Test
    void findAllProduct() {
        // when
        ExtractableResponse<Response> response = 메뉴_목록_조회됨();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static ExtractableResponse<Response> 메뉴_생성_요청(Menu menu) {
        return ofRequest(Method.POST, "/api/menus", menu);
    }

    private ExtractableResponse<Response> 메뉴_목록_조회됨() {
        return ofRequest(Method.GET, "/api/menus");
    }

    public static Menu 메뉴_생성됨(Menu menu) {
        return 메뉴_생성_요청(menu)
                .body()
                .as(Menu.class);
    }
}
