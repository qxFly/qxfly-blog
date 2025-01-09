package fun.qxfly.quartz.service;

public interface ScheduledTaskService {
    /**
     * 清空每日浏览量
     *
     * @param daily
     * @return
     */
    boolean resetDailyViewTask(String daily);

    /**
     * 清空用户每日浏览记录
     *
     * @return
     */
    boolean resetUserDailyViewTask();

    /**
     * 清空用户每日点赞记录
     *
     * @return
     */
    boolean resetUserDailyLikeTask();

    /**
     * 定时刷新用户信息
     *
     * @return
     */
    Integer[] refreshUserInfoTask();

    /**
     * 定时清理超时验证码
     *
     * @return
     */
    Integer[] clearCaptchaTask();
}
