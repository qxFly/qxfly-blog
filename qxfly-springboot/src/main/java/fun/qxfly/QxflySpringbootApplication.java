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
        ConfigurableApplicationContext applicationContext = SpringApplication.run(QxflySpringbootApplication.class, args);
        // 启动时，设置socket的上下文
        WebSocketServer.setApplicationContext(applicationContext);
        // 启动时，初始化配置文件
        UserConfig.setApplicationContext(applicationContext);
        boolean b = UserConfig.writeConfig();
        if (!b) log.warn("应用已关闭，请重启，在这之后的报错请忽视");
    }
}
