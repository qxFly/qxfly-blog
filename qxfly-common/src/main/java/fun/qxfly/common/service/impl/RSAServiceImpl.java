package fun.qxfly.common.service.impl;

import fun.qxfly.common.mapper.RSAMapper;
import fun.qxfly.common.service.RSAService;
import fun.qxfly.common.utils.RSAEncrypt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
public class RSAServiceImpl implements RSAService {

    private final RSAMapper rsaMapper;

    public RSAServiceImpl(RSAMapper rsaMapper) {
        this.rsaMapper = rsaMapper;
    }

    /**
     * 根据公匙获取私匙（查后即删，需保证原子性）
     *
     * @param publicKey
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String getPrivateKey(String publicKey) {
        String privateKey = rsaMapper.getPrivateKey(publicKey);
        rsaMapper.deleteKey(publicKey);
        return privateKey;
    }

    /**
     * 生成公匙
     *
     * @return
     */
    @Override
    public String generatePublicKey() {
        Map<Integer, String> keyMap;
        try {
            keyMap = RSAEncrypt.genKeyPair();
        } catch (Exception e) {
            log.error("生成RSA密钥对失败", e);
            return "系统错误";
        }
        rsaMapper.saveKey(keyMap.get(0), keyMap.get(1));
        return keyMap.get(0);
    }
}
