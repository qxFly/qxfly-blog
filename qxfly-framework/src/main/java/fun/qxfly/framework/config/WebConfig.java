package fun.qxfly.framework.config;

import fun.qxfly.framework.interceptor.LoginCheckInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final LoginCheckInterceptor loginCheckInterceptor;

    public WebConfig(LoginCheckInterceptor loginCheckInterceptor) {
        this.loginCheckInterceptor = loginCheckInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginCheckInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/gs",//获取公匙
                        "/login", //登陆
                        "/logout", //退出
                        "/register", //注册
                        "/updateLoginStatue", //检测更用户信息
                        "/getImage", //图片列表
                        "/listFile", //下载页文件列表
                        "/listSite", //网站列表
                        "/download/**", // 下载
                        "/v3/**", //api文档
                        "/swagger-ui/**",//api文档
                        "/fileList", //左侧栏文件列表
                        "/saying/**", //一言
                        "/userAvatar/**", //用户头像
                        "/user/getUserInfo", //获取用户信息
                        "/user/getSuggestAuthor",// 获取推荐作者
                        "/user/findPassword",// 找回密码
                        "/user/sendCode",// 找回密码
                        "/article/getArticles", //文章列表
                        "/article/getArticleById", //文章详情
                        "/articleCover/**", //文章封面
                        "/articleImage/**", //文章内容图片
                        "/article/getArticleComments", //获取文章评论
                        "/article/addArticleView", // 增加浏览量
                        "/article/getTags", // 获取标签
                        "/article/getClassifies", // 获取分类
                        "/article/getArticleAttachment", //文章附件
                        "/articleAttachment/**", //附件下载
                        "/article/getCollectionArticles" //获取用户收藏
                );
    }
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        // 设置允许跨域的路径
//        registry.addMapping("/**")
//                // 设置允许跨域请求的域名
//                .allowedOriginPatterns("*")
//                // 是否允许cookie
//                .allowCredentials(true)
//                // 设置允许的请求方式
//                .allowedMethods("GET", "POST", "DELETE", "PUT")
//                // 设置允许的header属性
//                .allowedHeaders("*")
//                // 跨域允许时间
//                .maxAge(3600);
//    }
}
