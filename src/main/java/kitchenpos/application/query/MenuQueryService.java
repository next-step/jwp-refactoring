package kitchenpos.application.query;

import kitchenpos.dto.response.MenuViewResponse;
import kitchenpos.exception.EntityNotExistsException;
import kitchenpos.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MenuQueryService {
    private final MenuRepository menuRepository;

    public MenuQueryService(final MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public List<MenuViewResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(MenuViewResponse::of)
                .collect(Collectors.toList());
    }

    public MenuViewResponse findById(Long id) {
        return MenuViewResponse.of(menuRepository.findById(id)
                .orElseThrow(EntityNotExistsException::new));
    }
}
