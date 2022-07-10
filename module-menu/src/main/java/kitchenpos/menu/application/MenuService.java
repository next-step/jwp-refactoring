package kitchenpos.menu.application;

import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menu.domain.Menus;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.mapper.MenuMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuMapper menuMapper;

    public MenuService(final MenuRepository menuRepository, final MenuMapper menuValidator) {
        this.menuRepository = menuRepository;
        this.menuMapper = menuValidator;
    }

    @Transactional
    public MenuResponse create(final MenuCreateRequest request) {
        return MenuResponse.from(
                menuRepository.save(
                        menuMapper.mapFrom(request)
                )
        );
    }

    public List<MenuResponse> listResponse() {
        return new Menus(menuRepository.findAll()).toResponse();
    }
}
