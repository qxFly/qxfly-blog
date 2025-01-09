package fun.qxfly.framework.config;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import fun.qxfly.common.enums.FilePaths;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Data
public class UserConfig {
    /*jwt令牌密匙*/
    private String JwtSignKey = "qxfly";
    /*jwt令牌失效时长*/
    private Long JwtTimeout = 2592000000L;
    /*GithubAPI*/
    private String GithubAPI = "";
    /*邀请码*/
    private String Invite = "qxfly";
    /*阿里云AccessKeyId*/
    private String Aliyun_AccessKeyId = "";
    /*阿里云AccessKeySecret*/
    private String Aliyun_AccessKeySecret = "";
    /*阿里云短信服务签名名称*/
    private String Aliyun_Dysmsapi_SignName = "";
    /*阿里云短信服务模板代码*/
    private String Aliyun_Dysmsapi_TemplateCode = "";

    private static ConfigurableApplicationContext configurableApplicationContext;

    public static void setApplicationContext(ConfigurableApplicationContext configurableApplicationContext) {
        UserConfig.configurableApplicationContext = configurableApplicationContext;
    }

    public static boolean writeConfig() {
        // 目录路径
        File dir = new File(FilePaths.USER_CONFIG_PATH.getPath());
        // 配置文件路径
        File userConfigFile = new File(dir + "/config.json");
        //目录不存在
        if (!userConfigFile.exists()) {
            log.warn("配置文件不存在...");
            try {
                log.warn("正在创建配置文件...");
                dir.mkdirs();
                userConfigFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
                log.error("配置文件创建失败！请删除相关目录文件后重试：{}", userConfigFile.getAbsolutePath());
                configurableApplicationContext.close();
                return false;
            }
            try (OutputStream outputStream = new FileOutputStream(userConfigFile)) {
                log.warn("正在写出配置文件...");
                UserConfig userConfig = new UserConfig();
                byte[] bytes = JSONObject.toJSONBytes(userConfig, SerializerFeature.PrettyFormat);
                outputStream.write(bytes);
                log.warn("配置文件写出完成，请先填写配置文件后再重新启动程序！");
                configurableApplicationContext.close();
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                log.error("配置文件写出失败！请删除相关目录文件后重试：{}", userConfigFile.getAbsolutePath());
                configurableApplicationContext.close();
                return false;
            }
        } else {
            return readUserConfig(userConfigFile);
        }
    }

    private static boolean readUserConfig(File userConfigFile) {
        try (InputStream inputStream = new FileInputStream(userConfigFile);
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            log.warn("检测到配置文件，读取配置文件");
            /*读取配置文件*/

            String line;
            StringBuilder config = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                config.append(line);
            }
            /*把 json 格式转化为 UserConfig 对象*/
            JSONObject jsonObject = JSONObject.parseObject(config.toString());
            UserConfig userConfig = jsonObject.toJavaObject(UserConfig.class);
            /*通过反射获取 UserConfig 对象的成员变量*/
            Class<? extends UserConfig> userConfigClass = userConfig.getClass();
            Field[] fields = userConfigClass.getDeclaredFields();
            for (Field field : fields) {
                String fieldName = field.getName();
                /*排除log对象*/
                List<String> exclude = Arrays.asList("log", "configurableApplicationContext");
                if (exclude.contains(fieldName)) continue;
                /*通过反射获取 UserConfig 对象的成员变量的 get 方法*/
                Method method = userConfigClass.getMethod("get" + field.getName());
                /*通过反射获取成员变量的值*/
                String invoke = method.invoke(userConfig).toString();
                /*设置系统属性，以便其他类读取*/
                System.setProperty(fieldName, invoke);
            }
            log.warn("读取配置文件成功！");
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("读取配置文件失败!");
            log.warn("请检查文件格式 或 删除文件后重新启动生成配置文件！");
            configurableApplicationContext.close();
            return false;
        }
        return true;
    }
}
