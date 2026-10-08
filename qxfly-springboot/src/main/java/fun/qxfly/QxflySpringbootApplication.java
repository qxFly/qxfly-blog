package fun.qxfly;

import fun.qxfly.controller.Message.WebSocketServer;
import fun.qxfly.framework.config.UserConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan({"fun.qxfly.mapper", "fun.qxfly.admin.mapper", "fun.qxfly.framework.mapper", "fun.qxfly.common.mapper", "fun.qxfly.quartz.mapper"})
@EnableScheduling
public class QxflySpringbootApplication {
    private static final Logger log = LoggerFactory.getLogger(QxflySpringbootApplication.class);

    public static void main(String[] args) {
        // 启动前先加载外部配置：JwtUtils 等类在类加载时读取系统属性，
        // 必须保证属性在 Spring 启动（类加载）之前已就绪，否则 SignKey 为 null
        boolean configOk = UserConfig.writeConfig();
        if (!configOk) {
            log.error("配置文件缺失或读取失败，启动已终止，请检查 data/qxfly-conf/config.json 后重启");
            return;
        }
        ConfigurableApplicationContext applicationContext = SpringApplication.run(QxflySpringbootApplication.class, args);
        // 启动时，设置socket的上下文
        WebSocketServer.setApplicationContext(applicationContext);
    }
}
