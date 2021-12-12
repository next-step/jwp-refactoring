package kitchenpos.menu.domain.menu;

import kitchenpos.menu.domain.menugroup.MenuGroup;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static kitchenpos.fixture.MenuGroupDomainFixture.menuGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("메뉴 관리")
class MenuTest {

    @Nested
    @DisplayName("메뉴 생성")
    class CreateMenu {
        @Test
        @DisplayName("성공")
        public void create() {
            // given
            final String name = "후라이드 치킨";
            final BigDecimal price = BigDecimal.valueOf(15000);
            final MenuGroup menuGroup = menuGroup("치킨 세트");

            // when
            final Menu actual = new Menu(name, price, menuGroup);

            // then
            assertAll(
                    () -> assertThat(actual.getName()).isEqualTo(name),
                    () -> assertThat(actual.getMenuPrice().getPrice()).isEqualTo(price),
                    () -> assertThat(actual.getMenuGroup()).isEqualTo(menuGroup)
            );
        }

        @Test
        @DisplayName("실패 - 메뉴명 없음")
        public void failNameEmpty() {
            // given
            final String name = "";
            final BigDecimal price = BigDecimal.valueOf(15000);
            final MenuGroup menuGroup = menuGroup("치킨 세트");

            // when
            Assertions.assertThatThrownBy(() -> {
                Menu actual = new Menu(name, price, menuGroup);
            }).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("실패 - 메뉴 그룹 없음")
        public void failMenuGroupEmpty() {
            // given
            final String name = "후라이드 치킨";
            final BigDecimal price = BigDecimal.valueOf(15000);
            final MenuGroup menuGroup = null;

            // when
            Assertions.assertThatThrownBy(() -> {
                Menu actual = new Menu(name, price, menuGroup);
            }).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("실패 - 잘못된 메뉴 가격 입력")
        public void failPriceIllegal() {
            // given
            final String name = "후라이드 치킨";
            final BigDecimal price = BigDecimal.valueOf(-1);
            final MenuGroup menuGroup = menuGroup("치킨 세트");

            // when
            Assertions.assertThatThrownBy(() -> {
                Menu actual = new Menu(name, price, menuGroup);
            }).isInstanceOf(IllegalArgumentException.class);
        }
    }
}
