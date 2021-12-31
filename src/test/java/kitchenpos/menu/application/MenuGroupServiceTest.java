package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴그룹 등록")
    @Test
    void 메뉴그룹_등록() {
        // given
        MenuGroupRequest request = new MenuGroupRequest("추천메뉴");

        // when
        MenuGroupResponse actual = menuGroupService.create(request);

        // then
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo(request.getName());
    }

    @DisplayName("메뉴그룹 조회")
    @Test
    void 메뉴그룹_목록_조회() {
        // given
        int savedMenuSize = 4;

        // when
        List<MenuGroupResponse> actual = menuGroupService.list();

        // then
        assertThat(actual.size()).isEqualTo(savedMenuSize);
    }

}
