package dev.brianweloba.product_service.util;

import org.springframework.stereotype.Service;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Service
public class BooleanValidationUtils {

    /**
     * Parses a string to boolean. Accepts "true" (case insensitive) as true,
     * and anything else as false.
     *
     * @param value The string value to parse.
     * @return True if the value is "true" (case insensitive), false otherwise.
     */
    public boolean parseBoolean(String value) {
        return Boolean.parseBoolean(value);
    }

    /**
     * Validates if a string represents a valid boolean value ("true" or "false").
     *
     * @param value The string value to validate.
     * @return True if the value is "true" or "false" (case insensitive), false
     *         otherwise.
     */
    public boolean isValidBoolean(String value) {
        return "true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value);

    }
}
