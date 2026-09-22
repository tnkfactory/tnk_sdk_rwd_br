# Tnkfactory SDK Rwd

## 목차

1. [SDK 설정하기](#1-sdk-설정하기)

    * [라이브러리 등록](#라이브러리-등록)
    * [Manifest 설정하기](#manifest-설정하기)
        * [Application ID 설정하기](#application-id-설정하기)
        * [권한 설정](#권한-설정)
        * [Activity tag 추가하기](#activity-tag-추가하기)
    * [Proguard 사용](#proguard-사용)
    * [COPPA 설정](#coppa-설정)

2. [광고 목록 띄우기](#2-광고-목록-띄우기)
   * [유저 식별 값 설정](#유저-식별-값-설정)
   * [광고 목록 띄우기 (Activity)](#광고-목록-띄우기-activity)
   * [디자인 변경하기](#디자인-변경하기)
   * [포인트 아이콘과 단위 출력하기](#포인트-아이콘과-단위-출력하기)

3. [Publisher API](#3-publisher-api) 

   [포인트 조회 및 인출](#포인트-조회-및-인출)
   * [TnkSession.queryPoint()](#tnksessionquerypoint)
   * [TnkSession.purchaseItem()](#tnksessionpurchaseitem)
   * [TnkSession.withdrawPoints()](#tnksessionwithdrawpoints)
   * [TnkOfferwall.getEarnPoint()](#tnkofferwallgetearnpoint)

   [그밖의 기능들](#그밖의-기능들)
   * [TnkSession.queryPublishState()](#tnksessionquerypublishstate)
   * [TnkSession.queryAdvertiseCount()](#tnksessionqueryadvertisecount)
   * [TnkSession.enableLogging()](#tnksessionenablelogging)
   * [TnkSession.setAgreePrivacy()](#tnksessionsetagreeprivacy)
     
   [광고 상세화면 직접 호출](#광고-상세화면-직접-호출)
   * [TnkOfferwall.showTermsDialog()](#method---showtermsdialog)
   * [TnkOfferwall.adDetail()](#method---addetail)
   * [TnkOfferwall.showAdDetailDialog()](#method---showaddetaildialog)
   * [TnkOfferwall.adJoin()](#method---adjoin)
   * [TnkOfferwall.adAction()](#method---adaction)
   * [Push 프로세스 처리 및 샘플 코드](#push-프로세스-처리-및-샘플-코드)

   [이벤트 웹뷰 호출](#이벤트-웹뷰-호출)
   * [TnkOfferwall.openEventWebView()](#method---openeventwebview)
   * [TnkOfferwall.getEventLink()](#method---geteventlink)

4. [Callback URL](#callback-url)
   * [호출방식](#호출방식)
   * [Parameters](#parameters-12)
   * [리턴값처리](#리턴값-처리)
   * [Callback URL 구현 예시 (Java)](#callback-url-구현-예시-java)
   
5. [Analytics Report](#4-analytics-report)
   * [기본 설정](#기본-설정)
   * [TNK SDK 초기화](#tnk-sdk-초기화)
     * [Method](#method)
     * [Parameters](#parameters)
   * [사용 활동 분석](#사용-활동-분석)
     * [TnkSession.actionCompleted()](#tnksessionactioncompleted)
   * [구매 활동 분석](#구매-활동-분석)
     * [TnkSession.buyCompleted()](#tnksessionbuycompleted)
   * [사용자 정보 설정](#사용자-정보-설정)
6. [플레이스먼트 뷰](#6-플레이스먼트-뷰)
7. [TnkError](#tnkerror)


## 1. SDK 설정하기

### 라이브러리 등록
TNK SDK는 Maven Central에 배포되어 있습니다.

settings.gradle에 아래와 같이 mavenCentral()가 포함되어있는지 확인합니다.
```gradle
pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        
        // 경로를 추가해 주시기 바랍니다.
        maven { url "https://repository.tnkad.net:8443/repository/public/" }
    }
}
rootProject.name = "project_name"
include ':app'
```

만약 settings.gradle에 저 부분이 존재하지 않다면 최상위 Level(Project)의 build.gradle에 maven repository를 추가해주세요.
```gradle
repositories {
    mavenCentral()
    maven { url "https://repository.tnkad.net:8443/repository/public/" }
}
```

tnk 라이브러리를 사용하기 위해 아래의 코드를 App Module의 build.gradle 파일에 추가해주세요.
```gradle
dependencies {
    implementation 'com.tnkfactory:rwd:8.09.33'
}
```
### Manifest 설정하기

#### 권한 설정

아래와 같이 권한 사용을 추가합니다.
```xml
<!-- 인터넷 -->
<uses-permission android:name="android.permission.INTERNET" />
<!-- 동영상 광고 재생을 위한 wifi접근 -->
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<!-- 광고 아이디 획득 -->
<uses-permission android:name="com.google.android.gms.permission.AD_ID"/>
```

#### Application ID 설정하기

Tnk 사이트에서 앱 등록하면 상단에 App ID 가 나타납니다. 이를 AndroidMenifest.xml 파일의 application tag 안에 아래와 같이 설정합니다.
(*your-application-id-from-tnk-site* 부분을 실제 App ID 값으로 변경하세요.)


```xml
<application>

    <meta-data android:name="tnkad_app_id" android:value="your-application-id-from-tnk-site" />

</application>
```



#### Activity tag 추가하기

광고 목록을 띄우기 위한 Activity를 <activity/>로 아래와 같이 설정합니다. 

```xml
<activity android:name="com.tnkfactory.ad.AdWallActivity" android:exported="true" android:screenOrientation="portrait"/>
```

AndroidManifest.xml의 내용 예시 
```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.tnkfactory.tnkofferer">

    // 인터넷
    <uses-permission android:name="android.permission.INTERNET" />
    // 동영상 광고 재생을 위한 wifi접근
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    // 광고 아이디 획득
    <uses-permission android:name="com.google.android.gms.permission.AD_ID"/>
    // 기타 앱에서 사용하는 권한
    //...
    //...
    
    
    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">

        ...
        ...
        <activity android:name="com.tnkfactory.ad.AdWallActivity"
            android:theme="@style/Theme.AppCompat.DayNight.NoActionBar"
            android:exported="true"
            android:screenOrientation="portrait"/>
        ...
        ...
        <!-- App ID -->
        <meta-data
            android:name="tnkad_app_id"
            android:value="30408070-4051-9322-2239-15040708030f" />
        ...
        ...
    </application>
</manifest>
```
	

### Proguard 사용

Proguard를 사용하실 경우 Proguard 설정내에 아래 내용을 반드시 넣어주세요.

```
-keep class com.tnkfactory.** { *;}
```

### COPPA 설정

COPPA는 [미국 어린이 온라인 개인정보 보호법](https://www.ftc.gov/tips-advice/business-center/privacy-and-security/children's-privacy) 및 관련 법규입니다. 구글 에서는 앱이 13세 미만의 아동을 대상으로 서비스한다면 관련 법률을 준수하도록 하고 있습니다. 연령에 맞는 광고가 보일 수 있도록 아래의 옵션을 설정하시기 바랍니다.

```kotlin
val offerwall = TnkOfferwall(this)
offerwall.setCOPPA(true) // ON - 13세 미만 아동을 대상으로 한 서비스 일경우 사용
offerwall.setCOPPA(false) // OFF
```

## 2. 광고 목록 띄우기


```diff
- 주의 : 테스트 상태에서는 테스트하는 장비를 개발 장비로 등록하셔야 광고목록이 정상적으로 나타납니다.
```
링크 : [테스트 단말기 등록방법](https://github.com/tnkfactory/android-sdk-rwd/blob/master/reg_test_device.md)

다음과 같은 과정을 통해 광고 목록을 출력 하실 수 있습니다.

1) TNK SDK 초기화 

2) 유저 식별값 설정

3) COPPA 설정

4) 광고 목록 출력

광고 목록을 출력하는 Activity의 예제 소스

```kotlin
class MainActivity : AppCompatActivity() {

    private lateinit var offerwall: TnkOfferwall

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvPoint = findViewById<TextView>(R.id.tv_point)
        val btnOfferwall = findViewById<Button>(R.id.btn_offerwall)

        // 1) TNK SDK 초기화
        offerwall = TnkOfferwall(this)

        lifecycleScope.launch(Dispatchers.IO) {
            // 고유 아이디는 매체사에서 유저 식별을 위한 고유값을 사용하셔야 하며
            // 이 예제에서는 google adid를 사용 합니다.
            val adid = AdvertisingIdInfo.requestIdInfo(this@MainActivity) // background thread 처리 필요

            // 2) 유저 식별값 설정
            offerwall.setUserName(adid.id)
            // 3) COPPA 설정 (https://www.ftc.gov/business-guidance/privacy-security/childrens-privacy)
            offerwall.setCOPPA(false)
            // 4) 포인트 금액 앞에 아이콘, 뒤에 재화 단위 출력 여부를 설정합니다.
            TnkAdConfig.pointEffectType = TNK_POINT_EFFECT_TYPE.UNIT // 금액 뒤에 관리자 페이지에서 설정한 단위 출력
            TnkAdConfig.topScrollButtonConfig = 2 // 오퍼월 리스트 TOP 이동 아이콘 노출 옵션 (1 -> 일반 , 2 -> 쇼핑에만, 3 -> 모두노출)

            // 결과는 Main UI Thread 로 전달됩니다.
            offerwall.getEarnPoint { point ->
                tvPoint.text = "받을 수 있는 포인트 : $point p"
            }
        }

        // 5) 오퍼월 액티비티를 출력합니다.
        btnOfferwall.setOnClickListener {
            offerwall.startOfferwallActivity(this@MainActivity)
        }
    }
}
```


### 유저 식별 값 설정

앱이 실행되면 우선 앱 내에서 사용자를 식별하는 고유한 ID를 아래의 API를 사용하시어 Tnk SDK에 설정하시기 바랍니다. 

사용자 식별 값으로는 게임의 로그인 ID 등을 사용하시면 되며, 적당한 값이 없으신 경우에는 Device ID 값 등을 사용할 수 있습니다.

(유저 식별 값이 Device ID 나 전화번호, 이메일 등 개인 정보에 해당되는 경우에는 암호화하여 설정해주시기 바랍니다.) 

유저 식별 값을 설정하셔야 이후 사용자가 적립한 포인트를 개발사의 서버로 전달하는 callback 호출 시에 같이 전달받으실 수 있습니다.

##### Method

- void TnkOfferwall.setUserName(String userName)

##### Parameters

| 파라메터 명칭 | 내용                                                         |
| ------------- | ------------------------------------------------------------ |
| userName      | 앱에서 사용자를 식별하기 위하여 사용하는 고유 ID 값 (로그인 ID 등)  길이는 256 bytes 이하입니다. |

### 광고 목록 띄우기 (Activity)

자신의 앱에서 광고 목록을 띄우기 위하여 TnkOfferwall.startOfferwallActivity() 함수를 사용합니다. 멀티탭 광고목록을 보여주기 위하여 새로운 Activity를 띄웁니다.

##### Method

- TnkOfferwall.startOfferwallActivity(Activity activity)

##### Description

멀티탭 광고 목록 화면 (AdWallActivity)를 화면에 띄웁니다. 

반드시 Main UI Thread 상에서 호출하여야 합니다.

##### Parameters

| 파라메터 명칭 | 내용                                                         |
| ------------- | ------------------------------------------------------------ |
| activity      | 현재 Activity 객체                                           |

##### 적용예시

```kotlin

override fun onCreate(savedInstanceState: Bundle?) {

    val offerwall = TnkOfferwall(this)
    val button:Button = findViewById(R.id.btn_offerwall)
    
    button.setOnClickListener {
        offerwall.startOfferwallActivity(this@MainActivity)
    }
}
```

### 광고 목록 띄우기 (ViewGroup)

Activity나 Fragment내 특정 레이아웃에 광고 목록을 출력 할 경우 

getAdListView() 메소드를 호출하면 오퍼월 ViewGroup를 반환합니다.

##### Method

- TnkOfferwall.getAdListView()
- TnkOfferwall.getAdListView(long:appId)

##### Description

ViewGroup를 원하시는 곳에 addView메소드를 사용하여 화면을 구성 하실 수 있습니다.

##### Parameters

| 파라메터 명칭 | 내용 |
|---------|--|
| appid   | 광고 목록 출력과 동시에 특정 광고를 출력 할 경우 appid를 전달합니다. |

##### 적용예시

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {

    val offerwall = TnkOfferwall(this)
    val framelayout:FrameLayout = findViewById(R.id.layout_offerwall)
    
    showProgress()
    offerwall.load(object : TnkResultListener {
        override fun onSuccess() {
            dismissProgress()
            framelayout.addView(offerwall.getAdListView())
        }

        override fun onFail(error: TnkError) {
            MaterialAlertDialogBuilder(this@EmbedActivityB)
                .setMessage(error.message)
                .create()
                .show()
        }

    })
}
```

### 디자인 변경하기

광고 리스트 화면(AdListView)는 기본 스타일을 그대로 사용하셔도 충분하지만, 원하시는 경우 매체앱과 통일감 있도록 UI를 변경하실 수 있습니다.

#### 포인트 아이콘과 단위 출력하기

유저에게 제공하는 리워드 금액 앞에 P아이콘이 출력 되는 것이 기본 설정입니다.

관리자 페이지에서 설정하신 텍스트가 출력되기를 원하실 경우 아래와 같이 설정 하실 수 있습니다.
```kotlin
/**
 * 포인트 아이콘, 재화 표기 옵션
 * 1. 모두 출력 ICON_N_UNIT
 * 2. 출력 안함 NONE
 * 3. 아이콘 ICON
 * 4. 재화 단위 텍스트 UNIT
 */
TnkAdConfig.pointEffectType = TNK_POINT_EFFECT_TYPE.ICON_N_UNIT
```

그 외 상세한 디자인 변경을 원하실 경우 다음 링크를 참고 하시기 바랍니다.

[디자인 커스텀 가이드](https://github.com/tnkfactory/tnk_sdk_rwd_br/blob/main/ui_customizing.md)


## 3. Publisher API

### 포인트 조회 및 인출

사용자가 광고참여를 통하여 획득한 포인트는 Tnk서버에서 관리되거나 앱의 자체서버에서 관리될 수 있습니다.

포인트가 Tnk 서버에서 관리되는 경우에는 다음의 포인트 조회 및 인출 API를 사용하시어 필요한 아이템 구매 기능을 구현하실 수 있습니다.

#### TnkSession.queryPoint()

Tnk서버에 적립되어 있는 사용자 포인트 값을 조회합니다.

동기 방식과 비동기 방식 2가지 호출 방식을 제공하고 있으며 화면 멈춤 현상이 없도록 구현하기 위해서는 비동기 방식을 사용할 것을 권장합니다.

다만 Main UI Thread 가 아닌 별도 Thread를 생성하여 호출하는 경우 (주로 게임 앱)에는 비동기 방식을 사용할 수 없으므로 별도 Thread를 생성하여 동기 방식으로 호출하셔야 합니다.

##### [비동기로 호출하기]

###### Method

- void TnkSession.queryPoint(Context context, ServiceCallback callback)

###### Description

Tnk 서버에 적립되어 있는 사용자 포인트 값을 조회합니다. 비동기 방식으로 호출되며 결과를 받으면 callback이 호출됩니다. Main UI Thread 상에서만 호출이 가능합니다.
ServiceCallback의 사용법은 아래 적용예시를 참고해주세요.

###### Parameters

| 파라메터 명칭 | 내용                                                         |
| -------------- | ----------------------------------------------------------- |
| context       | 현재 Activity 또는 Context 객체                              |
| callback      | 서버에서 결과가 오면 callback 객체의 OnReturn(Context context, Object result) 메소드가 호출됩니다. 메소드 호출은 Main UI Thread 상에서 이루어 집니다. 전달된 result 객체는 Integer 객체이며 사용자 포인트가 담겨 있습니다. |

###### 적용예시

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // ...

    val pointView = findViewById<TextView>(R.id.main_point)

    TnkSession.queryPoint(this, object : ServiceCallback() {
        override fun onReturn(context: Context?, result: Any?) {
            val point = result as Int
            pointView.text = point.toString()
        }
    })

    // ...
}
```

##### [동기방식으로 호출하기]

###### Method

- int TnkSession.queryPoint(Context context)

###### Description

Tnk 서버에 적립되어 있는 사용자 포인트 값을 조회하여 그 결과를 int 값으로 반환합니다.

###### Parameters

| 파라메터 명칭 | 내용                            |
| ------------- | ------------------------------- |
| context       | 현재 Activity 또는 Context 객체 |

###### Return : int

- 서버에 적립되어 있는 포인트 값

###### 적용예시

```kotlin
// import kotlin.concurrent.thread

fun getPoint() {
    thread {
        val point = TnkSession.queryPoint(this@MainActivity)
        showPoint(point) // 결과를 받아서 필요한 로직을 수행한다. (별도 Thread 에서 실행됩니다)
    }
}
```

#### TnkSession.purchaseItem()

TnK 서버에서는 별도로 아이템 목록을 관리하는 기능을 제공하지는 않습니다.
다만 게시앱에서 제공하는 아이템을 사용자가 구매할 때 Tnk 서버에 해당 포인트 만큼을 차감 할 수 있습니다. 이 API 역시 비동기 방식과 동기 방식을 모두 제공합니다.

##### [비동기로 호출하기]

###### Method

- void TnkSession.purchaseItem(Context context, int pointCost, String itemId, ServiceCallback callback)

###### Description

Tnk 서버에 적립되어 있는 사용자 포인트를 차감합니다. 차감내역은 Tnk사이트의 보고서 페이지에서 조회하실 수 있습니다.

###### Parameters

| 파라메터 명칭 | 내용                                                         |
| -------------- | ----------------------------------------------------------- |
| context       | 현재 Activity 또는 Context 객체                              |
| pointCost     | 차감할 포인트                                                |
| itemId        | 구매할 아이템의 고유 ID (게시앱에서 정하여 부여한 ID) Tnk 사이트의 보고서 페이지에서 함께 보여줍니다. |
| callback      | 서버에서 결과가 오면 callback 객체의 OnReturn(Context context, Object result) 메소드가 호출됩니다. 메소드 호출은 Main UI Thread 상에서 이루어 집니다. 전달된 result 객체는 long[] 객체이며 long[0] 값은 차감 후 남은 포인트 값이며, long[1] 값은 고유한 거래 ID 값이 담겨 있습니다. long[1] 값이 음수 인경우에는 포인트 부족 등으로 오류가 발생한 경우입니다. |

###### 적용예시

```kotlin
button.setOnClickListener {
    TnkSession.purchaseItem(this@MainActivity, 30, "item.00001", object : ServiceCallback() {
        override fun onReturn(context: Context?, result: Any?) {
            val ret = result as LongArray

            if (ret[1] < 0) {
                // error
            } else {
                Log.d("tnkad", "current point = ${ret[0]}, transaction id = ${ret[1]}")
                pointView.text = ret[0].toString()
            }
        }
    })
}
```

##### [동기방식으로 호출하기]

###### Method

- long[] TnkSession.purchaseItem(Context context, int pointCost, String itemId)

###### Description

Tnk 서버에 적립되어 있는 사용자 포인트를 차감하고 그 결과를 long[] 로 반환합니다. 차감내역은 Tnk사이트의 보고서 페이지에서 조회하실 수 있습니다.

###### Parameters

| 파라메터 명칭 | 내용                                                         |
| ------------- | ------------------------------------------------------------ |
| context       | 현재 Activity 또는 Context 객체                              |
| pointCost     | 차감할 포인트                                                |
| itemId        | 구매할 아이템의 고유 ID (게시앱에서 정하여 부여한 ID) Tnk 사이트의 보고서 페이지에서 함께 보여줍니다. |

###### Return : long[]

- long[0] 은 포인트 차감후 남은 포인트 값입니다.
- long[1] 은 고유한 거래 번호 값입니다. 이 값이 음수 인 경우에는 오류가 발생한 것입니다. (포인트 부족 등)

#### TnkSession.withdrawPoints()

Tnk 서버에서 관리되는 사용자 포인트 전체를 한번에 인출하는 기능입니다.

##### [비동기로 호출하기]

###### Method

- void TnkSession.withdrawPoints(Context context, String desc, ServiceCallback callback)

###### Description

Tnk 서버에 적립되어 있는 사용자의 모든 포인트를 차감합니다. 차감내역은 Tnk사이트의 보고서 페이지에서 조회하실 수 있습니다.

###### Parameters

| 파라메터 명칭 | 내용                                                         |
| -------------- | ----------------------------------------------------------- |
| context       | 현재 Activity 또는 Context 객체                              |
| desc          | 인출과 관련된 설명 등을 넣어줍니다. Tnk 사이트의 보고서 페이지에서 함께 보여줍니다. |
| callback      | 서버에서 결과가 오면 callback 객체의 OnReturn(Context context, Object result) 메소드가 호출됩니다. 메소드 호출은 Main UI Thread 상에서 이루어 집니다. 전달된 result 객체는 Integer 객체이며 인출된 포인트 값입니다. 해당 사용자에게 충전된 포인트가 없는 경우에는 0이 반환됩니다. |

###### 적용예시

```kotlin
button.setOnClickListener {
    TnkSession.withdrawPoints(this@MainActivity, "user_delete", object : ServiceCallback() {
        override fun onReturn(context: Context?, result: Any?) {
            val point = result as Int
            Log.d("tnkad", "withdraw point = $point")

            pointView.text = point.toString()
        }
    })
}
```

##### [동기방식으로 호출하기]

###### Method

- int TnkSession.withdrawPoints(Context context, String desc)

###### Description

Tnk 서버에 적립되어 있는 사용자의 모든 포인트를 차감하고 차감된 포인트 값을 반환합니다. 차감내역은 Tnk사이트의 보고서 페이지에서 조회하실 수 있습니다.

###### Parameters

| 파라메터 명칭 | 내용                                                         |
| ------------- | ------------------------------------------------------------ |
| context       | 현재 Activity 또는 Context 객체                              |
| desc          | 인출과 관련된 설명 등을 넣어줍니다. Tnk 사이트의 보고서 페이지에서 함께 보여줍니다. |

###### Return : int

- 인출된 포인트 값, 사용자에게 인출할 포인트가 없으면 0이 반환됩니다.

#### TnkOfferwall.getEarnPoint()

Tnk서버에서 사용자가 참여 가능한 모든 광고의 적립 가능한 총 포인트 값을 조회합니다.

###### Method

- fun getEarnPoint(listener: (Long) -> Unit)
- suspend fun getEarnPoint()
- fun getEarnPointSync(): Long

###### Description

Tnk서버에서 사용자가 참여 가능한 모든 광고의 적립 가능한 총 포인트 값을 조회하여 그 결과를 long 값으로 반환합니다.

###### Return : Long

- 참여 가능한 광고의 적립 가능한 총 포인트 값
```kotlin
// 방법 1 : suspend 함수 (내부에서 IO Thread 로 전환하므로 Main 에서 launch 하면 됩니다)
lifecycleScope.launch {
    val point = tnkOfferwall.getEarnPoint()
    tvPoint.text = point.toString()
}

// 방법 2 : 콜백 (결과는 Main UI Thread 로 전달됩니다)
tnkOfferwall.getEarnPoint {
    tvPoint.text = it.toString()
}

// 방법 3 : 동기 호출 (Main UI Thread 에서 호출하면 안 됩니다)
thread {
    val point = tnkOfferwall.getEarnPointSync()
    runOnUiThread {
        tvPoint.text = "포인트 : $point"
    }
}
```

### 그밖의 기능들

#### TnkSession.queryPublishState()

Tnk 사이트의 [게시정보]에서 광고 게시 중지를 하게 되면 이후에는 사용자가 광고 목록 창을 띄워도 광고들이 나타나지 않습니다.
그러므로 향후 광고 게시를 중지할 경우를 대비하여 화면에 충전소 버튼 자체를 보이지 않게 하는 기능을 갖추는 것이 바람직합니다.
이를 위하여 현재 게시앱의 광고게시 상태를 조회하는 기능을 제공합니다.

##### Method

- void TnkSession.queryPublishState(Context context, ServiceCallback callback)

##### Parameters

| 파라메터 명칭 | 내용                                                         |
| -------------- | ----------------------------------------------------------- |
| context       | 현재 Activity 또는 Context 객체                              |
| callback      | 서버에서 결과가 오면 callback 객체의 OnReturn(Context context, Object result) 메소드가 호출됩니다. 메소드 호출은 Main UI Thread 상에서 이루어 집니다. 전달된 result 객체는 Integer 객체이며 상태코드가 담겨 있습니다. 상태코드 값이 TnkSession.STATE_YES 인 경우(실제 값은 1)는 광고게시상태를 의미합니다. |

##### state code
| state code | 상태 |
|------|------|
|`TnkSession.STATE_NO` = 0 | 등록전|
|`TnkSession.STATE_YES` = 1 | 판매중|
|`TnkSession.STATE_TEST` = 2 | 테스트 중|
|`TnkSession.STATE_CHECK` = 3 | 검증 중|
|`TnkSession.STATE_PASSED` = 4 | 검증 완료|
|`TnkSession.STATE_STOP` = 8 | 임시로 중지됨|
|`TnkSession.STATE_ERROR` = 9 | 잔액 부족등 에러로 중지됨|
|`TnkSession.STATE_UNKNOWN` = 99| 없는 코드 값|
    
```kotlin
TnkSession.STATE_NO       // 0  등록전
TnkSession.STATE_YES      // 1  판매중
TnkSession.STATE_TEST     // 2  테스트 중
TnkSession.STATE_CHECK    // 3  검증 중
TnkSession.STATE_PASSED   // 4  검증 완료
TnkSession.STATE_STOP     // 8  임시로 중지됨
TnkSession.STATE_ERROR    // 9  잔액 부족등 에러로 중지됨
TnkSession.STATE_UNKNOWN  // 99 알수 없는 코드 값
```

##### 적용예시

```kotlin
val button = findViewById<Button>(R.id.main_ad)

// ... 

TnkSession.queryPublishState(this, object : ServiceCallback() {
	override fun onReturn(p0: Context?, p1: Any?) {
		(p1 as? Int)?.let {
			when (it) {
				1 -> {
					// 정상
					button.visibility = View.VISIBLE
				}
				else -> {
					TAlertDialog.show(this@LotteryActivity, "서버와 연결이 원활하지 않습니다.", { finish() }, null)
				}
			}
		}
	}

	override fun onError(p0: Context?, p1: Throwable?) {
		super.onError(p0, p1)
		TAlertDialog.show(this@LotteryActivity, "서버와 연결이 원활하지 않습니다.\n" + p1?.message, { finish() }, null)
	}
})
```

#### TnkSession.queryAdvertiseCount()

광고 게시 상태를 확인하여 충전소 버튼을 보이게하거나 안보이게 하는 것으로도 좋지만 실제적으로 현재 적립 가능한 광고가 있는지 여부를 판단해서 버튼을 노출하는 것이 보다 바람직합니다.
이를 위하여 현재 적립가능한 광고 정보를 확인하는 기능을 아래와 같이 제공합니다.

##### Method

- void TnkSession.queryAdvertiseCount(Context context, ServiceCallback callback)

##### Parameters

| 파라메터 명칭 | 내용                                                         |
| -------------- | ----------------------------------------------------------- |
| context       | 현재 Activity 또는 Context 객체                              |
| callback      | 서버에서 결과가 오면 callback 객체의 OnReturn(Context context, Object result) 메소드가 호출됩니다. 메소드 호출은 Main UI Thread 상에서 이루어 집니다. 전달된 result 객체는 int[] 객체이며 int[0]는 광고 건수, int[1] 에는 적립가능한 포인트 합계가 담겨 있습니다. 만약 현재 광고 게시상태가 아니라면 int[0]에는 0이 담겨있습니다. |

#### TnkSession.enableLogging()

Tnk의 SDK에서 생성하는 로그를 출력할지 여부를 결정합니다. 테스트 시에는 true로 설정하시고 Release 빌드시에는 false로 설정해주시기 바랍니다.

##### Method

- void TnkSession.enableLogging(boolean enable)

#### TnkSession.setAgreePrivacy()

개인정보 수집동의 여부를 설정합니다. true 설정시 오퍼월에서 개인정보 수집동의 팝업이 뜨지 않습니다. 다시 해당 팝업창을 띄우고 싶은 경우 false로 설정해주시기 바랍니다.

##### Method

- void TnkSession.setAgreePrivacy(Activity activity, boolean isAgree)
- boolean TnkSession.getAgreePrivacy(Activity activity)

## 광고 상세화면 직접 호출

> Push Notification 을 지원하는 매체는 푸시 동작으로 광고 상세화면을 띄우는데 아래 함수들이 사용됩니다.
> SDK 초기화(Application ID 설정)와 setUserName() 이 완료된 후 광고 ID 를 통해 광고 상세 화면을 직접 호출할 수 있습니다.

아래 함수들은 모두 `TnkOfferwall` 인스턴스의 메소드입니다. 개인정보 수집동의(약관) 팝업을 띄워야 하므로
`TnkOfferwall` 객체는 **`FragmentActivity` 를 상속받는 Activity** 에서 생성하여야 합니다.
(`AppCompatActivity` 는 `FragmentActivity` 를 상속하므로 그대로 사용하시면 됩니다.)

```kotlin
val offerwall = TnkOfferwall(this)   // this : FragmentActivity
```

공통 파라메터인 `actionId` 는 광고 종류를 구분하는 값입니다.
**쇼핑형(CPS) 광고인 경우에는 5 로 설정**하여야 하며, 일반 광고인 경우에는 0 이거나 생략할 수 있습니다.

#### Method - showTermsDialog

##### Method

- TnkOfferwall.showTermsDialog(context:Context, onCallback:(Boolean)->Unit)

##### Description

개인정보 수집동의 팝업을 노출합니다.

* 이미 수집동의를 수락한 경우 onCallback 으로 true 를 바로 반환합니다.
* 동의되지 않은 경우에는 수집동의 여부를 묻는 팝업을 노출합니다. 확인 / 취소 여부에 따라 onCallback 결과를 반환합니다.
* `TnkOfferwall` 을 `FragmentActivity` 가 아닌 Context 로 생성한 경우에는 팝업을 띄울 수 없으므로 false 를 반환합니다.

##### Parameters

| 파라메터 명칭 | 내용                                                         |
| ------------- | ------------------------------------------------------------ |
| context       | 현재 Activity 또는 Context 객체                              |
| onCallback    | 개인정보 수집동의 결과 (true : 동의됨 / false : 취소 또는 팝업 표시 불가) |

#### Method - adDetail

##### Method

- TnkOfferwall.adDetail(context:Context, appId:Long, actionId:Int = 0, onCallback:(Boolean, TnkError?)->Unit)

##### Description

광고 ID(appId)를 입력받아 직접 상세 화면을 호출합니다.
개인정보 수집동의가 되어있지 않은 경우 **약관 동의 팝업을 먼저 노출**하고, 동의한 경우에만 상세 화면을 띄웁니다.
사용자가 약관 동의를 취소하면 `TnkError` 코드 1001 (`USER_CANCELED`), `FragmentActivity` 가 아니어서 팝업을 띄울 수 없는 경우에는 1002 (`NO_TNK_CONTEXT`) 가 전달됩니다.

##### Parameters

| 파라메터 명칭 | 내용                                                         |
| ------------- | ------------------------------------------------------------ |
| context       | 현재 Activity 또는 Context 객체                              |
| appId         | 광고마다 가지고 있는 광고의 고유 아이디 입니다.              |
| actionId      | 쇼핑형(CPS) 광고인 경우 5 로 설정합니다. (일반 광고인 경우 생략 가능) |
| onCallback    | 광고 호출 성공 여부와 에러 정보를 전달합니다.                |

#### Method - showAdDetailDialog

##### Method

- TnkOfferwall.showAdDetailDialog(context:Context, appId:Long, actionId:Int = 0, onCallback:(Boolean, TnkError?)->Unit)

##### Description

**약관 동의 확인 절차 없이** 광고 상세 화면을 바로 호출합니다.
이미 개인정보 수집동의를 받은 것이 확실한 경우에 사용하시고, 그렇지 않은 경우에는 `showTermsDialog()` 로 동의를 먼저 확인하거나 `adDetail()` 을 사용하시기 바랍니다.

##### Parameters

| 파라메터 명칭 | 내용                                                         |
| ------------- | ------------------------------------------------------------ |
| context       | 현재 Activity 또는 Context 객체                              |
| appId         | 광고마다 가지고 있는 광고의 고유 아이디 입니다.              |
| actionId      | 쇼핑형(CPS) 광고인 경우 5 로 설정합니다. (일반 광고인 경우 생략 가능) |
| onCallback    | 광고 호출 성공 여부와 에러 정보를 전달합니다.                |

##### 샘플코드 

```kotlin
offerwall.showAdDetailDialog(this@MainActivity, 123123) { success, error ->
            if (success) {
                // 광고 상세 출력 성공
                FirebaseAnalytics.event(EVENT_ON_SCREEN, "Tnk Ad detail dialog shown successfully")
            } else {
                // 이미 참여한 광고 또는 종료된 광고(push받은 후 시간이 지나거나, 광고가 종료된 경우 등)
                FirebaseAnalytics.event(EVENT_ON_SCREEN, "Tnk Ad show detail error")

                TAlertDialog.show(
                    context = this@MainActivity,
                    message = "${error?.code} - ${error?.message}",
                    onConfirm = {
                        // 팝업에서 확인 눌렀을 경우 액션
                        FirebaseAnalytics.event(EVENT_ON_SCREEN, "Tnk Ad show detail error dlg closed")
                    },
                    null
                )
            }
        }
```

#### Method - adJoin

##### Method

- TnkOfferwall.adJoin(context:Context, appId:Long, actionId:Int = 0, onCallback:(Boolean, TnkError?)->Unit)

##### Description

광고 ID(appId)를 입력받아 상세 화면 없이 해당 광고의 참여 액션을 바로 수행합니다. (설치 조회, 광고 참여 웹페이지 이동 등)
`adDetail()` 과 마찬가지로 개인정보 수집동의가 되어있지 않은 경우 약관 동의 팝업을 먼저 노출합니다.

##### Parameters

| 파라메터 명칭 | 내용                                                         |
| ------------- | ------------------------------------------------------------ |
| context       | 현재 Activity 또는 Context 객체                              |
| appId         | 광고마다 가지고 있는 광고의 고유 아이디 입니다.              |
| actionId      | 쇼핑형(CPS) 광고인 경우 5 로 설정합니다. (일반 광고인 경우 생략 가능) |
| onCallback    | 광고 참여 성공 여부와 에러 정보를 전달합니다.                |

#### Method - adAction

##### Method

- TnkOfferwall.adAction(context:Context, appId:Long, actionId:Int = 0, onCallback:(Boolean, TnkError?)->Unit)

##### Description

광고의 상세화면 노출 여부 설정값에 따라 **상세 화면 호출 또는 즉시 참여로 자동 분기**합니다.
상세 화면이 없는 광고는 `adJoin()` 과 동일하게 바로 참여 처리되고, 그 외에는 `adDetail()` 과 동일하게 상세 화면이 노출됩니다.
푸시에서 전달받은 광고를 광고 종류에 상관없이 하나의 호출로 처리하고 싶은 경우에 사용하시기 바랍니다.

##### Parameters

| 파라메터 명칭 | 내용                                                         |
| ------------- | ------------------------------------------------------------ |
| context       | 현재 Activity 또는 Context 객체                              |
| appId         | 광고마다 가지고 있는 광고의 고유 아이디 입니다.              |
| actionId      | 쇼핑형(CPS) 광고인 경우 5 로 설정합니다. (일반 광고인 경우 생략 가능) |
| onCallback    | 광고 호출 성공 여부와 에러 정보를 전달합니다.                |

### Push 프로세스 처리 및 샘플 코드

1) 푸시에서 수신한 데이터에서 CPS 여부, 광고 ID 2가지를 발췌합니다.
2) 개인정보 수집 여부를 체크합니다.
3) 개인정보 수집에 동의한 경우 발췌한 데이터에 따라 상세 화면을 호출합니다.

```kotlin
// TnkOfferwall 은 FragmentActivity 를 상속받는 Activity 에서 생성합니다.
val offerwall = TnkOfferwall(this@MainActivity)

// 약관 동의 상태 체크 함수
offerwall.showTermsDialog(this@MainActivity) { agreed ->
    if (agreed) {
        // 약관 동의한 상태 or 팝업에서 약관 동의 한 경우
        // 푸시 수신시 광고아이템 종류(AD or CPS)
        // 광고 아이디
        val adAppId = 12345L
        val isCps = false
        // CPS일경우에는 actionId를 5, 그외에는 0으로 설정해야합니다.
        val actionId = if (isCps) 5 else 0

        offerwall.showAdDetailDialog(this@MainActivity, adAppId, actionId) { success, error ->
            if (!success) {
                // 이미 참여한 광고 또는 종료된 광고
                Log.d("TNK", "${error?.code} - ${error?.message}")
            }
        }
    } else {
        // 동의 취소를 한 경우
        // Do nothing
    }
}
```



## 이벤트 웹뷰 호출

이벤트 아이디를 알고 있는 경우 오퍼월을 거치지 않고 이벤트 페이지를 직접 띄울 수 있습니다.
푸시나 배너에서 특정 이벤트로 바로 랜딩시킬 때 사용합니다.

#### Method - openEventWebView

##### Method

- TnkOfferwall.openEventWebView(context:Context, eventId:Long, useFullScreen:Boolean? = null, onResult:(Boolean, String)->Unit)

##### Description

이벤트 아이디로 이벤트 페이지를 엽니다.
서버에서 내려주는 이벤트 설정에 따라 아래 세 가지 중 하나로 자동 분기합니다.

| 서버 설정 | 노출 방식 |
| --------- | --------- |
| 전용 웹뷰 지정 | 제휴사 전용 웹뷰 |
| 인앱 웹뷰 사용 | 광고 상세 인앱 웹뷰 |
| 그 외 | 외부 브라우저 |

`useFullScreen` 은 **인앱 웹뷰로 분기한 경우에만** 적용됩니다.
전용 웹뷰나 외부 브라우저로 분기하면 값에 관계없이 무시됩니다.

인앱 웹뷰로 분기하는 경우 `context` 는 `FragmentActivity` 를 상속받는 Activity 여야 합니다.

호출 스레드는 가리지 않으며, `onResult` 는 Main UI Thread 에서 호출됩니다.
인앱 웹뷰로 분기한 경우 호출 시점의 화면 상태에 따라 아래와 같이 동작합니다. (8.09.33 부터)

| 상황 | 동작 | onResult |
| ---- | ---- | -------- |
| 화면이 떠 있음 | 즉시 표시 | `true` |
| 이벤트 조회 중 앱이 백그라운드로 전환됨 | 표시를 보류했다가 앱 복귀 시 표시 | `true` |
| 이벤트 조회 중 화면이 종료됨 | 표시하지 않음 | `false` |
| `context` 가 `FragmentActivity` 가 아님 | 표시하지 않음 | `false` |

`onResult` 의 `true` 는 "표시했거나, 복귀 시 표시가 예약되었다"는 뜻입니다.

##### Parameters

| 파라메터 명칭 | 내용                                                         |
| ------------- | ------------------------------------------------------------ |
| context       | 현재 Activity 객체. 인앱 웹뷰로 분기하는 경우 `FragmentActivity` 를 상속받아야 합니다. |
| eventId       | 이벤트마다 가지고 있는 고유 아이디 입니다.                   |
| useFullScreen | 인앱 웹뷰의 전체화면 여부입니다. 생략하거나 `null` 이면 서버 설정을 따르고, `true` / `false` 를 주면 그 값으로 강제합니다. |
| onResult      | 성공 여부와 실패 시 에러 메시지를 전달합니다.                |

##### 샘플코드

```kotlin
val offerwall = TnkOfferwall(this)   // this : FragmentActivity

// 서버 설정을 따릅니다.
offerwall.openEventWebView(this@MainActivity, 782047L) { success, message ->
    if (!success) {
        Log.d("TNK", "openEventWebView failed : $message")
    }
}

// 전체화면으로 강제합니다.
offerwall.openEventWebView(this@MainActivity, 782047L, useFullScreen = true) { success, message ->
    if (!success) {
        Log.d("TNK", "openEventWebView failed : $message")
    }
}
```

#### Method - getEventLink

##### Method

- TnkOfferwall.getEventLink(eventId:Long, onResult:(TnkOffRepository.EventLinkVo?)->Unit)
- TnkOfferwall.getEventLink(eventId:Long, onResult:(TnkOffRepository.EventLinkVo?, TnkError?)->Unit)  (8.09.33 부터)

##### Description

이벤트 페이지를 직접 띄우지 않고 **랜딩 정보만** 조회합니다.
화면 전환을 매체앱에서 직접 처리하려는 경우에 사용하시기 바랍니다.
조회에 실패하면 `null` 이 전달됩니다. 실패 사유가 필요한 경우 `TnkError` 를 함께 받는 형태를 사용하시기 바랍니다.

조회는 SDK 내부에서 IO Thread 로 처리하고 `onResult` 는 Main UI Thread 에서 호출되므로, 호출 스레드를 가리지 않습니다.
(8.09.32 이하에서는 Main UI Thread 에서 호출하면 항상 `null` 이 전달되었습니다.)

콜백으로 전달되는 `TnkOffRepository.EventLinkVo` 의 `mkt_app_id` 에 랜딩 URL 이 들어 있습니다.
(`import com.tnkfactory.ad.off.TnkOffRepository` 가 필요합니다.)

##### Parameters

| 파라메터 명칭 | 내용                                                         |
| ------------- | ------------------------------------------------------------ |
| eventId       | 이벤트마다 가지고 있는 고유 아이디 입니다.                   |
| onResult      | 이벤트 랜딩 정보를 전달합니다. 실패 시 `null` 입니다. `TnkError` 를 받는 형태는 성공 시 `error` 가 `null` 입니다. |

##### 샘플코드

```kotlin
offerwall.getEventLink(782047L) { eventLink ->
    if (eventLink == null) {
        Log.d("TNK", "getEventLink failed")
        return@getEventLink
    }
    val landingUrl = eventLink.mkt_app_id
    // 매체앱에서 직접 화면 전환 처리
}

// 실패 사유가 필요한 경우 (8.09.33 부터)
offerwall.getEventLink(782047L) { eventLink, error ->
    if (error != null) {
        Log.w("TNK", "getEventLink failed : ${error.code} ${error.message}")
        return@getEventLink
    }
    val landingUrl = eventLink?.mkt_app_id
    // 매체앱에서 직접 화면 전환 처리
}
```


## 4. Callback URL

사용자가 광고참여를 통하여 획득한 포인트를 개발사의 서버에서 관리하실 경우 다음과 같이 진행합니다.

* 매체 정보 설정 화면에서 아래와 같이 '포인트 관리' 항목을 '자체서버에서 관리'로 선택합니다.
* URL 항목에 포인트 적립 정보를 받을 URL을 입력합니다.

이후에는 사용자에게 포인트가 적립될 때 마다 실시간으로 위 URL로 적립 정보를 받을 수 있습니다.

![RedStyle_08](https://github.com/tnkfactory/android-sdk-rwd/blob/master/img/callback_setting.jpg)

##### 호출방식

HTTP POST

##### Parameters

| 파라메터   | 상세 내용                                                    | 최대길이 |
| ---------- | ------------------------------------------------------------ |---- |
| seq_id     | 포인트 지급에 대한 고유한 ID 값이다. URL이 반복적으로 호출되더라도 이 값을 사용하여 중복지급여부를 확인할 수 있다. | string(50) |
| pay_pnt    | 사용자에게 지급되어야 할 포인트 값이다.                      | long |
| md_user_nm | 게시앱에서 사용자 식별을 하기 위하여 전달되는 값이다. 이 값을 받기 위해서는 매체앱내에서 setUserName() API를 사용하여 사용자 식별 값을 설정하여야 한다. | string(256) |
| md_chk     | 전달된 값이 유효한지 여부를 판단하기 위하여 제공된다. 이 값은 app_key + md_user_nm + seq_id 의 MD5 Hash 값이다. app_key 값은 앱 등록시 부여된 값으로 Tnk 사이트에서 확인할 수 있다. | string(32) |
| app_id     | 사용자가 참여한 광고앱의 고유 ID 값이다.                     | long |
| pay_dt     | 포인트 지급시각이다. (System milliseconds) 예) 1577343412017 | long |
| app_nm     | 참여한 광고명 이다.                                          |  string(120) |
|pay\_amt|정산되는 금액.|long|
|actn\_id|<p>- 0 : 설치형</p><p>- 1 : 실행형</p><p>- 2 : 액션형</p><p>- 4 : 클릭형</p><p>- 5 : 구매형</p><p>- 6 : 이벤트</p>|int|

##### 리턴값 처리

Tnk 서버에서는 위 URL을 호출하고 HTTP 리턴코드로 200이 리턴되면 정상적으로 처리되었다고 판단합니다.
만약 200이 아닌 값이 리턴된다면 Tnk 서버는 비정상처리로 판단하고 이후에는 5분 단위 및 1시간 단위로 최대 24시간 동안 반복적으로 호출합니다.

* 중요! 동일한 Request가 반복적으로 호출될 수 있으므로 seq_id 값을 사용하시어 반드시 중복체크를 하셔야합니다.



##### Callback URL 구현 예시 (Java)

```java
// 해당 사용자에게 지급되는 포인트

int payPoint = Integer.parseInt(request.getParameter("pay_pnt"));

// tnk 내부에서 생성한 고유 번호로 이 거래에 대한 Id이다.

String seqId = request.getParameter("seq_id");

// 전달된 파라메터가 유효한지 여부를 판단하기 위하여 사용한다. (아래 코딩 참고)

String checkCode = request.getParameter("md_chk");

// 게시앱에서 사용자 구분을 위하여 사용하는 값(전화번호나 로그인 ID 등)을 앱에서 TnkOfferwall.setUserName()으로 설정한 후 받도록한다.

String mdUserName = request.getParameter("md_user_nm");

// 앱 등록시 부여된 app_key (tnk 사이트에서 확인가능)

String appKey = "d2bbd...........19c86c8b021";

// 유효성을 검증하기 위하여 아래와 같이 verifyCode를 생성한다. DigestUtils는 Apache의 commons-codec.jar 이 필요하다. 다른 md5 해시함수가 있다면 그것을 사용해도 무방하다.

String verifyCode = DigestUtils.md5Hex(appKey + mdUserName + seqId);

// 생성한 verifyCode와 chk_cd 파라메터 값이 일치하지 않으면 잘못된 요청이다.

if (checkCode == null || !checkCode.equals(verifyCode)) {

    // 오류

    log.error("tnkad() check error : " + verifyCode + " != " + checkCode);

} else {

    log.debug("tnkad() : " + mdUserName + ", " + seqId);


    // 포인트 부여하는 로직수행 (예시)

    purchaseManager.getPointByAd(mdUserName, payPoint, seqId);

}
```



## 4. Analytics Report

Analytics 적용을 위해서는 Tnk 사이트에서 앱 등록 및 프로젝트 상의 SDK 관련 설정이 우선 선행되어야합니다.

[[SDK 설정하기](#1-sdk-설정하기)]의 내용을 우선 확인해주세요.

google anlytics등 매체사에서 사용하고 계신 환경에 맞추어 로깅 하는 방법도 존재합니다.
자세한 내용은 아래 링크의 내용을 참고 하시기 바랍니다.
[직접 구현](https://github.com/tnkfactory/tnk_sdk_rwd_br/blob/main/tnk_ad_analytics.md)

### 기본 설정

AndroidMenifest.xml 파일 내에 Tnk 앱 등록세 발급 받은 App ID를 설정하시고 그 아래에 아래와 같이 tnkad_tracking 값을 true로 설정합니다.

이후 더 이상 tracking을 원하지 않을 경우에는 false로 설정하시기 바랍니다.

```xml
<application>
    ...
    <meta-data android:name="tnkad_app_id"  android:value="your-app-id-from-tnk-sites" />
    <meta-data android:name="tnkad_tracking" android:value="true" />
    ...
</application>
```

앱의 유입경로 파악을 위해서는 Google Install Referrer 라이브러리가 필요합니다. 아래와 같이 build.gradle (Module: app) 안에 라이브러리를 설정합니다.

\- GooglePlay에서 다운받은 앱에 대해서만 유입 경로 파악 기능이 제공됩니다.

```gradle
dependencies {
    ...
    implementation 'com.android.installreferrer:installreferrer:2.2'
    ...
}
```

SDK가 요구하는 permission들을 추가합니다.

\- 앱의 유입 경로 기능을 사용하기 위해서는 BIND_GET_INSTALL_REFERRER_SERVICE 권한은 필수입니다.

```xml
<!-- 인터넷 -->
<uses-permission android:name="android.permission.INTERNET" />
<!-- 와이파이 상태 -->
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<!-- 설치된 앱의 식별값 확인 -->
<uses-permission android:name="com.google.android.finsky.permission.BIND_GET_INSTALL_REFERRER_SERVICE" />
<!-- 광고 아이디 획득 -->
<uses-permission android:name="com.google.android.gms.permission.AD_ID"/>
```

전체적인 AndroidMenifest 파일의 모습은 아래와 같습니다.

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android" package="com.myapplication" >

    <uses-permission android:name="android.permission.INTERNET" /> 
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="com.google.android.finsky.permission.BIND_GET_INSTALL_REFERRER_SERVICE" />
    <uses-permission android:name="com.google.android.gms.permission.AD_ID"/>


    <application android:icon="@drawable/ic_launcher" android:label="@string/app_name"> 

        ... your activities ...

 

        <meta-data android:name="tnkad_app_id" android:value="your-app-id-from-tnk-sites" />

        <meta-data android:name="tnkad_tracking" android:value="true" />    

    </application>

</manifest> 
```

### TNK SDK 초기화

앱이 실행되는 시점에 TnkSession.applicationStarted()를 호출합니다.

#### TnkSession.applicationStarted()

##### Method

- void TnkSession.applicationStarted(Context context)

##### Description

앱이 실행되는 시점에 호출합니다. 다른 API 보다 가장 먼저 호출되어야 합니다.

##### Parameters

| 파라메터 명칭 | 내용         |
| ------------- | ------------ |
| context       | Context 객체 |

##### 적용 예시

```kotlin
TnkSession.applicationStarted(context)
```

### 사용 활동 분석

사용자가 앱을 설치하고 처음 실행했을 때 어떤 행동을 취하는지 분석하고자 할 때 아래의 API를 사용합니다.

예를 들어 로그인, 아이템 구매, 친구 추천 등의 행동이 이루어 질때 해당 행동에 대한 구분자와 함께 호출해주시면 사용자가 어떤 패턴으로 앱을 이용하는지 또는 어떤 단계에서 많이 이탈하는지 등의 분석이 가능해집니다.

#### TnkSession.actionCompleted()

##### Method

- void TnkSession.actionCompleted(Context context, String actionName)

##### Description

사용자의 특정 액션 발생시 호출합니다.

동일 액션에 대해서는 최초 발생시에만 데이터가 수집됩니다.

##### Parameters

| 파라메터 명칭 | 내용                                                         |
| -------------------- | ------------------------------------------------------------ |
| context       | Context 객체                                                 |
| actionName    | 사용자 액션을 구별하기 위한 문자열 (예를 들어 "user_login" 등) 사용하시는 actionName 들은 모두 Tnk 사이트의 분석보고서 화면에서 등록되어야 합니다. |

##### 적용예시

```kotlin
// 추가 데이터 다운로드 완료시
TnkSession.actionCompleted(this, "resource_loaded")

// 회원 가입 완료시
TnkSession.actionCompleted(this, "signup_completed")

// 프로필 작성 완료시
TnkSession.actionCompleted(this, "profile_entered")

// 친구 추천시
TnkSession.actionCompleted(this, "friend_invite")
```

### 구매 활동 분석

사용자가 유료 구매 등의 활동을 하는 경우 이에 대한 분석데이터를 얻고자 할 경우에는 아래의 API를 사용합니다.

구매활동 분석 API 적용시에는 유입경로별로 구매횟수와 구매 사용자 수 파악이 가능하며, 하루 사용자 중에서 몇명의 유저가 구매 활동을 하였는 지 또 사용자가 앱을 처음 실행한 후 얼마정도가 지나야 구매활동을 하는지 등의 데이터 분석이 가능합니다. 분석 보고서에서 제공하는 데이터에 각 아이템별 가격을 대입시키면 ARPU 및 ARPPU 값도 산출하실 수 있습니다.

#### TnkSession.buyCompleted()

##### Method

- void TnkSession.buyCompleted(Context context, String itemName)

##### Description

사용자가 유료 구매를 완료하였을 때 호출합니다.

##### Parameters

| 파라메터 명칭 | 내용                                                        |
| ------------- | ----------------------------------------------------------- |
| context       | Context 객체                                                |
| itemName      | 구매한 item을 구별하기 위한 문자열 (예를 들어 "item_01" 등) |

##### 적용예시

```kotlin
// item_01 구매 완료시
TnkSession.buyCompleted(this, "item_01")

// item_02 구매 완료시
TnkSession.buyCompleted(this, "item_02")
```

### 사용자 정보 설정

사용자의 성별 및 나이 정보를 설정하시면 보고서에서 해당 내용이 반영되어 추가적인 데이터를 확인하실 수 있습니다.

```kotlin
val offerwall = TnkOfferwall(this)

// 나이 설정
offerwall.setUserAge(this, 23)

// 성별 설정 (남)
offerwall.setUserGender(this, "M")

// 성별 설정 (여)
offerwall.setUserGender(this, "F")
```

## 6. 플레이스먼트 뷰

[플레이스먼트 뷰 가이드](./AdPlacementView.md)

## TnkError


| 코드 | 내용                       | 메세지 |
|------|--------------------------|--|
| 0    | 정상                       | | 
| 1    | 존재하지 않거나 게시중이 아닌 매체앱     | 포인트를 받을 수 있는 매체앱이 아닙니다 |
| 2    | 존재하지 않거나 광고중이 아닌 광고앱     | 종료되었거나 적립이 완료된 광고입니다 |
| 3    | 잘못되었거나 발생할 수 없는 상태, 잘못된 액션 | 종료되었거나 적립이 완료된 광고입니다 |
| 4    | 이미 지급되었음                 | 이미 지급된 항목입니다. 다른 곳에서 적립받은 경우 또는 이미 실행하신 이력이 있을 수 있습니다. |
| 5    | 참여이력 없음                  | 참여하지 않은 광고입니다. 처음부터 다시 진행해주세요. |
| 6    | 참여할 수 없는 기기임             | 지원하지 않는 기기입니다. |
| 9    | 존재하지 않은 앱, 시스템 오류        | 종료되었거나 적립이 완료된 광고입니다. |
| 10   | 지급 조건이 만족되지 않음           | 적립되지 않았습니다. 적립조건을 확인해주세요. |
| 11   | 출석일이 아직 안됨               | 출석일이 아직 지나지 않았습니다. {left_hour}시간 후 다시 시도해주세요. |
| 12   | 일일 구매 제한 초과              | 오늘 구매 가능 횟수가 초과됐습니다. 내일 다시 구매가 가능합니다 |
| 17   | 잘못된 placement id         |  잘못된 placement id 입니다. |
| 99   | 네트워크 단절, 시스템 오동작 등       | 시스템 또는 네트워크 오류입니다. 잠시후 다시 시도해주세요. |
| 1001 | 사용자 취소 (약관 동의 팝업에서 취소 등) | |
| 1002 | `FragmentActivity` 가 아니어서 팝업을 띄울 수 없음 | |
| 1003 | 광고를 찾을 수 없음 | |
| 1004 | 중복 호출 | |

<br/>
