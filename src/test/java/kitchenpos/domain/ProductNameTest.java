package kitchenpos.domain;

import kitchenpos.exception.ExceptionMessage;
import kitchenpos.exception.InvalidNameSizeException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("상품 이름 테스트")
class ProductNameTest {

    @DisplayName("상품 이름이 null 이거나 empty 이면 예외를 발생한다.")
    @ParameterizedTest(name = "{index} | {displayName} | {argumentsWithNames}")
    @NullAndEmptySource
    void nullOrEmpty(String input) {
        Assertions.assertThatThrownBy(() -> ProductName.from(input))
                .isInstanceOf(InvalidNameSizeException.class)
                .hasMessageStartingWith(ExceptionMessage.INVALID_PRODUCT_NAME_SIZE);
    }

    @DisplayName("상품 이름이 null 과 empty가 아니면 정상적으로 생성된다.")
    @ParameterizedTest(name = "{index} | {displayName} | {argumentsWithNames}")
    @ValueSource(strings = {"버팔로윙", "치킨텐더", "국"})
    void create(String input) {
        ProductName result = ProductName.from(input);

        Assertions.assertThat(result).isNotNull();
    }

    @DisplayName("상품 이름이 다르면 상품 이름 객체는 동등하지 않다.")
    @Test
    void equalsTest() {
        ProductName productName1 = ProductName.from("버팔로윙");
        ProductName productName2 = ProductName.from("치킨텐더");

        Assertions.assertThat(productName1).isNotEqualTo(productName2);
    }

    @DisplayName("상품 이름이 같으면 상품 이름 객체는 동등하다.")
    @Test
    void equalsTest2() {
        ProductName productName1 = ProductName.from("버팔로윙");
        ProductName productName2 = ProductName.from("버팔로윙");

        Assertions.assertThat(productName1).isEqualTo(productName2);
    }
}
