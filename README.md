## 说明文档
### 接入方式

#### 在项目中设置资源库地址，在项目跟目录的build.gradle配置


```
allprojects {
    repositories {
        maven { url 'https://gitee.com/liu-huiliang/jarlibs/raw/master' }
    }
}

```

####  在项目中配置引用

```
dependencies {
    implementation 'com.lhl.login:login:1.0.0'
}
```

#### 在项目中使用google登录
```
App.getApp().googleLogin
```

#### 在项目中使用facebook登录
```
App.getApp().facebookLogin
```

#### 在项目中使用google支付

```
App.getApp().googlePay
```

##### Google支付参数说明

| 参数名            | 参数类型   | 参数说明      |
|----------------|--------|-----------|
| num            | int    | 购买数量，暂时无用 |
| goods          | string | 商品id，更具   |
|  price  | float  | 支付金额      |
| currency | string | 币种        |
| passThrough | string | 透传参数      |


### 回调说明

#### 登录回调说明
```
onLoginSucceed 登录成功回调
onLoginFailure 登录失败回调
```
#### user信息
| 字段名 | 字段类型 | 字段说明 |
| --- | --- | --- |
 | id | string | 用户id |
 | token | string | 用户登录凭证 |
 | email | string  | 用户邮箱暂时没有接入 |

#### 支付回调说明

