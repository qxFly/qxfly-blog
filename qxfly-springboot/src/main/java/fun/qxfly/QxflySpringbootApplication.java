package fun.qxfly;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan({"fun.qxfly.mapper", "fun.qxfly.admin.mapper","fun.qxfly.framework.mapper","fun.qxfly.common.mapper"})
@EnableScheduling
public class QxflySpringbootApplication {
    public static void main(String[] args) {
        SpringApplication.run(QxflySpringbootApplication.class, args);
    }

}
