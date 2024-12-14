/* 产引入jsencrypt实现数据RSA加密 */
import JSEncrypt from "jsencrypt"; // 处理长文本数据时报错 jsencrypt.js Message too long for RSA
/* 产引入encryptlong实现数据RSA加密 */
import Encrypt from "encryptlong"; // encryptlong是基于jsencrypt扩展的长文本分段加解密功能。

//加密，公钥加密
export function encrypt(data, publicKey) {
  var encryptor = new Encrypt();
  encryptor.setPublicKey(publicKey);
  // 如果是对象/数组的话，需要先JSON.stringify转换成字符串
  const result = encryptor.encryptLong(data);
  return result;
}
export function decrypt(data, publicKey) {
  var encryptor = new Encrypt();
  encryptor.setPrivateKey(publicKey);
  // 如果是对象/数组的话，需要先JSON.stringify转换成字符串
  var result = encryptor.decryptLong(data);
  return result;
}
