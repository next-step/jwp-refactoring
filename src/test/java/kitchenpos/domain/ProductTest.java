package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    @DisplayName("상품 이름은 공백이면 안됩니다.")
    void nameIsNotNull() {
        assertAll(
                () -> assertThatIllegalArgumentException().isThrownBy(() -> new Product("")),
                () -> assertThatIllegalArgumentException().isThrownBy(() -> new Product(null))
        );
    }

}