package com.jeet.schedular.handler;

import com.jeet.schedular.dto.Filter;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Set;

@Slf4j
@Component
public class ValidationHandler {

    private final Validator validator;

    public ValidationHandler() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }


    public Mono<ServerResponse> validate(ServerRequest request) {

        return request.queryParam("age").map(age -> {
            try {
                int ageValue = Integer.parseInt(age);
                if (ageValue < 0) {
                    return ServerResponse.badRequest().bodyValue("Age should be positive");
                }
                return ServerResponse.ok().bodyValue("Valid age: " + ageValue);
            } catch (NumberFormatException e) {
                log.info("Error occurred: {}", e.getMessage(), e);
                return ServerResponse.badRequest().bodyValue("Age must be a valid integer.");
            }
        }).orElse(ServerResponse.badRequest().bodyValue("Missing required parameter: age."));

    }

    public Mono<ServerResponse> parseQueryParams(ServerRequest request) {
        String keyword = request.queryParam("keyword").orElse("");
        int page = Integer.parseInt(request.queryParam("page").orElse("0"));
        int size = Integer.parseInt(request.queryParam("size").orElse("0"));

        // Create a Filter object
        Filter filter = new Filter();
        filter.setKeyword(keyword);
        filter.setPage(page);
        filter.setSize(size);

        Set<ConstraintViolation<Filter>> validations = validator.validate(filter);

        if(!validations.isEmpty()) {
            String errorMessage = validations.stream()
                    .map(ConstraintViolation::getMessage)
                    .reduce((m1, m2) -> m1 + ";" + m2)
                    .orElse("validation failed");

            return ServerResponse.badRequest().bodyValue(errorMessage);
        }
        return ServerResponse.ok().bodyValue("Parsed filter: " + filter.toString());
    }

}
