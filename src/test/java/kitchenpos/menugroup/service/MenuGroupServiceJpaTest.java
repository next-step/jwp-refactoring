package kitchenpos.menugroup.service;

import kitchenpos.IntegrationTest;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MenuGroupServiceJpaTest extends IntegrationTest {

    @Autowired
    private MenuGroupServiceJpa menuGroupServiceJpa;


    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    @Test
    void addMenus() {

        MenuGroup 할인메뉴 = menuGroupServiceJpa.create(new MenuGroupRequest("할인메뉴"));

        assertThat(할인메뉴.getName()).isEqualTo("할인메뉴");
        assertThat(할인메뉴.getId()).isNotNull();
    }

    @DisplayName("매뉴 그룹의 목록을 조회할 수 있다.")
    @Test
    void listMenu() {
        List<MenuGroup> menus = menuGroupServiceJpa.list();
        assertThat(menus)
                .extracting("name")
                .containsExactly("두마리메뉴", "한마리메뉴", "순살파닭두마리메뉴", "신메뉴");
    }
}