package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.domain.MenuGroup;
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
    void 생성시_생성한메뉴그룹반환() {
        final MenuGroup menuGroup = menuGroupService.create(new MenuGroup("점심 특선 메뉴"));
        assertThat(menuGroup.getName()).isEqualTo("점심 특선 메뉴");
    }

    @Test
    void 조회시_존재하는메뉴목록반환() {
        assertThat(menuGroupService.list()).isNotEmpty();
    }

}
