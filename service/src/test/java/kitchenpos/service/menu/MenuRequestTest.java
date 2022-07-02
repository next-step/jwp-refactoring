package kitchenpos.service.menu;

import kitchenpos.domain.menu.InvalidNameException;
import kitchenpos.domain.menu.InvalidPriceException;
import kitchenpos.service.menu.dto.MenuRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuRequestTest {
    @Test
    @DisplayName("메뉴 가격이 음수면 요청을 생성할 수 없다")
    void create_failed_1() {
        assertThatThrownBy(() -> new MenuRequest("name", -1L, 0L, Collections.emptyList())).isExactlyInstanceOf(
                InvalidPriceException.class);
    }

    @Test
    @DisplayName("메뉴 이름이 비어있으면 요청을 생성할 수 없다")
    void create_failed_2() {
        assertThatThrownBy(() -> new MenuRequest(null, 10000L, 0L, Collections.emptyList())).isExactlyInstanceOf(
                InvalidNameException.class);
    }

}
