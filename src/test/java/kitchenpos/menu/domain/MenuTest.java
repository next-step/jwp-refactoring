package kitchenpos.menu.domain;

import static kitchenpos.fixture.MenuFixture.*;
import static kitchenpos.fixture.ProductFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

    private Price price;
    private MenuGroup menuGroup;
    private Product product;
    private MenuProduct menuProduct;

    @BeforeEach
    void setUp() {
        //given
        price = new Price(10000);
        menuGroup = new MenuGroup("파스타메뉴");
        product = 상품_생성(1L, "알리오올리오", 15000);
        menuProduct = new MenuProduct(1L, product, 1L);
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    void create() {
        //when
        Menu menu = 메뉴_생성("파스테세트", price, menuGroup, Arrays.asList(menuProduct));

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
        assertThatThrownBy(() -> 메뉴_생성("파스테세트", new Price(-1), menuGroup, Arrays.asList(menuProduct)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("메뉴 가격은 0보다 작을 수 없습니다.");
    }

    @DisplayName("메뉴 내 제품가격의 합보다 메뉴 가격이 크면, 메뉴 등록에 실패한다.")
    @Test
    void create_invalidPriceSum() {
        //when & then
        assertThatThrownBy(() -> 메뉴_생성("파스테세트", new Price(20000), menuGroup, Arrays.asList(menuProduct)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("메뉴 내 제품가격의 합보다 메뉴가격이 클 수 없습니다.");
    }

}
