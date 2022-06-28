package kitchenpos.menu.unit;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class MenuTest {

    Product 스테이크;
    Product 샐러드;
    Product 에이드;

    @Autowired
    ProductRepository productRepository;

    @BeforeEach
    void init() {
        스테이크 = productRepository.save(new Product("스테이크", 200));
        샐러드 = productRepository.save(new Product("샐러드", 100));
        에이드 = productRepository.save(new Product("에이드", 50));
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
        MenuProduct 스테이크_1개 = 메뉴_상품_생성되어_있음(스테이크, 1);
        MenuProduct 샐러드_1개 = 메뉴_상품_생성되어_있음(샐러드, 1);
        MenuProduct 에이드_2개 = 메뉴_상품_생성되어_있음(에이드, 2);

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
                        Arrays.asList(샐러드_1개, 스테이크_1개, 에이드_2개)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 그룹이 필요합니다.");
    }

    BigDecimal 상품_금액_총합_계산(MenuProduct... menuProducts) {
        return 상품_금액_총합_계산(productRepository, menuProducts);
    }

    static BigDecimal 상품_금액_총합_계산(ProductRepository productRepository, MenuProduct... menuProducts) {
        BigDecimal result = BigDecimal.ZERO;
        for (MenuProduct menuProduct : menuProducts) {
            Product product = productRepository.findById(menuProduct.getProductId()).get();
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

    static public Menu 메뉴_생성되어_있음(ProductRepository productRepository, String menuName, String menuGroupName, MenuProduct... menuProducts) {
        MenuGroup menuGroup = 메뉴_그룹_생성되어_있음(menuGroupName);
        BigDecimal totalPrice = 상품_금액_총합_계산(productRepository, menuProducts);
        return Menu.createMenu(menuName, totalPrice, menuGroup, Arrays.asList(menuProducts));
    }
}
