package kitchenpos.common.domain;

import kitchenpos.common.exception.InputDataErrorCode;
import kitchenpos.common.exception.InputDataException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Name(이름) 단위 테스트")
class NameTest {

    @Test
    @DisplayName("이름이 없으면 에러 처리")
    void saveNameNullErrorTest(){
        assertThatThrownBy(() -> {
                    new Name(null);
                }).isInstanceOf(InputDataException.class)
                .hasMessageContaining(InputDataErrorCode.THE_NAME_CAN_NOT_EMPTY.errorMessage());
    }

    @Test
    @DisplayName("이름 생성 테스트")
    void saveNameTest(){
          Name createdName = new Name("마약치킨");
        assertThat(createdName.getName()).isEqualTo("마약치킨");
    }
}
