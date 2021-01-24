package kitchenpos.menu.application;

import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MenuGroupServiceTest {
    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @AfterEach
    void cleanup() {
        menuGroupRepository.deleteAllInBatch();
    }

    @DisplayName("메뉴 그룹 등록")
    @Test
    void create() {
        MenuGroupResponse actual = menuGroupService.create(new MenuGroupRequest("후라이드메뉴"));

        assertThat(actual.getName()).isEqualTo("후라이드메뉴");
    }
}