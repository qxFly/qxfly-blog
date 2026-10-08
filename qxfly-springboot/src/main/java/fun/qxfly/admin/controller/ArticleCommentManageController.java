package fun.qxfly.admin.controller;

import fun.qxfly.admin.service.ArticleCommentManageService;
import com.github.pagehelper.PageInfo;
import fun.qxfly.common.domain.po.Result;
import fun.qxfly.common.domain.entity.Comment;
import fun.qxfly.common.domain.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/manage")
@Tag(name = "文章评论管理")
@Tag(name = "管理员")
public class ArticleCommentManageController {

    final
    ArticleCommentManageService articleCommentManageService;

    public ArticleCommentManageController(ArticleCommentManageService articleCommentManageService) {
        this.articleCommentManageService = articleCommentManageService;
    }

    /**
     * 文章评论审核
     * @param comment
     * @return
     */
    @Operation(description = "文章评论审核", summary = "文章评论审核")
    @PostMapping("/articleCommentVerify")
    public Result articleCommentVerify(@RequestBody Comment comment) {
        if (articleCommentManageService.articleCommentVerify(comment.getId(), comment.getVerify())) {
            return Result.success();
        } else {
            return Result.error("");
        }
    }

    /**
     * 删除文章评论（含子评论）
     *
     * @param comment
     * @return
     */
    @Operation(description = "删除文章评论", summary = "删除文章评论")
    @PostMapping("/deleteArticleComment")
    public Result deleteArticleComment(@RequestBody Comment comment) {
        if (articleCommentManageService.deleteArticleComment(comment.getId())) {
            return Result.success();
        } else {
            return Result.error("");
        }
    }

    /**
     * 批量审核文章评论
     *
     * @param map ids: 评论id列表, verify: 审核状态(1待审核 2不通过 3通过)
     * @return
     */
    @Operation(description = "批量审核文章评论", summary = "批量审核文章评论")
    @PostMapping("/batchArticleCommentVerify")
    public Result batchArticleCommentVerify(@RequestBody Map<String, Object> map) {
        Object idsObj = map.get("ids");
        Object verifyObj = map.get("verify");
        if (!(idsObj instanceof List) || ((List<?>) idsObj).isEmpty() || !(verifyObj instanceof Number)) {
            return Result.error("参数错误");
        }
        Integer verify = ((Number) verifyObj).intValue();
        if (verify < 1 || verify > 3) {
            return Result.error("参数错误");
        }
        List<Integer> ids = ((List<?>) idsObj).stream()
                .filter(o -> o instanceof Number)
                .map(o -> ((Number) o).intValue())
                .collect(Collectors.toList());
        if (ids.isEmpty()) {
            return Result.error("参数错误");
        }
        if (articleCommentManageService.batchArticleCommentVerify(ids, verify)) {
            return Result.success();
        } else {
            return Result.error("操作失败");
        }
    }

    /**
     * 搜索评论
     *
     * @param map
     * @return
     */
    @Operation(description = "搜索评论", summary = "搜索评论")
    @PostMapping("/listArticleComment")
    public Result listArticleComment(@RequestBody Map<String, Object> map) {
        map.forEach((a, b) -> {
//            log.info("{}={}", a, b);
        });
        Comment comment = new Comment();
        comment.setId((Integer) map.get("comId"));
        comment.setArticleId((Integer) map.get("articleId"));
        comment.setContent((String) map.get("content"));
        comment.setParentCommentId((Integer) map.get("parentCommentId"));
        User user = new User();
        user.setId((Integer) map.get("userId"));
        user.setUsername((String) map.get("username"));
        comment.setUser(user);
        comment.setToUserId((Integer) map.get("toUserId"));
        comment.setToUsername((String) map.get("toUsername"));
        comment.setVerify((Integer) map.get("verify"));
        PageInfo<Comment> pageInfo = articleCommentManageService.searchArticleComment((Integer) map.get("currPage"), (Integer) map.get("pageSize"), comment, (String) map.get("createTimeStart"), (String) map.get("createTimeEnd"));
        return Result.success(pageInfo);
    }

}
