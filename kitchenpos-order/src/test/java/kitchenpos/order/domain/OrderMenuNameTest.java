package kitchenpos.order.domain;

import kitchenpos.exception.ExceptionMessage;
import kitchenpos.exception.InvalidNameSizeException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("주문 메뉴 테스트")
class OrderMenuNamReTest {

    @DisplayName("주문 메뉴 이름이 null 이거나 empty 이면 예외를 발생한다.")
    @ParameterizedTest(name = "{index} | {displayName} | {argumentsWithNames}")
    @NullAndEmptySource
    void nullOrEmpty(String input) {
        Assertions.assertThatThrownBy(() -> OrderMenuName.from(input))
                .isInstanceOf(InvalidNameSizeException.class)
                .hasMessageStartingWith(ExceptionMessage.INVALID_ORDER_MENU_NAME);
    }

    @DisplayName("주문 메뉴 이름이 null 과 empty가 아니면 정상적으로 생성된다.")
    @ParameterizedTest(name = "{index} | {displayName} | {argumentsWithNames}")
    @ValueSource(strings = {"버팔로윙", "치킨텐더", "국"})
    void create(String input) {
        OrderMenuName result = OrderMenuName.from(input);

        Assertions.assertThat(result).isNotNull();
    }

    @DisplayName("주문 메뉴 이름이 다르면 메뉴 이름 객체는 동등하지 않다.")
    @Test
    void equalsTest() {
        OrderMenuName menuName1 = OrderMenuName.from("버팔로윙");
        OrderMenuName menuName2 = OrderMenuName.from("치킨텐더");

        Assertions.assertThat(menuName1).isNotEqualTo(menuName2);
    }

    @DisplayName("주문 메뉴 이름이 같으면 메뉴 이름 객체는 동등하다.")
    @Test
    void equalsTest2() {
        OrderMenuName menuName1 = OrderMenuName.from("버팔로윙");
        OrderMenuName menuName2 = OrderMenuName.from("버팔로윙");

        Assertions.assertThat(menuName1).isEqualTo(menuName2);
    }
}
