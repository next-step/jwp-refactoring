package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("테이블 비움 여부 테스트")
class EmptyTest {
    @Test
    @DisplayName("테이블 비움 여부 객체 생성")
    void createEmpty() {
        // when
        Empty actual = Empty.from(true);

        // then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual).isInstanceOf(Empty.class)
        );
    }

    @ParameterizedTest(name = "[{index}] 테이블 비움 여부를 확인한다")
    @ValueSource(booleans = {true, false})
    void isEmpty(boolean expect) {
        // given
        Empty actual = Empty.from(expect);

        // when & then
        assertThat(actual.isTrue()).isEqualTo(expect);
    }
}
