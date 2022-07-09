package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NameTest {

    @Test
    @DisplayName("이름은 공백이거나 비어있으면 안됩니다.")
    void nameIsNotEmpty() {
        assertAll(
                () -> assertThatIllegalArgumentException().isThrownBy(() -> new Name("")),
                () -> assertThatIllegalArgumentException().isThrownBy(() -> new Name(null))
        );
    }

    @Test
    @DisplayName("이름이 생성됨")
    void createName() {
        Name name = new Name("이름");

        //when & then
        assertThat(name.value()).isEqualTo("이름");
    }
}
