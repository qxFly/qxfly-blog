package fun.qxfly.common.service;

public interface RSAService {

    /**
     * 根据公匙获取私匙
     *
     * @param publicKey
     * @return
     */
    String getPrivateKey(String publicKey);

    /**
     * 生成公匙
     *
     * @return
     */
    String generatePublicKey();
}
