package fun.qxfly.quartz.service.impl;

import fun.qxfly.common.domain.entity.User;
import fun.qxfly.common.service.CUserInfoService;
import fun.qxfly.quartz.mapper.ScheduledTaskMapper;
import fun.qxfly.quartz.service.ScheduledTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ScheduledTaskServiceImpl implements ScheduledTaskService {

    private final ScheduledTaskMapper scheduledTaskMapper;
    private final CUserInfoService cUserInfoService;
    public ScheduledTaskServiceImpl(ScheduledTaskMapper scheduledTaskMapper, CUserInfoService cUserInfoService) {
        this.scheduledTaskMapper = scheduledTaskMapper;
        this.cUserInfoService = cUserInfoService;
    }

    /**
     * 清空每日浏览量
     *
     * @param type
     * @return
     */
    @Override
    public boolean resetDailyViewTask(String type) {
        if (type.equals("daily")) {
            return scheduledTaskMapper.resetDailyView();
        } else if (type.equals("weekly")) {
            return scheduledTaskMapper.resetWeeklyViews();
        } else {
            return scheduledTaskMapper.resetMonthlyViews();
        }
    }

    /**
     * 清空用户每日浏览记录
     *
     * @return
     */
    @Override
    public boolean resetUserDailyViewTask() {
        return scheduledTaskMapper.resetUserDailyViewTask();
    }

    /**
     * 清空用户每日点赞记录
     *
     * @return
     */
    @Override
    public boolean resetUserDailyLikeTask() {
        return scheduledTaskMapper.resetUserDailyLikeTask();
    }

    /**
     * 定时刷新用户信息
     *
     * @return
     */
    @Override
    public Integer[] refreshUserInfoTask() {
        List<User> userList = scheduledTaskMapper.listAllUser();
        Integer[] i = {userList.size(), 0};
        for (User user : userList) {
            if (cUserInfoService.refreshUserInfoTask(user.getId())) {
                i[1]++;
            }
        }
        return i;
    }

    /**
     * 定时清理超时验证码
     *
     * @return
     */
    @Override
    public Integer[] clearCaptchaTask() {
        Integer i = scheduledTaskMapper.clearCaptchaTask(new Date().getTime());
        return new Integer[0];
    }
}
