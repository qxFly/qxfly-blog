package fun.qxfly.admin.controller;

import fun.qxfly.admin.service.LeaveMessageManegeService;
import fun.qxfly.common.domain.entity.LeaveMessage;
import fun.qxfly.common.domain.po.Result;
import fun.qxfly.service.LeaveMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/manage")
@Tag(name = "管理员")
@Tag(name = "留言管理")
public class LeaveMessageManageController {
    private final LeaveMessageManegeService leaveMessageManegeService;
    private final LeaveMessageService leaveMessageService;
    public LeaveMessageManageController(LeaveMessageManegeService leaveMessageManegeService, LeaveMessageService leaveMessageService) {
        this.leaveMessageManegeService = leaveMessageManegeService;
        this.leaveMessageService = leaveMessageService;
    }

    /**
     * 删除留言
     *
     * @return
     */
    @Operation(description = "删除留言", summary = "删除留言")
    @PostMapping("/deleteLeaveMessage")
    public Result deleteLeaveMessage(@RequestBody LeaveMessage leaveMessage) {
        boolean f = leaveMessageService.deleteLeaveMessage(leaveMessage.getId());
        if (f) return Result.success();
        return Result.error("删除失败");
    }
}
