package kitchenpos.domain;

import kitchenpos.menugroup.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class MenuGroupTest {

    @DisplayName("메뉴그룹 생성 작업을 성공한다.")
    @Test
    void of() {
        // given
        String expectedName = "짜장면";

        // when
        MenuGroup 중국집1인메뉴세트그룹 = MenuGroup.of(expectedName);

        // then
        assertAll(
                () -> assertThat(중국집1인메뉴세트그룹).isNotNull(),
                () -> assertThat(중국집1인메뉴세트그룹.getName()).isEqualTo(expectedName)
        );
    }
}
