package fun.qxfly.framework.config;

import fun.qxfly.common.enums.FilePaths;
import fun.qxfly.framework.interceptor.LoginCheckInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final LoginCheckInterceptor loginCheckInterceptor;
    private final IgnoreUrlProperties ignoreUrlProperties;
    public WebConfig(LoginCheckInterceptor loginCheckInterceptor, IgnoreUrlProperties ignoreUrlProperties) {
        this.loginCheckInterceptor = loginCheckInterceptor;
        this.ignoreUrlProperties = ignoreUrlProperties;
    }

    /**
     * 拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginCheckInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(ignoreUrlProperties.getUrls());
    }

    /**
     * 跨域配置
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
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
//        // 设置允许跨域的路径
//        registry.addMapping("/articleCover/**")
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
//        registry.addMapping("/articleAttachment/**")
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
    }

    /**
     * 虚拟路径配置，资源映射路径
     */
    @Value("${qxfly.file.path.VirtualPath}")
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
        /*系统背景*/
        registry.addResourceHandler("/systemBgImg/**")
                .addResourceLocations("file:///" + FilePaths.SYSTEM_SETTINGS_PATH.getPath());
    }
}
