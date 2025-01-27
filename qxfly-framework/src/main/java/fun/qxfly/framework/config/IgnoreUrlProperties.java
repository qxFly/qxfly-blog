package fun.qxfly.framework.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
@ConfigurationProperties(prefix = "qxfly.jwt.ignore")
public class IgnoreUrlProperties {
    private String[] urls;
}
