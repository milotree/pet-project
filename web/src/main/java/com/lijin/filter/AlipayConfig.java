package com.lijin.filter;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {
	
//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	// 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
	public static String app_id = "2016102300746359";
	
	// 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCE6L7fubSmaXQmAvEyexID9DH8G7Eibo68hsNHbjv5YuAFNeiaZAzJzo8LKByITIP1LQe5bc+Jf5KTmjzU3tmcd83um969zGh1mXNRogSWbRIsoAG+ppZqSFGVHBTp3b2MZtCUm7FiXl0t/ijxwMbNmJlQLODo88J80F6IwJPPaC2wstzzX14We9IyW9XNV+Hxrn1eCHKrlvRof5YCDVzryrSgOuWskkBcOSBsuiHW+EYyaWxZgnJ/kYgcStlzCkahPrIyuilNg5dPDNBeJqZA/6pkAv0rxBU4bywma/Ra86xo3Ul2KqHzzxQNLXsxr0OzmKP2GqbzUyX5Wn5tosyxAgMBAAECggEAbenk4DQ9t1mCWmUYfvzUWzy5kZp9FNKFbFeuawY4xvcD+Isqf3O6N7KBDWLwpa9ReZLxzmGHvlgoKbTmFczt/b3+KhPxUtkWkhqDCcGJNbWh0BHLH5W40tBX145PpVBhjxj/6cYQHh+Xw49q4PLh9ej68RkMLtpXvsy7QTCxjFWT5ZKwIFfuLBUEv2aeUMFuC99omlO62LzYX3Wffv8gC6gTyfzgA+R7GMPZSfHRaIzvW8qDmOYRYBBv5VjRD4ZdtJGKzrudAbfQPlgjgo6CCEU7Lza3g6IAh2cU6yVDPUPWeIUuZSdcuKL4lmX3JlGxzOpebDcZs14SQJqvyDnJGQKBgQDCJlltEcw0oweIRIbE73C8hSJV1siJEEYw5D2NgeEhQFv3nDsEw8vHhQjWC2De/E/OjWAanQ5l/I1y/U0qhJY5nVuRAUmzLCajApS/aZ3iogDlwOAjbAWnXbxBUaS1yGu1aTGCznMMxgw1mBXWEGNpvCF1lfrbZys8mx90ulLifwKBgQCvP/2RYl0HcTqG4MoIhzhKcKZlPuxNa3pmceSvNzPSy+twdUYDC+pKL6nOFtE6y1MAkFRbwxO/GCKf1H/NXhlMdK04L3jtUwVyyCBaXQQn8WAJDwKWD4srdvYB3IrxgxIsY/0Ze8WEdTWJ1ean5YqAmL3C+yPDX65mDcR1lD5YzwKBgB8Qb8KJ1HiflTbyb/XWsd0YGDRP12pG1lRAULDxlQny+EQYFUFf6p0T5iQJUgibqlFMQZJAius6i49YhB5HdJnnWZ1L7XTjnZL7eL1fssluTmJ0h+hjAkyTLOnunEIRvVobaLabPcFPduikWuoTWTSzy067HpY4XaymzVOp9FUVAoGAdCPMP5UYYd5AEzLVsWErhOmy3FYe9A/ORiYkj5nd1k0d8qFFjK9QzWlY9yNX9cbZcv4zabyWUYYFLDb/m3JJAcoxIZMUaPMkITVPe5M5Nd9Y7afs5tXWPS/RSNqHmhZq7NA247SK6YVjvbzr50jPyPlwiM82nDvEsA1frSxZRTECgYA0eyTTN5I2Eqrrjn1nhcUnoHjM8uDozABlMBFouSIW6WXpON0qpVFEzP5L15aeyBjJq0+sAmlmI9lFakaMmpFGZZli2TeK8PrTAAYiiIC+L2JE0rpsZoLxUuxVTelGTLmcwC5vaUeM67YUbN3qeFA8LfCdu3eT5PoxeHPdQen1QQ==";
	
	// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnwuap6Gq07eNebnr54s7h2yFRfcP+X1bItsooigzfcm82V5ua+9cECNJzMFc5hW7SckkOaCykSTAD+ww+tx4ckjCpxVm7Azy5ha04xKKJRBh0HeV8aMgBJgaD6se+2siq22Qdre+WJR5K6qoOspdJHUyjLC28JjdCDY0jQU0xmIJ+6kHMmfcRvlbqTYSecmGHcNbF7xzeKctlq8l+LwwEoBVNuP+8HwS5tmuOoCDKlJw/wz1VYzoA6CM1gz7ULLtSvC/DZHoXp+dPaG32LDKmvNsZgPARKLWVMcRogPj8g6tgHUJD9nkAErwuO6OOYT3PPuU70gvLSO02miRNEDu7wIDAQAB";

	// 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = "http://47.100.60.48:8011/cart/payloading";

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String return_url = "http://47.100.60.48:8011/cart/payloading";

	// 签名方式
	public static String sign_type = "RSA2";
	
	// 字符编码格式
	public static String charset = "utf-8";
	
	// 支付宝网关
	public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";
	
	// 支付宝网关
	public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /** 
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

