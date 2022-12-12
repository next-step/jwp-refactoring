package kitchenpos.menu.application;

import kitchenpos.menu.dao.MenuGroupDao;
import kitchenpos.menu.dto.MenuGroupCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("MenuGroupServiceTest")
@SpringBootTest
@Transactional
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @BeforeEach
    void setUp() {
        menuGroupService = new MenuGroupService(menuGroupDao);
    }

    @DisplayName("메뉴 그룹 생성")
    @ParameterizedTest
    @ValueSource(strings = {"메뉴그룹 A"})
    void create(String name) {
        assertThat(menuGroupService.create(new MenuGroupCreateRequest(name)).getName())
                .isEqualTo(name);
    }

    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void name() {
        assertThat(menuGroupService.list()).hasSize(4);
    }
}
