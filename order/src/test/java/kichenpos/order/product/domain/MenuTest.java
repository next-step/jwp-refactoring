package kichenpos.order.product.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.stream.Stream;
import kichenpos.common.domain.Name;
import kichenpos.common.domain.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("메뉴")
class MenuTest {

    @Test
    @DisplayName("생성")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> Menu.of(
                1L,
                Name.from("후라이드치킨세트"),
                Price.ZERO
            ));
    }

    @ParameterizedTest(name = "[{index}] {argumentsWithNames} 으로 생성 불가능")
    @DisplayName("이름, 가격은 필수")
    @MethodSource
    void instance_nullArguments_thrownIllegalArgumentException(Name name, Price price) {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> Menu.of(1L, name, price))
            .withMessageEndingWith("필수입니다.");
    }

    private static Stream<Arguments> instance_nullArguments_thrownIllegalArgumentException() {
        return Stream.of(
            Arguments.of(null, Price.ZERO),
            Arguments.of(Name.from("치킨"), null)
        );
    }
}

