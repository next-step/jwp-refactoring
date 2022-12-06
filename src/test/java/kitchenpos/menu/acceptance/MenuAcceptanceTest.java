package kitchenpos.menu.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("메뉴 관련 기능 인수 테스트")
public class MenuAcceptanceTest extends AcceptanceTest {

    private MenuGroupResponse 양식;
    private ProductResponse 스테이크;
    private ProductResponse 스파게티;
    private ProductResponse 에이드;
    private HashMap<Long, Long> quantityOfProducts;

    @BeforeEach
    public void setUp() {
        super.setUp();

        양식 = MenuGroupAcceptance.create_menu_group("양식").as(MenuGroupResponse.class);
        스테이크 = ProductAcceptance.create_product("스테이크", new BigDecimal(25000)).as(ProductResponse.class);
        스파게티 = ProductAcceptance.create_product("스파게티", new BigDecimal(18000)).as(ProductResponse.class);
        에이드 = ProductAcceptance.create_product("에이드", new BigDecimal(3500)).as(ProductResponse.class);

        quantityOfProducts = new HashMap<>();
        quantityOfProducts.put(스테이크.getId(), 1L);
        quantityOfProducts.put(스파게티.getId(), 1L);
        quantityOfProducts.put(에이드.getId(), 2L);
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
                MenuAcceptance.create_menu("양식 세트", new BigDecimal(50000), 양식.getId(), quantityOfProducts);

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
                MenuAcceptance.create_menu("양식 세트", new BigDecimal(50000), null, quantityOfProducts);

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
        quantityOfProducts.put(-1L, 1L);

        // when
        ExtractableResponse<Response> response =
                MenuAcceptance.create_menu("양식 세트", new BigDecimal(50000), 양식.getId(), quantityOfProducts);

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
                MenuAcceptance.create_menu(null, new BigDecimal(50000), 양식.getId(), quantityOfProducts);

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
                MenuAcceptance.create_menu("양식 세트", null, 양식.getId(), quantityOfProducts);

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
                MenuAcceptance.create_menu("양식 세트", new BigDecimal(-50000), 양식.getId(), quantityOfProducts);

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
                MenuAcceptance.create_menu("양식 세트", new BigDecimal(60000), 양식.getId(), quantityOfProducts);

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
        MenuAcceptance.create_menu("양식 세트", new BigDecimal(50000), 양식.getId(), quantityOfProducts);

        // when
        ExtractableResponse<Response> response = MenuAcceptance.menu_list_has_been_queried();

        // then
        assertThat(response.jsonPath().getList(".", MenuResponse.class)).hasSize(1);
    }
}
