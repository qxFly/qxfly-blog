package fun.qxfly.common.mapper;

import org.apache.ibatis.annotations.*;

@Mapper
public interface RSAMapper {
    /**
     * 根据公匙获取私匙
     *
     * @param publicKey
     * @return
     */
    @Select("select privateKey from rsa_key where publicKey = #{publicKey}")
    String getPrivateKey(String publicKey);

    /**
     * 删除RSA密匙
     *
     * @param publicKey
     */
    @Delete("delete from rsa_key where publicKey = #{publicKey}")
    void deleteKey(String publicKey);

    /**
     * 保存RSA密匙
     *
     * @param publicKey
     * @param privateKey
     */
    @Insert("insert into rsa_key(publicKey, privateKey)value(#{publicKey},#{privateKey})")
    void saveKey(@Param("publicKey") String publicKey, @Param("privateKey") String privateKey);
}
