package fun.qxfly.framework.mapper;

import fun.qxfly.common.domain.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ScheduledTaskMapper {
    /**
     * 清空每日浏览量
     *
     * @return
     */
    @Update("update daily_view set daily_views = 0")
    boolean resetDailyView();

    /**
     * 清空每周浏览量
     *
     * @return
     */
    @Update("update daily_view set Weekly_views = 0")
    boolean resetWeeklyViews();

    /**
     * 清空每月浏览量
     *
     * @return
     */
    @Update("update daily_view set Monthly_views = 0")
    boolean resetMonthlyViews();

    /**
     * 清空用户每日浏览记录
     *
     * @return
     */
    @Delete("delete from user_article_daily_view")
    boolean resetUserDailyViewTask();

    /**
     * 清空用户每日点赞记录
     *
     * @return
     */
    @Delete("delete from user_article_daily_like")
    boolean resetUserDailyLikeTask();

    /**
     * 定时清理超时验证码
     *
     * @param time
     * @return
     */
    @Delete("delete from captcha where createTime < #{date} - 1000 * 60 * 5")
    Integer clearCaptchaTask(long time);

    /**
     * 获取所有用户
     *
     * @return
     */
    @Select("select id from user")
    List<User> listAllUser();
}
