package kitchenpos.domain.order.application;

import kitchenpos.domain.order.dto.MenuExistRequest;
import kitchenpos.exception.BusinessException;
import kitchenpos.exception.ErrorCode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class DefaultMenuClient implements MenuClient {

    private final RestTemplate template;

    public DefaultMenuClient() {
        this.template = new RestTemplate();
    }

    public void validateMenuExist(List<Long> menuIds) {
        MenuExistRequest request = new MenuExistRequest(menuIds);
        final HttpStatus statusCode = this.template
                .exchange("localhost:8081/api/menus/exist", HttpMethod.GET, new HttpEntity(request), ResponseEntity.class)
                .getStatusCode();

        if (!statusCode.is2xxSuccessful()) {
            throw new BusinessException(ErrorCode.MENU_NOT_EXIST);
        }
    }
}
