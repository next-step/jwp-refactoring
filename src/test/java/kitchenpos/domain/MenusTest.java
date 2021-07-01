package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MenusTest {

    @Test
    void size() {
        // given
        Menus menus = new Menus(Arrays.asList(new Menu(),new Menu(),new Menu()));

        // when
        int size = menus.size();

        // then
        assertThat(size).isEqualTo(3);
    }
}