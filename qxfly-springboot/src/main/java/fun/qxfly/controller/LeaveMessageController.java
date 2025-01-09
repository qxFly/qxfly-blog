package fun.qxfly.controller;

import com.github.pagehelper.PageInfo;
import fun.qxfly.common.domain.DTO.LeaveMessageDTO;
import fun.qxfly.common.domain.entity.LeaveMessage;
import fun.qxfly.common.domain.po.Result;
import fun.qxfly.service.LeaveMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@Tag(name = "留言", description = "留言")
@RequestMapping("/index")
public class LeaveMessageController {
    private final LeaveMessageService leaveMessageService;

    public LeaveMessageController(LeaveMessageService leaveMessageService) {
        this.leaveMessageService = leaveMessageService;
    }

    /**
     * 分页获取留言列表
     *
     * @return
     */
    @Operation(description = "分页获取留言列表", summary = "分页获取留言列表")
    @PostMapping("/listLeaveMessage")
    public Result listLeaveMessage(@RequestBody LeaveMessageDTO leaveMessageDTO) {
        PageInfo<LeaveMessage> leaveMessagePageInfo = leaveMessageService.listLeaveMessage(leaveMessageDTO);
        return Result.success(leaveMessagePageInfo);
    }


    /**
     * 发送留言
     */
    @Operation(description = "发送留言", summary = "发送留言")
    @PostMapping("/sendLeaveMessage")
    public Result sendLeaveMessage(@RequestBody LeaveMessage leaveMessage) {
       boolean f =  leaveMessageService.sendLeaveMessage(leaveMessage);
        return Result.success();
    }

    /**
     * 删除留言
     */
    @Operation(description = "删除留言", summary = "删除留言")
    @GetMapping("/deleteLeaveMessage")
    public Result deleteLeaveMessage(Integer id) {
        boolean f =  leaveMessageService.deleteLeaveMessage(id);
        return Result.success();
    }
}
