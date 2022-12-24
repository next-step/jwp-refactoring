package kitchenpos.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.domain.MenuFixture.메뉴;
import static kitchenpos.domain.MenuGroupFixture.메뉴그룹;
import static kitchenpos.domain.MenuProductFixture.메뉴상품;
import static kitchenpos.domain.ProductFixture.상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("MenuProducts 클래스 테스트")
public class MenuProductsTest {

    private Product 알리오올리오;
    private Product 쉬림프로제;
    private Product 카프레제샐러드;
    private Product 레드와인;
    private MenuGroup 코스;
    private Menu 풀코스;

    @BeforeEach
    void setup() {
        알리오올리오 = 상품(1L, "알리오올리오", 17000);
        쉬림프로제 = 상품(3L, "쉬림프로제", 18000);
        카프레제샐러드 = 상품(4L, "카프레제샐러드", 13000);
        레드와인 = 상품(5L, "레드와인", 9000);

        코스 = 메뉴그룹(2L, "코스");
        풀코스 = 메뉴(2L, "풀코스", 62000, 코스);
    }

    @DisplayName("MenuProduct 가격 합으로 Menu 가격을 검증한다")
    @Test
    void Menu_가격_검증_테스트() {
        // given
        MenuGroup 코스 = 메뉴그룹(2L, "코스");
        Menu 풀코스 = 메뉴(2L, "풀코스", 70000, 코스);

        MenuProduct 풀코스_카프레제샐러드 = 메뉴상품(3L, 풀코스.getId(), 카프레제샐러드, 1);
        MenuProduct 풀코스_알리오올리오 = 메뉴상품(4L, 풀코스.getId(), 알리오올리오, 1);
        MenuProduct 풀코스_쉬림프로제 = 메뉴상품(5L, 풀코스.getId(), 쉬림프로제, 1);
        MenuProduct 풀코스_레드와인 = 메뉴상품(6L, 풀코스.getId(), 레드와인, 2);

        List<MenuProduct> menuProductList = Arrays.asList(풀코스_카프레제샐러드, 풀코스_알리오올리오, 풀코스_쉬림프로제, 풀코스_레드와인);

        // when
        MenuProducts menuProducts = new MenuProducts(menuProductList);

        // then
        assertThatThrownBy(
                () -> menuProducts.validateMenuPrice(풀코스.getPrice())
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("MenuProducts 전체 금액을 계산한다")
    @Test
    void MenuProducts_전체_금액_계산_테스트() {
        // given
        MenuProduct 풀코스_카프레제샐러드 = 메뉴상품(3L, 풀코스.getId(), 카프레제샐러드, 1);
        MenuProduct 풀코스_알리오올리오 = 메뉴상품(4L, 풀코스.getId(), 알리오올리오, 1);
        MenuProduct 풀코스_쉬림프로제 = 메뉴상품(5L, 풀코스.getId(), 쉬림프로제, 1);
        MenuProduct 풀코스_레드와인 = 메뉴상품(6L, 풀코스.getId(), 레드와인, 2);

        List<MenuProduct> menuProductList = Arrays.asList(풀코스_카프레제샐러드, 풀코스_알리오올리오, 풀코스_쉬림프로제, 풀코스_레드와인);
        MenuProducts menuProducts = new MenuProducts(menuProductList);

        // when
        Price menuProductPriceSum = menuProducts.getMenuProductPriceSum();

        // then
        assertThat(menuProductPriceSum.value().intValue()).isEqualTo(66000);
    }
}
