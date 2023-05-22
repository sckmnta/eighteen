package models.lombok;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)

public class RegisterBodyLombockModel {
    String username, job, email, password, statusCode, jsonSchema, error;
}
