package kitchenpos.menu.ui;

import io.restassured.RestAssured;
import kitchenpos.menu.MenuUiTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuName;
import kitchenpos.menu.domain.MenuProductBag;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.menu.domain.MenuGroupTestFixture.메뉴_그룹_추천_메뉴;
import static kitchenpos.menu.domain.MenuProductTestFixture.메뉴_상품;
import static kitchenpos.menu.domain.MenuTest.메뉴;
import static kitchenpos.product.domain.ProductTestFixture.상품_콜라;
import static kitchenpos.product.domain.ProductTestFixture.상품_통다리;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 ui 테스트")
class MenuRestControllerTest extends MenuUiTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;
    private Product 통다리;
    private Product 콜라 = 상품_콜라(2L);
    private MenuGroup 추천_메뉴;

    @BeforeEach
    void setup() {
        super.setUp();
        통다리 = productRepository.save(상품_통다리());
        콜라 = productRepository.save(상품_콜라());
        추천_메뉴 = menuGroupRepository.save(메뉴_그룹_추천_메뉴());
    }

    @DisplayName("생성 성공")
    @Test
    void 생성_성공() {
        //given:
        final Menu 메뉴 = 메뉴(MenuName.from("자메이카 통다리 1인 세트"),
                Price.from(BigDecimal.ONE),
                추천_메뉴.getId(),
                MenuProductBag.from(Arrays.asList(
                        메뉴_상품(통다리.getId(), 5),
                        메뉴_상품(콜라.getId(), 1))));
        //when:
        Menu 저장된_메뉴 = 메뉴_저장됨(메뉴);
        //then:
        assertThat(저장된_메뉴).isEqualTo(메뉴);
    }

    @DisplayName("목록 조회 성공")
    @Test
    void 목록_조회_성공() {
        //given:
        메뉴_저장됨(메뉴(MenuName.from("자메이카 통다리 1인 세트"),
                Price.from(BigDecimal.ONE),
                추천_메뉴.getId(),
                MenuProductBag.from(Arrays.asList(
                        메뉴_상품(통다리.getId(), 5),
                        메뉴_상품(콜라.getId(), 1)))));
        //when:
        final List<Menu> 메뉴_목록 = 메뉴_목록_조회_됨();
        //then:
        assertThat(메뉴_목록).isNotEmpty();
    }

    private Menu 메뉴_저장됨(Menu 메뉴) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(메뉴)
                .when().post("/api/menus")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(Menu.class);
    }

    private List<Menu> 메뉴_목록_조회_됨() {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/menus")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().jsonPath().getList(".", Menu.class);
    }
}
