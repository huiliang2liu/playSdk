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

### 回调说明

#### 登录回调说明
```
onLoginSucceed 登录成功回调
onLoginFailure 登录失败回调
```
#### user信息
| 字段名 | 字段类型 |
| --- | --- |
 | id | string |
 | token | string |
 | email | string  |

#### 支付回调说明

