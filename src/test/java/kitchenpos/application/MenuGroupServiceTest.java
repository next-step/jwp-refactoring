package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("메뉴 그룹 서비스에 관련한 기능")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;
    @InjectMocks
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
        given(menuGroupDao.save(any())).willReturn(세트메뉴);

        // When
        MenuGroup actual = menuGroupService.create(세트메뉴);

        // Then
        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals(세트메뉴.getId(), actual.getId()),
                () -> assertEquals(세트메뉴.getName(), actual.getName())
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
