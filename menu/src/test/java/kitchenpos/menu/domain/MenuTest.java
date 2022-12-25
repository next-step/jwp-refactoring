package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.product.domain.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuTest {

    MenuProduct 더블강정치킨상품;
    MenuProduct 더블개손해치킨상품;
    MenuGroup 추천메뉴;

    @BeforeEach
    void setup() {
        Product 개손해치킨 = new Product(2L, "개손해치킨", new BigDecimal(1));
        Product 강정치킨 = new Product(1L, "강정치킨", new BigDecimal(17_000));
        더블강정치킨상품 = new MenuProduct(1L, null, 강정치킨.getId(), 2L);
        더블개손해치킨상품 = new MenuProduct(1L, null, 개손해치킨.getId(), 2L);
        추천메뉴 = new MenuGroup(1L, "추천메뉴");
    }

    @Test
    @DisplayName("메뉴 생성")
    void createMenu() {
        //when
        Menu 더블강정치킨 = new Menu(1L, "더블강정치킨", new BigDecimal(1L), 추천메뉴, Arrays.asList(더블강정치킨상품));

        //then
        assertThat(더블강정치킨.getName()).isEqualTo("더블강정치킨");
        assertThat(더블강정치킨.getPrice()).isEqualTo(new BigDecimal(1L));
    }

    @Test
    @DisplayName("가격정보가 없거나 0원미만이면 안됩니다.")
    void validateMenuPrice() {
        //when & then
        Assertions.assertThatThrownBy(
            () -> new Menu(1L, "더블강정치킨", null,
                추천메뉴,
                Arrays.asList(더블강정치킨상품)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("가격정보가 없거나 0원미만이면 안됩니다.");

        //when & then
        Assertions.assertThatThrownBy(
            () -> new Menu(1L, "더블강정치킨", new BigDecimal(-1),
                추천메뉴,
                Arrays.asList(더블강정치킨상품)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("가격정보가 없거나 0원미만이면 안됩니다.");
    }
}