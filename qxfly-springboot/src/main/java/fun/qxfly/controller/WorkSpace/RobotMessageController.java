package fun.qxfly.controller.WorkSpace;

import fun.qxfly.common.domain.po.Result;
import fun.qxfly.common.domain.vo.NavigationVO;
import fun.qxfly.service.WockSpace.WorkSpaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@Tag(name = "机器人消息", description = "机器人消息")
@RequestMapping("/webhook")
public class RobotMessageController {

    // 企业微信机器人Webhook配置
    private static final String WEBHOOK_URL =
            "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=13bcb327-09db-46be-864d-3b2f8838fe3f";

    // 创建WebClient实例（非阻塞式HTTP客户端）
    private final WebClient webClient = WebClient.create();

    /**
     * 处理企业微信群消息
     * @param requestBody 消息体JSON
     * @return 响应状态
     */
    @PostMapping("/webhook")
    public Mono<String> handleWeComMessage(@RequestBody Map<String, Object> requestBody) {
        // 解析消息内容
        Map<String, Object> sender = (Map<String, Object>) requestBody.get("sender");
        String userId = sender != null ? sender.get("userid").toString() : "unknown";
        String content = requestBody.get("content").toString();

        System.out.println("收到消息：" + content);

        // 构造回复消息
        String replyContent = "已收到[" + userId + "]的消息：" + content;

        // 发送回复到企业微信
        return webClient.post()
                .uri(WEBHOOK_URL)
                .bodyValue(Map.of(
                        "msgtype", "text",
                        "text", Map.of(
                                "content", replyContent,
                                "mentioned_list", new String[]{userId}
                        )
                ))
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(response -> System.out.println("回复成功：" + response));
    }

}
