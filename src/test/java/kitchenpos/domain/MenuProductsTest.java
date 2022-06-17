package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("MenuProducts 클래스 테스트")
class MenuProductsTest {

    private final MenuEntity menu = new MenuEntity("강정치킨", BigDecimal.TEN, 1L);
    private final List<MenuProductEntity> menuProducts = Arrays.asList(new MenuProductEntity(new ProductEntity("강정치킨", BigDecimal.TEN), 1L));

    @DisplayName("1개의 MenuProduct를 MenuProducts에 추가한다.")
    @Test
    void create() {
        MenuProducts menuProducts = new MenuProducts();

        menuProducts.addAll(this.menu, this.menuProducts);

        assertAll(
                () -> assertThat(menuProducts.get()).hasSize(1),
                () -> assertThat(menuProducts.get().get(0).getMenu()).isEqualTo(menu)
        );
    }
}
