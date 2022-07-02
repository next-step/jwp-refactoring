package kitchenpos.menu.unit;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MenuTest {

    Product 스테이크;
    Product 샐러드;
    Product 에이드;
    Map<Long, Product> products = new HashMap<>();;

    @BeforeEach
    void init() {
        스테이크 = new Product("스테이크", 200);
        샐러드 = new Product("샐러드", 100);
        에이드 = new Product("에이드", 50);
        products.put(스테이크.getId(), 스테이크);
        products.put(샐러드.getId(), 샐러드);
        products.put(에이드.getId(), 에이드);
    }

    @DisplayName("메뉴 생성에 성공한다.")
    @Test
    void 생성() {
        // given
        MenuGroup 양식 = 메뉴_그룹_생성되어_있음("양식");
        MenuProduct 스테이크_1개 = 메뉴_상품_생성되어_있음(스테이크, 1);
        MenuProduct 샐러드_1개 = 메뉴_상품_생성되어_있음(샐러드, 1);
        MenuProduct 에이드_2개 = 메뉴_상품_생성되어_있음(에이드, 2);
        BigDecimal 상품_금액_총합 = 상품_금액_총합_계산(스테이크_1개, 샐러드_1개, 에이드_2개);

        // when
        Menu 커플_메뉴 = Menu.createMenu(
                "커플 메뉴",
                상품_금액_총합,
                양식,
                Arrays.asList(샐러드_1개, 스테이크_1개, 에이드_2개),
                Menu.DEFAULT_VERSION
        );

        // then
        assertThat(커플_메뉴).isNotNull();
    }

    @DisplayName("메뉴 가격이 0보다 작으면 생성에 실패한다.")
    @Test
    void 생성_예외_가격_음수() {
        // given
        MenuGroup 양식 = 메뉴_그룹_생성되어_있음("양식");
        MenuProduct 스테이크_1개 = 메뉴_상품_생성되어_있음(스테이크, 1);
        MenuProduct 샐러드_1개 = 메뉴_상품_생성되어_있음(샐러드, 1);
        MenuProduct 에이드_2개 = 메뉴_상품_생성되어_있음(에이드, 2);

        // when, then
        assertThatThrownBy(
                () -> Menu.createMenu(
                        "커플 메뉴",
                        BigDecimal.ZERO.subtract(BigDecimal.ONE),
                        양식,
                        Arrays.asList(샐러드_1개, 스테이크_1개, 에이드_2개),
                        Menu.DEFAULT_VERSION
                )
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 그룹이 null이면 메뉴 생성에 실패한다.")
    @Test
    void 생성_예외_잘못된_그룹() {
        // given
        MenuProduct 스테이크_1개 = 메뉴_상품_생성되어_있음(스테이크, 1);
        MenuProduct 샐러드_1개 = 메뉴_상품_생성되어_있음(샐러드, 1);
        MenuProduct 에이드_2개 = 메뉴_상품_생성되어_있음(에이드, 2);
        BigDecimal 상품_금액_총합 = 상품_금액_총합_계산(스테이크_1개, 샐러드_1개, 에이드_2개);

        // when, then
        assertThatThrownBy(
                () -> Menu.createMenu(
                        "커플 메뉴",
                        상품_금액_총합,
                        null,
                        Arrays.asList(샐러드_1개, 스테이크_1개, 에이드_2개),
                        Menu.DEFAULT_VERSION))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 그룹이 필요합니다.");
    }


    BigDecimal 상품_금액_총합_계산(MenuProduct... menuProducts) {
        BigDecimal result = BigDecimal.ZERO;
        for (MenuProduct menuProduct : menuProducts) {
            Product product = products.get(menuProduct.getProductId());
            result.add(product.getPrice(menuProduct.getQuantity()));
        }
        return result;
    }

    public static MenuProduct 메뉴_상품_생성되어_있음(Product product, long quantity) {
        return new MenuProduct(product.getId(), quantity);
    }

    public static MenuGroup 메뉴_그룹_생성되어_있음(String name) {
        return new MenuGroup(name);
    }
}
