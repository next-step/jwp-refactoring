package kitchenpos.Menu.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static kitchenpos.Menu.domain.MenuFixture.메뉴;
import static kitchenpos.Menu.domain.MenuGroupFixture.메뉴그룹;
import static kitchenpos.Menu.domain.MenuProductFixture.메뉴상품;
import static kitchenpos.Product.domain.ProductFixture.상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Menu 클래스 테스트")
public class MenuTest {

    private Product 알리오올리오;
    private Product 쉬림프로제;
    private Product 카프레제샐러드;
    private Product 레드와인;

    private MenuGroup 코스;

    private Menu 풀코스;

    private MenuProduct 풀코스_카프레제샐러드;
    private MenuProduct 풀코스_알리오올리오;
    private MenuProduct 풀코스_쉬림프로제;
    private MenuProduct 풀코스_레드와인;

    @BeforeEach
    void setup() {
        알리오올리오 = 상품(1L, "알리오올리오", new BigDecimal(17000));
        쉬림프로제 = 상품(3L, "쉬림프로제", new BigDecimal(18000));
        카프레제샐러드 = 상품(4L, "카프레제샐러드", new BigDecimal(13000));
        레드와인 = 상품(5L, "레드와인", new BigDecimal(9000));

        코스 = 메뉴그룹(2L, "코스");

        풀코스_카프레제샐러드 = 메뉴상품(3L, 풀코스, 카프레제샐러드, 1);
        풀코스_알리오올리오 = 메뉴상품(4L, 풀코스, 알리오올리오, 1);
        풀코스_쉬림프로제 = 메뉴상품(5L, 풀코스, 쉬림프로제, 1);
        풀코스_레드와인 = 메뉴상품(6L, 풀코스, 레드와인, 2);
    }

    @DisplayName("메뉴 클래스를 생성한다")
    @Test
    void 메뉴_클래스_생성() {
        // when
        풀코스 = 메뉴(2L, "풀코스", new BigDecimal(62000), 코스);
        풀코스.addMenuProduct(Arrays.asList(풀코스_카프레제샐러드, 풀코스_알리오올리오, 풀코스_쉬림프로제, 풀코스_레드와인));

        // then
        assertAll(
                () -> assertThat(풀코스).isNotNull(),
                () -> assertThat(풀코스.getName()).isEqualTo("풀코스"),
                () -> assertThat(풀코스.getMenuGroup()).isEqualTo(코스),
                () -> assertThat(풀코스.getPrice().intValue()).isEqualTo(62000),
                () -> assertThat(풀코스.getMenuProducts()).hasSize(4)
        );
    }

    @DisplayName("음수 가격으로 메뉴 클래스를 생성한다")
    @Test
    void 음수_가격_메뉴_클래스_생성() {
        // when & then
        assertThatThrownBy(
                () -> 풀코스 = 메뉴(2L, "풀코스", new BigDecimal(-62000), 코스)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("전체 메뉴상품 가격 합보다 큰 가격의 메뉴 클래스를 생성한다")
    @Test
    void 메뉴_상품_가격_합보다_큰_가격의_메뉴_클래스_생성() {
        //given
        풀코스 = 메뉴(2L, "풀코스", new BigDecimal(100000), 코스);

        // when & then
        assertThatThrownBy(
                () -> 풀코스.addMenuProduct(Arrays.asList(풀코스_카프레제샐러드, 풀코스_알리오올리오, 풀코스_쉬림프로제, 풀코스_레드와인))
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
