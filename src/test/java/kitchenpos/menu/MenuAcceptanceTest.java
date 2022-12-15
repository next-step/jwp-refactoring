package kitchenpos.menu;

import static kitchenpos.menu.MenuFixture.메뉴_등록;
import static kitchenpos.menu.MenuFixture.메뉴_목록_조회;
import static kitchenpos.menu.MenuGroupFixture.메뉴_그룹_등록;
import static kitchenpos.menu.MenuGroupFixture.메뉴_그룹_목록_조회;
import static kitchenpos.product.ProductFixture.상품_등록;
import static kitchenpos.product.ProductFixture.상품_목록_조회;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class MenuAcceptanceTest extends AcceptanceTest {

    private MenuGroup 추천메뉴;
    private Product 강정치킨;
    private MenuProduct 더블강정치킨;

    /*
    Feature: 메뉴를 관리 합니다.
      Background
        Given 메뉴 그룹 등록 되어 있음
        And 상품 등록되어 있음
        And 메뉴 상품이 있음
      Scenario: 메뉴를 추가 한다.
        When 메뉴 등록
        Then 메뉴 등록됨
        When 메뉴 목록 조회
        Then 메뉴 목록 조회됨
    Scenario: 메뉴 추가에 실패 한다.
        When 잘못된 가격으로 메뉴 등록
        Then 메뉴 등록 실패함
        When 존재하지 않는 메뉴 그룹에 메뉴 등록
        Then 메뉴 등록 실패함
        When 존재하지 않는 상품에 메뉴 등록
        Then 메뉴 등록 실패함
        When 상품 수량 없이 메뉴 등록
        Then 메뉴 등록 실패함
     */
    @BeforeEach
    public void setUp() throws SQLException {
        super.setUp();
        메뉴_그룹_등록("추천 메뉴");
        추천메뉴 = 메뉴_그룹_목록_조회().jsonPath().getList(".", MenuGroup.class).get(0);
        상품_등록("강정치킨", new BigDecimal(17_000));
        강정치킨 = 상품_목록_조회().jsonPath().getList(".", Product.class).get(0);
        더블강정치킨 = new MenuProduct();
        더블강정치킨.setProductId(강정치킨.getId());
        더블강정치킨.setQuantity(2L);
    }

    @Test
    void 메뉴를_추가() {
        //when
        ExtractableResponse<Response> createResponse = 메뉴_등록("강정치킨+강정치킨", new BigDecimal(19_000),
            추천메뉴.getId(), Collections.singletonList(더블강정치킨));

        //then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        //when
        List<Menu> menus = 메뉴_목록_조회().jsonPath().getList(".", Menu.class);

        //then
        assertThat(menus)
            .hasSize(1)
            .extracting(
                Menu::getName,
                Menu::getMenuGroupId,
                Menu::getPrice,
                menu -> menu.getMenuProducts().get(0).getProductId(),
                menu -> menu.getMenuProducts().get(0).getQuantity()
            )
            .containsExactly(
                tuple(
                    "강정치킨+강정치킨",
                    추천메뉴.getId(),
                    new BigDecimal("19000.0"),
                    강정치킨.getId(),
                    2L
                )
            );
    }

    @Test
    void 메뉴_추가에_실패_잘못된_가격() {
        //when
        ExtractableResponse<Response> noPrice = 메뉴_등록("강정치킨+강정치킨", null, 추천메뉴.getId(),
            Collections.singletonList(더블강정치킨));
        //then
        assertThat(noPrice.statusCode()).isEqualTo(INTERNAL_SERVER_ERROR.value());

        //when
        ExtractableResponse<Response> negativePrice = 메뉴_등록("강정치킨+강정치킨", new BigDecimal(-1),
            추천메뉴.getId(),
            Collections.singletonList(더블강정치킨));
        //then
        assertThat(negativePrice.statusCode()).isEqualTo(INTERNAL_SERVER_ERROR.value());
    }

    @Test
    void 메뉴_추가에_실패_존재_하지_않는_메뉴_그룹() {
        //when
        ExtractableResponse<Response> nullMenuGroup = 메뉴_등록("강정치킨+강정치킨", new BigDecimal(19_000),
            null, Collections.singletonList(더블강정치킨));
        //then
        assertThat(nullMenuGroup.statusCode()).isEqualTo(INTERNAL_SERVER_ERROR.value());

        //when
        ExtractableResponse<Response> notExistMenuGroup = 메뉴_등록("강정치킨+강정치킨", new BigDecimal(19_000),
            -1L, Collections.singletonList(더블강정치킨));
        //then
        assertThat(notExistMenuGroup.statusCode()).isEqualTo(INTERNAL_SERVER_ERROR.value());
    }

    /*
    Scenario: 메뉴 추가에 실패 한다.
        When 존재하지 않는 상품에 메뉴 등록
        Then 메뉴 등록 실패함
        When 상품 수량 없이 메뉴 등록
        Then 메뉴 등록 실패함
     */
    @Test
    void 메뉴_추가에_실패_존재_하지_않는_상품() {
        //given
        MenuProduct 존재하지않는상품 = new MenuProduct();
        존재하지않는상품.setProductId(-1L);
        존재하지않는상품.setQuantity(1);

        //when
        ExtractableResponse<Response> noExistProductResponse = 메뉴_등록("강정치킨+강정치킨",
            new BigDecimal(19_000),
            추천메뉴.getId(), Arrays.asList(더블강정치킨, 존재하지않는상품));
        //then
        assertThat(noExistProductResponse.statusCode()).isEqualTo(INTERNAL_SERVER_ERROR.value());
    }

    @Test
    void 메뉴_추가에_실패_수량_없는_메뉴_상품() {
        //given
        MenuProduct 수량없는상품 = new MenuProduct();
        수량없는상품.setProductId(강정치킨.getId());
        수량없는상품.setQuantity(0);

        //when
        ExtractableResponse<Response> noExistProductResponse = 메뉴_등록("강정치킨+강정치킨",
            new BigDecimal(19_000),
            추천메뉴.getId(), Collections.singletonList(수량없는상품));
        //then
        assertThat(noExistProductResponse.statusCode()).isEqualTo(INTERNAL_SERVER_ERROR.value());
    }
}
