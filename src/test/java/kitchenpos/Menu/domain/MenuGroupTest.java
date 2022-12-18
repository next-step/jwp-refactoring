package kitchenpos.Menu.domain;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("MenuGroup 클래스 테스트")
public class MenuGroupTest {

    @DisplayName("MenuGroup을 생성한다")
    @Test
    void MenuGroup_생성() {
        MenuGroup menuGroup = new MenuGroup("오일");

        assertAll(
                () -> assertThat(menuGroup.getName()).isEqualTo("알리오올리오")
        );
    }
}
