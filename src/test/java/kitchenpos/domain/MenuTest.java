package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {

    @DisplayName("메뉴 생성에 성공한다.")
    @Test
    void 생성() {
        // given
        MenuGroup 양식 = 메뉴_그룹_생성되어_있음("양식");
        MenuProduct 샐러드_1개 = 메뉴_상품_생성되어_있음("샐러드", 100, 1);
        MenuProduct 스테이크_1개 = 메뉴_상품_생성되어_있음("스테이크", 200, 1);
        MenuProduct 에이드_2개 = 메뉴_상품_생성되어_있음("에이드", 50, 2);
        BigDecimal 상품_금액_총합 =BigDecimal.ZERO
                .add(샐러드_1개.getPrice())
                .add(스테이크_1개.getPrice())
                .add(에이드_2개.getPrice());

        // when
        Menu 커플_메뉴 = Menu.createMenu(
                "커플 메뉴",
                상품_금액_총합,
                양식,
                Arrays.asList(샐러드_1개, 스테이크_1개, 에이드_2개)
        );

        // then
        assertThat(커플_메뉴).isNotNull();
    }

    @DisplayName("메뉴 가격이 0보다 작으면 생성에 실패한다.")
    @Test
    void 생성_예외_가격_음수() {
        // given
        MenuGroup 양식 = 메뉴_그룹_생성되어_있음("양식");
        MenuProduct 샐러드_1개 = 메뉴_상품_생성되어_있음("샐러드", 100, 1);
        MenuProduct 스테이크_1개 = 메뉴_상품_생성되어_있음("스테이크", 200, 1);
        MenuProduct 에이드_2개 = 메뉴_상품_생성되어_있음("에이드", 50, 2);

        // when, then
        assertThatThrownBy(
                () -> Menu.createMenu(
                        "커플 메뉴",
                        BigDecimal.ZERO.subtract(BigDecimal.ONE),
                        양식,
                        Arrays.asList(샐러드_1개, 스테이크_1개, 에이드_2개)
                )
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 구성 상품들의 금액 총합보다 메뉴 가격이 더 크면 생성에 실패한다.")
    @Test
    void 생성_예외_가격_초과() {
        // given
        MenuGroup 양식 = 메뉴_그룹_생성되어_있음("양식");
        MenuProduct 샐러드_1개 = 메뉴_상품_생성되어_있음("샐러드", 100, 1);
        MenuProduct 스테이크_1개 = 메뉴_상품_생성되어_있음("스테이크", 200, 1);
        MenuProduct 에이드_2개 = 메뉴_상품_생성되어_있음("에이드", 50, 2);
        BigDecimal 상품_금액_총합 =BigDecimal.ZERO
                .add(샐러드_1개.getPrice())
                .add(스테이크_1개.getPrice())
                .add(에이드_2개.getPrice());

        // when, then
        assertThatThrownBy(
                () -> Menu.createMenu(
                        "커플 메뉴",
                        상품_금액_총합.add(BigDecimal.ONE),
                        양식,
                        Arrays.asList(샐러드_1개, 스테이크_1개, 에이드_2개)
                )
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 그룹이 null이면 메뉴 생성에 실패한다.")
    @Test
    void 생성_예외_잘못된_그룹() {
        // given
        MenuProduct 샐러드_1개 = 메뉴_상품_생성되어_있음("샐러드", 100, 1);
        MenuProduct 스테이크_1개 = 메뉴_상품_생성되어_있음("스테이크", 200, 1);
        MenuProduct 에이드_2개 = 메뉴_상품_생성되어_있음("에이드", 50, 2);
        BigDecimal 상품_금액_총합 =BigDecimal.ZERO
                .add(샐러드_1개.getPrice())
                .add(스테이크_1개.getPrice())
                .add(에이드_2개.getPrice());

        // when, then
        assertThatThrownBy(
                () -> Menu.createMenu(
                        "커플 메뉴",
                        상품_금액_총합,
                        null,
                        Arrays.asList(샐러드_1개, 스테이크_1개, 에이드_2개)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 그룹이 필요합니다.");
    }

    public static MenuProduct 메뉴_상품_생성되어_있음(String name, long price, long quantity) {
        return new MenuProduct(new Product(name, BigDecimal.valueOf(price)), quantity);
    }

    public static MenuGroup 메뉴_그룹_생성되어_있음(String name) {
        return new MenuGroup(name);
    }

    public static Menu 메뉴_생성되어_있음(String menuName, String menuGroupName, MenuProduct... menuProducts) {
        MenuGroup menuGroup = 메뉴_그룹_생성되어_있음(menuGroupName);
        BigDecimal totalPrice = Arrays.stream(menuProducts)
                .map(MenuProduct::getPrice)
                .reduce(BigDecimal.ZERO, (acc, price) -> acc.add(price));
        return Menu.createMenu(menuName, totalPrice, menuGroup, Arrays.asList(menuProducts));
    }
}
