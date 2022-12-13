package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    MenuGroupService menuGroupService;

    @Test
    @DisplayName("메뉴 그룹 생성 테스트")
    void createTest(){
        // given

        // when
        MenuGroup savedMenuGroup = menuGroupService.create(new MenuGroup("점심특선"));

        // then
        assertThat(savedMenuGroup.getId()).isNotNull();
    }

    @Test
    @DisplayName("메뉴 그룹 전체 조회 테스회")
    void listTest(){
        // given
        MenuGroup group1 = menuGroupService.create(new MenuGroup("점심특선"));
        MenuGroup group2 = menuGroupService.create(new MenuGroup("저녁특선"));
        MenuGroup group3 = menuGroupService.create(new MenuGroup("안주특선"));

        // when
        List<MenuGroup> groups = menuGroupService.list();

        // then
        List<Long> groupIds = groups.stream()
                .map(MenuGroup::getId)
                .collect(Collectors.toList());
        assertThat(groupIds).contains(
                group1.getId(), group2.getId(), group3.getId()
        );
    }
}