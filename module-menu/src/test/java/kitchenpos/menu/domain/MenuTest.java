package kitchenpos.menu.domain;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("메뉴 도메인 테스트")
class MenuTest {
    private MenuProduct 짜장면_보통;
    private MenuProduct 짜장면_곱배기;
    private MenuGroup 중국집_메뉴_그룹;
    private Menu 짜장면;
    private Product 짜장면_상품;

    @BeforeEach
    void setUp() {
        짜장면_상품 = new Product("짜장면", new BigDecimal(1000));
        짜장면_보통 = new MenuProduct(1L, new Menu(), 짜장면_상품.getId(), 1);
        짜장면_곱배기 = new MenuProduct(2L, new Menu(), 짜장면_상품.getId(), 2);
        중국집_메뉴_그룹 = new MenuGroup();
        짜장면 = new Menu("짜장면", 10000, 중국집_메뉴_그룹, Lists.newArrayList(짜장면_보통, 짜장면_곱배기));
    }

    @DisplayName("메뉴를 등록할 수 있다.")
    @Test
    void createMenuTest() {
        // then
        assertAll(
                () -> assertThat(짜장면.getName()).isEqualTo("짜장면"),
                () -> assertThat(짜장면.getPrice()).isEqualTo(BigDecimal.valueOf(10000))
        );
    }
}