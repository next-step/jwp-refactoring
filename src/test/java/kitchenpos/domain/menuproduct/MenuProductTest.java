package kitchenpos.domain.menuproduct;

import kitchenpos.domain.Price;
import kitchenpos.fixture.CleanUp;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kitchenpos.fixture.MenuFixture.양념치킨_콜라_1000원_1개;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

class MenuProductTest {
    @BeforeEach
    void setUp() {
        CleanUp.cleanUpTableFirst();
    }

    @Test
    void getAmount() {
        MenuProduct menuProduct = new MenuProduct(null, ProductFixture.양념치킨_1000원, 10);

        assertThat(menuProduct.getAmount()).isEqualTo(new Price(10000));
    }

    @Test
    @DisplayName("이미 메뉴가 등록되어있으면 변경이 불가능하다.")
    void 이미_메뉴가_등록되어있으면_변경이_불가능하다() {
        MenuProduct menuProduct = new MenuProduct(양념치킨_콜라_1000원_1개, null, 1);

        assertThatIllegalStateException().isThrownBy(() -> menuProduct.changeMenu(양념치킨_콜라_1000원_1개));
    }
}