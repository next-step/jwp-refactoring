package kitchenpos.menu.unit;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import kitchenpos.common.Name;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;

@DisplayName("메뉴 관련 단위 테스트")
public class MenuTest {
    private MenuGroup 중식;
    private Product 짜장면;
    private Product 군만두;

    @BeforeEach
    void setUp() {
        중식 = MenuGroup.of("중식");
        짜장면 = Product.of("짜장면", BigDecimal.valueOf(5000));
        군만두 = Product.of("군만두", BigDecimal.valueOf(2000));
        ReflectionTestUtils.setField(짜장면, "id", 1L);
        ReflectionTestUtils.setField(군만두, "id", 2L);
    }

    @DisplayName("메뉴를 생성할 수 있다.")
    @Test
    void createMenu() {
        // given
        MenuProduct 짜장면_1개 = MenuProduct.of(짜장면, 1);
        MenuProduct 군만두_2개 = MenuProduct.of(군만두, 2);
        // when
        Menu 짜장세트 = Menu.of("짜장세트", BigDecimal.valueOf(6000), 중식,
            MenuProducts.of(Arrays.asList(짜장면_1개, 군만두_2개)));
        // then
        assertAll(
            () -> assertThat(짜장세트.getMenuGroup().getName()).isEqualTo(Name.of("중식")),
            () -> assertThat(짜장세트.getMenuProducts().value().size()).isEqualTo(2)
        );
    }
}
