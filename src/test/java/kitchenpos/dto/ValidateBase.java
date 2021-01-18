package kitchenpos.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ValidateBase {
    @Autowired
    private Validator validator;

    protected <T>void validate(T obj) {
        assertThat(validator.validate(obj)).isNotEmpty();
    }
}
