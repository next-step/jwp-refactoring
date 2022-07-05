package kitchenpos.menu.domain;

import static kitchenpos.menu.fixture.MenuFixture.메뉴_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import kitchenpos.common.domain.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

    private Price price;
    private MenuProduct menuProduct;

    @BeforeEach
    void setUp() {
        //given
        price = new Price(10000);
        menuProduct = new MenuProduct(1L, 1L, 1L);
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    void create() {
        //when
        Menu menu = 메뉴_생성("파스테세트", price, 1L, Arrays.asList(menuProduct));

        //then
        assertThat(menu).isNotNull();
        assertThat(menu.getName()).isEqualTo("파스테세트");
        assertThat(menu.getPrice()).isEqualTo(price.get());
        assertThat((menu.getMenuProducts().get())).containsExactly(menuProduct);
    }

    @DisplayName("메뉴 가격이 0보다 작은 경우, 메뉴 생성에 실패한다.")
    @Test
    void create_invalidPrice() {
        //when & then
        assertThatThrownBy(() -> 메뉴_생성("파스테세트", new Price(-1), 1L, Arrays.asList(menuProduct)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("메뉴 가격은 0보다 작을 수 없습니다.");
    }
}
