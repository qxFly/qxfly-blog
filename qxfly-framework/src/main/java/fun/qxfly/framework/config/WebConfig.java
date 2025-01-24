package fun.qxfly.framework.config;

import fun.qxfly.common.enums.FilePaths;
import fun.qxfly.framework.interceptor.LoginCheckInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final LoginCheckInterceptor loginCheckInterceptor;

    public WebConfig(LoginCheckInterceptor loginCheckInterceptor) {
        this.loginCheckInterceptor = loginCheckInterceptor;
    }

    /**
     * 拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] excludePathPatterns = {
                "/favicon.ico", //网站图标
                "/gs",//获取公匙
                "/login", //登陆
                "/logout", //退出
                "/register", //注册
                "/updateLoginStatue", //检测更用户信息
                "/getImage", //图片列表
                "/index/listSite", //网站列表
                "/index/listIndexNav", //首页导航
                "/download/**", // 下载
                "/v3/**", //api文档
                "/swagger-ui/**",//api文档
                "/doc.html/**",
                "/webjars/**",
                "/userAvatar/**", //用户头像
                "/userBgImg/**", //用户背景图片
                "/user/getUserInfo", //获取用户信息
                "/user/getSuggestAuthor",// 获取推荐作者
                "/user/findPassword",// 找回密码
                "/user/sendCode",// 找回密码
                "/article/listArticles", //文章列表
                "/article/getArticleById", //文章详情
                "/articleCover/**", //文章封面
                "/articleImage/**", //文章内容图片
                "/article/getArticleComments", //获取文章评论
                "/article/addArticleView", // 增加浏览量
                "/article/getTags", // 获取标签
                "/article/getClassifies", // 获取分类
                "/article/getArticleAttachment", //文章附件
                "/articleAttachment/**", //附件下载
                "/article/getCollectionArticles", //获取用户收藏
                "/index/listLeaveMessage", //列出留言
                "/index/sendLeaveMessage", //发送留言
                "/index/deleteLeaveMessage"};//删除留言
        registry.addInterceptor(loginCheckInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(excludePathPatterns);
    }

    /**
     * 跨域配置
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 设置允许跨域的路径
        registry.addMapping("/articleCover/**")
                // 设置允许跨域请求的域名
                .allowedOriginPatterns("*")
                // 是否允许cookie
                .allowCredentials(true)
                // 设置允许的请求方式
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                // 设置允许的header属性
                .allowedHeaders("*")
                // 跨域允许时间
                .maxAge(3600);
        registry.addMapping("/articleAttachment/**")
                // 设置允许跨域请求的域名
                .allowedOriginPatterns("*")
                // 是否允许cookie
                .allowCredentials(true)
                // 设置允许的请求方式
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                // 设置允许的header属性
                .allowedHeaders("*")
                // 跨域允许时间
                .maxAge(3600);
    }

    /**
     * 虚拟路径配置，资源映射路径
     */
    @Value("${VirtualPath}")
    private String filePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /*
          资源映射路径
          addResourceHandler：访问映射路径
          addResourceLocations：资源绝对路径
         */
        /*资源下载*/
        registry.addResourceHandler("/download/**")
                .addResourceLocations(filePath);
        /*用户头像*/
        registry.addResourceHandler("/userAvatar/**")
                .addResourceLocations("file:///" + FilePaths.USER_AVATAR_PATH.getPath());
        /*用户背景图片*/
        registry.addResourceHandler("/userBgImg/**")
                .addResourceLocations("file:///" + FilePaths.USER_BACKGROUND_IMAGE_PATH.getPath());
        /*文章封面*/
        registry.addResourceHandler("/articleCover/**")
                .addResourceLocations("file:///" + FilePaths.ARTICLE_COVER_PATH.getPath());
        /*文章内容图片*/
        registry.addResourceHandler("/articleImage/**")
                .addResourceLocations("file:///" + FilePaths.ARTICLE_IMAGE_PATH.getPath());
        /*文章附件*/
        registry.addResourceHandler("/articleAttachment/**")
                .addResourceLocations("file:///" + FilePaths.ARTICLE_ATTACHMENT_PATH.getPath());
    }
}
