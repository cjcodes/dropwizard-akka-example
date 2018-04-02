package com.cjcodes;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.*;
import javax.validation.constraints.*;

public class TestConfiguration extends Configuration {
    public final Long TIMEOUT = 5000L;
}
