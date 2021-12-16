package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("테이블 상태")
class TableStatusTest {

    @ParameterizedTest(name = "[{index}] empty {0} 이라면 상태는 {1}")
    @DisplayName("빈 값 여부로 상태 조회")
    @CsvSource({"true,EMPTY", "false,FULL"})
    void valueOf(boolean empty, CustomerStatus expected) {
        assertThat(CustomerStatus.valueOf(empty))
            .isEqualTo(expected);
    }
}
