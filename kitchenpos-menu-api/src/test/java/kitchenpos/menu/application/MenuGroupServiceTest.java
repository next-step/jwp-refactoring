package kitchenpos.menu.application;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("MenuGroupService 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroup 양식;
    private MenuGroup 중식;

    @BeforeEach
    void setUp() {
        양식 = new MenuGroup("양식");
        중식 = new MenuGroup("중식");

        ReflectionTestUtils.setField(양식, "id", 1L);
        ReflectionTestUtils.setField(중식, "id", 2L);
    }

    @Test
    void 메뉴_그룹_등록() {
        given(menuGroupRepository.save(any(MenuGroup.class))).willReturn(양식);

        MenuGroupResponse response = menuGroupService.create(new MenuGroupRequest("양식"));

        assertThat(response).satisfies(res -> {
           assertEquals(양식.getId(), res.getId());
           assertEquals(양식.getName(), res.getName());
        });
    }

    @Test
    void 메뉴_그룹_목록_조회() {
        given(menuGroupRepository.findAll()).willReturn(Arrays.asList(양식, 중식));

        List<MenuGroupResponse> responses = menuGroupService.list();

        assertAll(
                () -> assertThat(responses).hasSize(2),
                () -> assertThat(responses.stream().map(MenuGroupResponse::getName)
                        .collect(toList())).containsExactly(양식.getName(), 중식.getName())
        );
    }
}
