package kitchenpos.menugroup.application;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class MenuGroupServiceRealTest {
    @Autowired
    MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성할수 있다")
    @Test
    void createTest() {
        //given
        MenuGroup expect = new MenuGroup();
        expect.setName("JPA메뉴그룹");

        //when
        MenuGroup result = menuGroupService.create(expect);

        //then
        assertThat(result.getName()).isEqualTo(expect.getName());
    }

    @DisplayName("메뉴 그룹 목록을 조회할 수 있다 ")
    @Test
    void findAll() {

        //given
        MenuGroup expect = new MenuGroup();
        expect.setName("JPA메뉴그룹");

        menuGroupService.create(expect);

        //when
        List<MenuGroupResponse> result = menuGroupService.list();

        //then
        assertThat(result.size()).isGreaterThan(0);
        assertThat(result.get(result.size()-1).getName()).isEqualTo(expect.getName());
    }


}
