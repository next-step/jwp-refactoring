package kitchenpos.common.unit;


import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.common.Name;
import kitchenpos.exception.ErrorMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("이름 관련 단위테스트")
public class NameTest {
    @DisplayName("객체 생성시 이름이 없으면 에러가 발생한다.")
    @Test
    void create_name_null_exception() {
        // when - then
        assertThatThrownBy(() -> Name.of(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.cannotBeNull("이름"));
    }

}
