package kitchenpos.menu.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("메뉴 관련 기능 인수 테스트")
public class MenuAcceptanceTest extends AcceptanceTest {
    private MenuGroupResponse 양식;
    private ProductResponse 스테이크;
    private ProductResponse 스파게티;
    private ProductResponse 에이드;
    private List<MenuProductRequest> menuProducts;

    @BeforeEach
    public void setUp() {
        super.setUp();

        양식 = MenuGroupAcceptance.create_menu_group("양식").as(MenuGroupResponse.class);
        스테이크 = ProductAcceptance.create_product("스테이크", 25_000).as(ProductResponse.class);
        스파게티 = ProductAcceptance.create_product("스파게티", 18_000).as(ProductResponse.class);
        에이드 = ProductAcceptance.create_product("에이드", 3_500).as(ProductResponse.class);

        menuProducts = Arrays.asList(new MenuProductRequest(스테이크.getId(), 1L),
                new MenuProductRequest(스파게티.getId(), 1L),
                new MenuProductRequest(에이드.getId(), 2L));
    }

    /**
     * Given 메뉴 그룹을 생성하고
     * And 상품을 생성하고
     * When 메뉴 생성을 요청하면
     * Then 메뉴가 생성된다.
     */
    @DisplayName("메뉴를 생성한다.")
    @Test
    void createMenu() {
        // when
        ExtractableResponse<Response> response =
                MenuAcceptance.create_menu("양식 세트", 50_000, 양식.getId(), menuProducts);

        // then
        assertEquals(HttpStatus.CREATED.value(), response.statusCode());
    }

    /**
     * Given 상품을 생성하고
     * When 메뉴 그룹이 존재하지 않는 메뉴 생성을 요청하면
     * Then 메뉴를 생성할 수 없다.
     */
    @DisplayName("메뉴 그룹이 존재하지 않는 메뉴를 생성한다.")
    @Test
    void createMenuWithNullMenuGroup() {
        // when
        ExtractableResponse<Response> response =
                MenuAcceptance.create_menu("양식 세트", 50_000, null, menuProducts);

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * Given 메뉴 그룹을 생성하고
     * When 존재하지 않는 상품을 포함하는 메뉴 생성을 요청하면
     * Then 메뉴를 생성할 수 없다.
     */
    @DisplayName("존재하지 않는 상품을 포함하는 메뉴를 생성한다.")
    @Test
    void createMenuWithNullProduct() {
        // given
        menuProducts = new ArrayList<>(menuProducts);
        menuProducts.add(new MenuProductRequest(-1L, 1L));

        // when
        ExtractableResponse<Response> response =
                MenuAcceptance.create_menu("양식 세트", 50_000, 양식.getId(), menuProducts);

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * Given 메뉴 그룹을 생성하고
     * And 상품을 생성하고
     * When 메뉴의 이름을 빈 값으로 하여 메뉴 생성을 요청하면
     * Then 메뉴를 생성할 수 없다.
     */
    @DisplayName("메뉴의 이름을 빈 값으로 하여 메뉴를 생성한다.")
    @Test
    void createMenuWithNullName() {
        // when
        ExtractableResponse<Response> response =
                MenuAcceptance.create_menu(null, 50_000, 양식.getId(), menuProducts);

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * Given 메뉴 그룹을 생성하고
     * And 상품을 생성하고
     * When 메뉴의 가격을 빈 값으로 하여 메뉴 생성을 요청하면
     * Then 메뉴를 생성할 수 없다.
     */
    @DisplayName("메뉴의 가격을 빈 값으로 하여 메뉴를 생성한다.")
    @Test
    void createMenuWithNullPrice() {
        // when
        ExtractableResponse<Response> response =
                MenuAcceptance.create_menu("양식 세트", null, 양식.getId(), menuProducts);

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * Given 메뉴 그룹을 생성하고
     * And 상품을 생성하고
     * When 메뉴의 가격을 음수 값을 하여 메뉴 생성을 요청하면
     * Then 메뉴를 생성할 수 없다.
     */
    @DisplayName("메뉴의 가격을 음수 값으로 하여 메뉴를 생성한다.")
    @Test
    void createMenuWithNegativePrice() {
        // when
        ExtractableResponse<Response> response =
                MenuAcceptance.create_menu("양식 세트", -50_000, 양식.getId(), menuProducts);

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * Given 메뉴 그룹을 생성하고
     * And 상품을 생성하고
     * When 메뉴 상품의 총 금액의 합보다 큰 값을 메뉴의 가격으로 하여 메뉴 생성을 요청하면
     * Then 메뉴를 생성할 수 없다.
     */
    @DisplayName("메뉴 상품의 총 금액의 합보다 큰 값을 메뉴의 가격으로 하여 메뉴를 생성한다.")
    @Test
    void createMenuWithInvalidPrice() {
        // when
        ExtractableResponse<Response> response =
                MenuAcceptance.create_menu("양식 세트", 60_000, 양식.getId(), menuProducts);

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * Given 메뉴 그룹을 생성하고
     * And 상품을 생성하고
     * When 메뉴 목록을 조회하면
     * Then 메뉴 목록이 조회된다.
     */
    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void list() {
        // given
        MenuAcceptance.create_menu("양식 세트", 50_000, 양식.getId(), menuProducts);

        // when
        ExtractableResponse<Response> response = MenuAcceptance.menu_list_has_been_queried();

        // then
        assertThat(response.jsonPath().getList(".", MenuResponse.class)).hasSize(1);
    }
}
