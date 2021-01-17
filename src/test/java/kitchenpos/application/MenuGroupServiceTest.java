package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

@DisplayName("메뉴 그룹 서비스에 관련한 기능")
@SpringBootTest
class MenuGroupServiceTest {
    @Autowired
    private MenuGroupDao menuGroupDao;
    @Autowired
    private MenuGroupService menuGroupService;

    private MenuGroup 세트메뉴;

    @BeforeEach
    void beforeEach() {
        세트메뉴 = new MenuGroup();
        세트메뉴.setId(1L);
        세트메뉴.setName("세트메뉴");
    }

    @DisplayName("`메뉴 그룹`을 생성한다.")
    @Test
    void createMenuGroup() {
        // Given
        MenuGroupRequest request = new MenuGroupRequest("세트메뉴");

        // When
        MenuGroup actual = menuGroupService.create(request);

        // Then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(request.getName())
        );
    }

    @DisplayName("모든 `메뉴 그룹` 목록을 조회한다.")
    @Test
    void findAllMenuGroups() {
        // Given
        MenuGroup 메인메뉴 = new MenuGroup();
        메인메뉴.setId(2L);
        메인메뉴.setName("메인메뉴");
        given(menuGroupDao.findAll()).willReturn(Arrays.asList(세트메뉴, 메인메뉴));

        // When
        List<MenuGroup> actual = menuGroupService.list();

        // Then
        assertAll(
                () -> assertThat(actual).extracting(MenuGroup::getId)
                        .containsExactly(세트메뉴.getId(), 메인메뉴.getId()),
                () -> assertThat(actual).extracting(MenuGroup::getName)
                        .containsExactly(세트메뉴.getName(), 메인메뉴.getName())
        );
    }
}
