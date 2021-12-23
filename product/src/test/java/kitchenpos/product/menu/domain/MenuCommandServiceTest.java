package kitchenpos.product.menu.domain;

import static kitchenpos.product.menu.sample.MenuSample.이십원_후라이드치킨_두마리세트;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 커맨드 서비스")
@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
class MenuCommandServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private MenuCommandService commandService;


    @Test
    @DisplayName("상품 저장")
    void save() {
        //given
        Menu 이십원_후라이드치킨_두마리세트 = 이십원_후라이드치킨_두마리세트();

        //when
        commandService.save(이십원_후라이드치킨_두마리세트);

        //then
        verify(menuRepository, only()).save(이십원_후라이드치킨_두마리세트);
    }
}
