package kitchenpos.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.Validator;

@SpringBootTest
public class ValidateBase {
    @Autowired
    protected Validator validator;
}
