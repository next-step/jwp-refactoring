package kitchenpos.order.application;

import kitchenpos.order.dto.MenuIdsExistRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@Profile({"!test"})
public class MenuClient {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${menu.server.url}")
    private String menuServerUrl;


    public boolean isExistMenuByIds(List<Long> menuIds) {
        String requestUrl = menuServerUrl + "/api/menus/exist";
        MenuIdsExistRequest menuIdsExistRequest = MenuIdsExistRequest.of(menuIds);

        HttpStatus httpStatus = this.restTemplate
                .exchange(requestUrl, HttpMethod.GET, new HttpEntity<>(menuIdsExistRequest), ResponseEntity.class)
                .getStatusCode();

        return httpStatus.equals(HttpStatus.OK);
    }
}
