package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class MenuGroupTest {

    @Test
    void of() {
        // when
        String expectedName = "짜장면";
        MenuGroup 중국집1인메뉴세트그룹 = MenuGroup.of(expectedName);

        // then
        assertAll(
                () -> assertThat(중국집1인메뉴세트그룹).isNotNull(),
                () -> assertThat(중국집1인메뉴세트그룹.getName()).isEqualTo(expectedName)
        );
    }
}
