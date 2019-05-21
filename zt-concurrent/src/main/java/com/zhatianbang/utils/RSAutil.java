package com.zhatianbang.utils;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by lenovo on 2019/5/5.
 */
public class RSAutil {
    public static final String SIGN_ALGORITHMS= "SHA1WithRSA";
    private static final String ALGORITHM   = "RSA";
    private static final int MAX_ENCRYPT_BLOCK = 117;
    private static final int MAX_DECRYPT_BLOCK = 128;

    public static String encrypt(String content,String charset, String aliPubKey) {
          try {
              byte[] keyBytes = Base64.decode(aliPubKey);
              X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
              KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
              Key pubKey = keyFactory.generatePublic(x509KeySpec);

              // 对数据加密
              Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
              cipher.init(Cipher.ENCRYPT_MODE, pubKey);

              byte[] data = content.getBytes(charset);
              byte[] splitData = splitData(data, cipher,MAX_ENCRYPT_BLOCK);
              return Base64.encode(splitData);

          } catch (Exception e) {
              throw new RuntimeException(e);
          }
                      
    }


    public static String decrypt(String content,String charset, String myPriKey){
          try {
              byte[] keyBytes = Base64.decode(myPriKey);
              PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
              KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
              Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
              Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
              cipher.init(Cipher.DECRYPT_MODE, privateK);
              byte[] data = Base64.decode(content);
              byte[] splitData = splitData(data, cipher,MAX_DECRYPT_BLOCK);
              return new String(splitData,charset);
          } catch (Exception e) {
              throw new RuntimeException(e);
          }
    }
          

    private static byte[] splitData(byte[] data, Cipher cipher,int maxLen) throws IllegalBlockSizeException,BadPaddingException, IOException {
          int totalLen = data.length;
          int offSet = 0 ;
          int index = 0;
          byte[] cache;

          ByteArrayOutputStream out = new ByteArrayOutputStream();
          // 对数据分段解密
          while (totalLen - offSet > 0) {
              if (totalLen - offSet > maxLen) {
                          cache = cipher.doFinal(data, offSet, maxLen);
              } else {
                          cache = cipher.doFinal(data, offSet, totalLen - offSet);
              }
              out.write(cache, 0, cache.length);
              index++;
              offSet = index * maxLen;
          }
          byte[] decryptedData = out.toByteArray();
          out.close();
          return decryptedData;

    }


    /**
     * 私钥加签
     * @param content 加签参数
     * @param charset 编码
     * @param myPriKey 私钥
     * @return
     */
    public static String sign(String content, String charset, String myPriKey) {
        try {
              byte[] decodePubKey = Base64.decode(myPriKey);
              PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodePubKey);
              KeyFactory factory = KeyFactory.getInstance(ALGORITHM);
              PrivateKey privateKey = factory.generatePrivate(keySpec);

              Signature signature = Signature.getInstance(SIGN_ALGORITHMS);

              signature.initSign(privateKey);
              signature.update(content.getBytes(charset));

              byte[] signed = signature.sign();

              return Base64.encode(signed);

        } catch (Exception e) {
              throw new RuntimeException(e);
        }

    }


    /**
     *
     * @param content
     * @param charset
     * @param aliPubKey
     * @param aliSign
     * @return
     */
    public static boolean verify(String content, String charset, String aliPubKey,String aliSign) {
          try {
                  KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
                  byte[] decodePubKey = Base64.decode(aliPubKey);

                  PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(decodePubKey));

                  Signature signature = Signature.getInstance(SIGN_ALGORITHMS);

                  signature.initVerify(pubKey);
                  signature.update(content.getBytes(charset));

                  return signature.verify(Base64.decode(aliSign));
          } catch (Exception e) {
                  throw new RuntimeException(e);
          }
    }
          
                  
      public static void main(String[] args){
//             String priKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAK96qVmq30Pznu/SGI5Ha3r0ZSDtjvrtLruDb8x6JYbkRyDPztdQeaZ0/pLeKCBCYn/gNXOPNjwKWW7tAWnKMa/VPnYsVZs0dfEZwgaQCHDQ6Rwf7G3hxFKX5R5dMJglQQkYzVhlUKqXQeWEFTB8Pb77S9Qms12V7RCUNHj6z/jrAgMBAAECgYBycQfDL9od4eNUumtDVza0YHEXsXZfFZI1vnmKHFsfBv+QWZam+5fqxEBGnlYC++hrR1j0vYB8Va+P1Ufnq3MNBX7ewYPqgcuRQFPmfF3vC+WQoi5I4exTkgH6vQJ4L+C9rz0RntUct3uC+7rpUizSfEepeH7+PifROHGbTVnt4QJBAP4MUD8P6AfGxr/JjJy7xq+1cyEm1r/JqB82biC4Vf/2gTduSfN37rXMBJQVbhNW2YvHfMLnwWgEOYO/mShgPJsCQQCw08+dalT6d5+5PV6VHG2ZpG2xrLWWovzZkoDH7G5JZANU1vHEa5zbcIh+f1fD2kWzBLqsAj4SBZHdgqVVi/HxAkEAxDtCqNNiiM8zqeTG5jHtEL/5g/v6GP5tDF8AxeldFG+V8/kOyi8zhd1nE/NRqUKGm3+lkO9u5yeMPYX7icJ8TQJARt59RR/Ksx+iekFoWOat3ngSJrWLibYFFMYsqvPNN8jHtQb4mrpugVxWuCwdZrdiW983WZ0Ed4CX92lXDjQqYQJAUmWpj0uuN2ilZoR5evQG2mOSP+4CGeKJhGqtlkWOv5jgxVZjYZeHTCxsCAL0lslSe95kgwCrRLvFqQeBVL/SVA==";
//             String pubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCveqlZqt9D857v0hiOR2t69GUg7Y767S67g2/MeiWG5Ecgz87XUHmmdP6S3iggQmJ/4DVzjzY8Cllu7QFpyjGv1T52LFWbNHXxGcIGkAhw0OkcH+xt4cRSl+UeXTCYJUEJGM1YZVCql0HlhBUwfD2++0vUJrNdle0QlDR4+s/46wIDAQAB";
             String priKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJenD0ebzAjRq51S" +
                     "Q8OeTGQsbgY16sVMWoHvCnspKuASrjcuoVDvD0+WjFfIGR1eXnrtjIc1nVJlGJ/S" +
                     "JWxnUbMjQ79W8WOjz84IYlamDuGLDrXpqvNjsbtwpLVy8uPVuwojktxMLzVnIKa6" +
                     "3BSsWWGIT4ujx3CRSLTnnmPZ8SGVAgMBAAECgYBo4TSEBY9aGgdhyd7rjrKuJLwC" +
                     "t48h2+hMLBFHk/T++mxZ5XnTC0G/fRKaMtyLnxQgV+D0MXQX46rf/Om8yseI16yL" +
                     "wmrXSboRvwLWk0I4JHKbUbBa3pyAryNLWE7oUwn2Aa9ml7RbXeqrvbTZ/McXsjUp" +
                     "zQc8GIYYeQ1dj9engQJBAMg5+Dzwn2qyhKPqxJMiVLm9eK81pCFiyJ0K9uJizwd0" +
                     "0ozWQ6cNsEp3ZWQeQh0JlYQNwJISvn+tfLL0cJLKVd0CQQDB5VDmxS3umGvACGHN" +
                     "vXhJyihZ0aqKRTFAs6T4HarViUod8nqObldNp1NSFYHZTByTVTkbUxaEow28ZDyK" +
                     "M0sZAkAGwUXpsGiAbgNhkFMPb7ISDh1bM+EUq0N/YNZRbunzI3LTA4uGwER5Tqvi" +
                     "A89RMXiU2u3rP6yjnBO/6dU+bosVAkA1m/mqVIpGlc304lxoTiKcBFDzxIyqfkcH" +
                     "5KOQFmPNdpAPh6NrDgcFaWYYI+oq8mhUNBP+AhMPySVbGTvD2jrBAkBBT51RA0UY" +
                     "5M3zDM8MAMv3wMVFmANCqgrfLfBE/Nmu5U/pJ0exjkV47x0XO8WwX5dPPGNKj7ne" +
                     "MYO0ObcJLx2c";
             String pubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCXpw9Hm8wI0audUkPDnkxkLG4G" +
                     "NerFTFqB7wp7KSrgEq43LqFQ7w9PloxXyBkdXl567YyHNZ1SZRif0iVsZ1GzI0O/" +
                     "VvFjo8/OCGJWpg7hiw616arzY7G7cKS1cvLj1bsKI5LcTC81ZyCmutwUrFlhiE+L" +
                     "o8dwkUi0555j2fEhlQIDAQAB";
             String charset = "utf-8";
             String content = "RSA公钥加密算法是1977年由罗纳德·李维斯特（Ron Rivest）、阿迪·萨莫尔（Adi Shamir）和伦纳德·阿德曼（Leonard ";

          String sign = sign(content,charset,priKey);
          boolean result = verify(content,charset,pubKey,sign);

          String encrptyContent = encrypt(content,charset,pubKey);
          String realContent = decrypt(encrptyContent,charset,priKey);

          System.out.println(String.format("sign=%s result=%s realContent=%s",sign,result,realContent));
      }




}
