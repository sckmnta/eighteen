package models.lombok;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)

public class RegisterResponseLombokModel {
    String token;
    int id;
    int statusCode;
    String jsonSchema;
    String username;
    String job;
    String error;
}
