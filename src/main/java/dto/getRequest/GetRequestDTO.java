package dto.getRequest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dto.helpers.Args;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetRequestDTO {
    @JsonProperty("args")
    private Args args;

    public Args getArgs() {
        return args;
    }

    public void setArgs(Args args) {
        this.args = args;
    }
}
