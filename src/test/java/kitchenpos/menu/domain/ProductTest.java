package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductTest {
    
    @Test
    @DisplayName("상품이 생성된다")
    void 상품_생성() {
        // given, when
        Product 상품 = Product.of("치킨", 17000);
        
        // then
        assertAll(
                () -> assertThat(상품.getName()).isEqualTo("치킨"),
                () ->assertThat(상품.getPrice()).isEqualTo(Price.from(17000))
        );
    }
    
    @DisplayName("상품 가격은 0원 이상이어야한다")
    @Test
    void 상품_가격_0원_이상() {
        // given, when, then
        assertThatThrownBy(() -> {
            Product.of("치킨", -6000);
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("가격은 0원 이상이어야 합니다");
    }

}
