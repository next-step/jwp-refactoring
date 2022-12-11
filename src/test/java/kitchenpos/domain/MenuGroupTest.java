package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class MenuGroupTest {

    @Test
    void of() {
        // when
        long expectedId = 1L;
        String expectedName = "짜장면";
        MenuGroup 중국집_1인_메뉴_세트 = MenuGroup.of(expectedId, expectedName);

        // then
        assertAll(
                () -> assertThat(중국집_1인_메뉴_세트).isNotNull(),
                () -> assertThat(중국집_1인_메뉴_세트.getId()).isEqualTo(expectedId),
                () -> assertThat(중국집_1인_메뉴_세트.getName()).isEqualTo(expectedName)
        );
    }
}
