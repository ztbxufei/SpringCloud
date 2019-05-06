
项目中需要用到公私钥实现数字签名、验签，通过下面命令生成的：
进入opensll安装目录bin文件夹下打开opensll.exe

1. #生成rsa私钥，X509编码，1024位
openssl genrsa -out rsa_private_key_1024.pem 1024 
2. #转换为PKCS#8编码
openssl pkcs8 -in rsa_private_key_1024.pem -out rsa_private_key_1024_pkcs8.pem -nocrypt -topk8 
3. #导出对应的公钥，X509编码
openssl rsa -in rsa_private_key_1024.pem -out rsa_public_key_1024.pem -pubout 

1生成1024位的rsa私钥，默认是X509编码，这一步生成的私钥文件只供第2、3步使用，并没有实际用处；
2使用第1步生成的私钥文件生成PKCS#8编码的私钥文件，这一步生成的文件为最终使用的私钥文件；
3使用第1步生成的私钥文件生成对应的公钥文件，这一步生成的公钥文件为最终使用的公钥文件；
通过Java代码读取rsa_private_key_1024_pkcs8.pem私钥文件生成数字签名，使用rsa_public_key_1024.pem公钥文件验证数字签名；
