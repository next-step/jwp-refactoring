package kitchenpos.menu.domain;

import static kitchenpos.menu.MenuFixture.더블강정치킨;
import static kitchenpos.menu.MenuFixture.더블개손해치킨상품;
import static kitchenpos.menu.MenuGroupFixture.추천메뉴;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.menu.MenuFixture;
import kitchenpos.product.dao.ProductDao;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuTest {

    @Mock
    private ProductDao productDao;

    @Test
    @DisplayName("가격정보가 없거나 0원이하면 안됩니다.")
    void validateMenuPrice() {
        Assertions.assertThatThrownBy(
            () -> new Menu(더블강정치킨.getId(), 더블강정치킨.getName(), null, 더블강정치킨.getMenuGroupId(),
                더블강정치킨.getMenuProducts()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("가격정보가 없거나 0원이하면 안됩니다.");
    }

    @Test
    @DisplayName("메뉴의 가격이 상품들의 가격 합보다 크면 안된다")
    void validateSumOfMenuPrice() {
        Assertions
            .assertThatThrownBy(() -> new Menu(1L, "더블강정치킨", new BigDecimal(199_000),
                추천메뉴.getId(),
                Arrays.asList(MenuFixture.더블강정치킨상품, 더블개손해치킨상품)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("메뉴의 가격이 상품들의 가격 합보다 크면 안된다");

    }
}