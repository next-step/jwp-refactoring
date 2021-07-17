package kitchenpos.menugroup.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

@DisplayName("메뉴그룹 테스트")
class MenuGroupTest {

    @TestFactory
    @DisplayName("메뉴그룹 생성 오류 발생")
    List<DynamicTest> validate_name() {
        return Arrays.asList(
                dynamicTest("메뉴그룹 이름 Null 입력시 오류 발생.", () ->
                        assertThatThrownBy(() -> new MenuGroup(null))
                            .isInstanceOf(IllegalArgumentException.class)
                            .hasMessage("이름은 Null이거나 공백일 수 없습니다.")
                ),
                dynamicTest("메뉴그룹 이름 공백 입력시 오류 발생.", () ->
                    assertThatThrownBy(() -> new MenuGroup(""))
                            .isInstanceOf(IllegalArgumentException.class)
                            .hasMessage("이름은 Null이거나 공백일 수 없습니다.")
                )
        );
    }
}
