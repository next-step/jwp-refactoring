package kitchenpos.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MenuTest {

    MenuGroup 양식;
    MenuProduct 샐러드_1개;
    MenuProduct 스테이크_1개;
    MenuProduct 에이드_2개;
    BigDecimal 메뉴_구성_상품_금액_총합;

    @BeforeEach
    void init() {
        // given
        Product 샐러드 = new Product("샐러드", BigDecimal.valueOf(100));
        Product 스테이크 = new Product("스테이크", BigDecimal.valueOf(200));
        Product 에이드 = new Product("에이드", BigDecimal.valueOf(50));
        양식 = new MenuGroup("양식");
        샐러드_1개 = new MenuProduct(샐러드, 1);
        스테이크_1개 = new MenuProduct(스테이크, 1);
        에이드_2개 = new MenuProduct(에이드, 2);
        메뉴_구성_상품_금액_총합 = 샐러드_1개.getPrice()
                .add(스테이크_1개.getPrice())
                .add(에이드_2개.getPrice());
    }

    @DisplayName("메뉴 생성에 성공한다.")
    @Test
    void 생성() {
        // when
        Menu 커플_메뉴 = Menu.createMenu(
                "커플 메뉴",
                메뉴_구성_상품_금액_총합,
                양식,
                Arrays.asList(샐러드_1개, 스테이크_1개, 에이드_2개)
        );

        // then
        assertThat(커플_메뉴).isNotNull();
    }

    @DisplayName("메뉴 가격이 0보다 작으면 생성에 실패한다.")
    @Test
    void 생성_예외_가격_음수() {
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
        // when, then
        assertThatThrownBy(
                () -> Menu.createMenu(
                        "커플 메뉴",
                        메뉴_구성_상품_금액_총합.add(BigDecimal.ONE),
                        양식,
                        Arrays.asList(샐러드_1개, 스테이크_1개, 에이드_2개)
                )
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 그룹이 null이면 메뉴 생성에 실패한다.")
    @Test
    void 생성_예외_잘못된_그룹() {
        // when, then
        assertThatThrownBy(
                () -> Menu.createMenu(
                        "커플 메뉴",
                        메뉴_구성_상품_금액_총합.add(BigDecimal.ONE),
                        null,
                        Arrays.asList(샐러드_1개, 스테이크_1개, 에이드_2개)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 그룹이 필요합니다.");
    }
}
