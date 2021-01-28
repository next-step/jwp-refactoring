package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;


    @Test
    void menu_create_test() {
        //given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("양식");

        //when
        MenuGroup createdMenuGroup = menuGroupService.create(menuGroup);

        //then
        Assertions.assertThat(createdMenuGroup.getId()).isNotNull();
        Assertions.assertThat(createdMenuGroup.getName()).isEqualTo(menuGroup.getName());
    }
}
