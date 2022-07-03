package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.menu.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql({"classpath:truncate.sql"})
public class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    public void createMenuGroup() {
        //given
        MenuGroup menuGroup = new MenuGroup(1L,"메뉴그룹");
        //when
        MenuGroup result = menuGroupService.create(menuGroup);
        //then
        assertThat(result).isNotNull();
    }

    @Test
    public void getMenuGroups(){
        //given
        menuGroupService.create(new MenuGroup(1L,"메뉴그룹"));
        //when
        List<MenuGroup> results = menuGroupService.list();
        //then
        assertThat(results).hasSize(1);
    }
}
