package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MenusTest {

    @Test
    void size() {
        // given
        List<Menu> menuList = Arrays.asList(new Menu(), new Menu(), new Menu());
        Menus menus = new Menus(menuList);

        // when
        int size = menus.size();

        // then
        assertThat(size).isEqualTo(menuList.size());
    }
}