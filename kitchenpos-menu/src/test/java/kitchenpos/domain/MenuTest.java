package kitchenpos.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static kitchenpos.domain.MenuFixture.메뉴;
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
        알리오올리오 = ProductFixture.상품(1L, "알리오올리오", 17000);
        쉬림프로제 = ProductFixture.상품(3L, "쉬림프로제", 18000);
        카프레제샐러드 = ProductFixture.상품(4L, "카프레제샐러드", 13000);
        레드와인 = ProductFixture.상품(5L, "레드와인", 9000);

        코스 = MenuGroupFixture.메뉴그룹(2L, "코스");
        풀코스 = 메뉴(2L, "풀코스", 62000, 코스);

        풀코스_카프레제샐러드 = MenuProductFixture.메뉴상품(3L, 풀코스.getId(), 카프레제샐러드, 1);
        풀코스_알리오올리오 = MenuProductFixture.메뉴상품(4L, 풀코스.getId(), 알리오올리오, 1);
        풀코스_쉬림프로제 = MenuProductFixture.메뉴상품(5L, 풀코스.getId(), 쉬림프로제, 1);
        풀코스_레드와인 = MenuProductFixture.메뉴상품(6L, 풀코스.getId(), 레드와인, 2);
    }

    @DisplayName("메뉴 클래스를 생성한다")
    @Test
    void 메뉴_클래스_생성() {
        // when
        풀코스 = 메뉴(2L, "풀코스", 62000, 코스);

        // then
        assertAll(
                () -> assertThat(풀코스).isNotNull(),
                () -> assertThat(풀코스.getName()).isEqualTo("풀코스"),
                () -> assertThat(풀코스.getMenuGroup()).isEqualTo(코스),
                () -> assertThat(풀코스.getPrice().value()).isEqualTo(new BigDecimal(62000))
        );
    }

    @DisplayName("음수 가격으로 메뉴 클래스를 생성한다")
    @Test
    void 음수_가격_메뉴_클래스_생성() {
        // when & then
        assertThatThrownBy(
                () -> 풀코스 = 메뉴(2L, "풀코스", -62000, 코스)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
