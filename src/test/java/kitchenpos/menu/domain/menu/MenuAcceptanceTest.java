package kitchenpos.menu.domain.menu;

import kitchenpos.menu.domain.menugroup.MenuGroup;
import kitchenpos.menu.dto.MenuRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static kitchenpos.menu.domain.fixture.MenuAcceptanceFixture.메뉴_생성;
import static kitchenpos.menu.domain.fixture.MenuDomainFixture.후라이드_치킨;
import static kitchenpos.menu.domain.fixture.MenuGroupDomainFixture.menuGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("메뉴 관리")
class MenuAcceptanceTest {

//    @Nested
//    @DisplayName("인수 테스트 - 메뉴 생성")
//    class CreateMenu {
//        @Test
//        @DisplayName("성공")
//        public void create() {
//            // given
//            MenuRequest menuRequest = new MenuRequest();
//            메뉴_생성();
//
//            // when
//            Menu actual = new Menu(name, price, menuGroup);
//
//            // then
//            assertAll(
//                    () -> assertThat(actual.getName()).isEqualTo(name),
//                    () -> assertThat(actual.getMenuPrice().getPrice()).isEqualTo(price),
//                    () -> assertThat(actual.getMenuGroup()).isEqualTo(menuGroup)
//            );
//        }
//
//        @Test
//        @DisplayName("실패 - 메뉴명 없음")
//        public void failNameEmpty() {
//            // given
//            String name = "";
//            BigDecimal price = BigDecimal.valueOf(15000);
//            MenuGroup menuGroup = menuGroup("치킨 세트");
//
//            // when
//            Assertions.assertThatThrownBy(() -> {
//                Menu actual = new Menu(name, price, menuGroup);
//            }).isInstanceOf(IllegalArgumentException.class);
//        }
//
//        @Test
//        @DisplayName("실패 - 메뉴 그룹 없음")
//        public void failMenuGroupEmpty() {
//            // given
//            String name = "후라이드 치킨";
//            BigDecimal price = BigDecimal.valueOf(15000);
//            MenuGroup menuGroup = null;
//
//            // when
//            Assertions.assertThatThrownBy(() -> {
//                Menu actual = new Menu(name, price, menuGroup);
//            }).isInstanceOf(IllegalArgumentException.class);
//        }
//
//        @Test
//        @DisplayName("실패 - 잘못된 메뉴 가격 입력")
//        public void failPriceIllegal() {
//            // given
//            String name = "후라이드 치킨";
//            BigDecimal price = BigDecimal.valueOf(-1);
//            MenuGroup menuGroup = menuGroup("치킨 세트");
//
//            // when
//            Assertions.assertThatThrownBy(() -> {
//                Menu actual = new Menu(name, price, menuGroup);
//            }).isInstanceOf(IllegalArgumentException.class);
//        }
//    }
}
