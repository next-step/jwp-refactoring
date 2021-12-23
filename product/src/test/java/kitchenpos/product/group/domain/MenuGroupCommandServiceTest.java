package kitchenpos.product.group.domain;

import static kitchenpos.product.group.sample.MenuGroupSample.두마리메뉴;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 그룹 커맨드 서비스")
@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
class MenuGroupCommandServiceTest {

    @Mock
    private MenuGroupRepository groupRepository;

    @InjectMocks
    private MenuGroupCommandService commandService;

    @Test
    @DisplayName("메뉴 그룹 저장")
    void save() {
        //given
        MenuGroup 두마리메뉴 = 두마리메뉴();

        //when
        commandService.save(두마리메뉴);

        //then
        verify(groupRepository, only()).save(두마리메뉴);
    }
}
