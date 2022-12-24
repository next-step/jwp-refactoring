package kitchenpos.menu.application;

import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class MenuService {

    public static final String MENU_GROUP_NOT_EXIST_EXCEPTION_MESSAGE = "메뉴 그룹이 존재하지 않습니다.";

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductValidator menuProductValidator;

    public MenuService(final MenuRepository menuRepository, final MenuGroupRepository menuGroupRepository, MenuProductValidator menuProductValidator) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductValidator = menuProductValidator;
    }

    @Transactional
    public MenuResponse create(final MenuCreateRequest request) {
        Menu menu = new Menu(new Name(request.getName()), new Price(request.getPrice()), findMenuGroup(request), new MenuProducts(request.getMenuProducts()));
        menu.validate(menuProductValidator);
        return MenuResponse.of(menuRepository.save(menu));
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }

    private MenuGroup findMenuGroup(MenuCreateRequest request) {
        if (Objects.isNull(request.getMenuGroupId())) {
            throw new IllegalArgumentException(MENU_GROUP_NOT_EXIST_EXCEPTION_MESSAGE);
        }
        return menuGroupRepository.findById(request.getMenuGroupId()).orElseThrow(EntityNotFoundException::new);
    }
}

