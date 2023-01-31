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
        
        // 정식 출시 이전이기 때문에 아래와 같이 경로를 추가해 주시기 바랍니다.
        maven { url "https://jitpack.io" }
        maven { url 'https://s01.oss.sonatype.org/service/local/repositories/comtnkfactory-1139/content/' }
    }
}
rootProject.name = "project_name"
include ':app'
```

만약 settings.gradle에 저 부분이 존재하지 않다면 최상위 Level(Project)의 build.gradle에 maven repository를 추가해주세요.
```gradle
repositories {
    mavenCentral()
}
```

아래의 코드를 App Module의 build.gradle 파일에 추가해주세요.
```gradle
dependencies {
    implementation 'com.tnkfactory:rwd:8.00.3'
}
```
### Manifest 설정하기

#### 권한 설정

아래와 같이 권한 사용을 추가합니다.
```xml
// 인터넷
<uses-permission android:name="android.permission.INTERNET" />
// 동영상 광고 재생을 위한 wifi접근
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
// 광고 아이디 획득
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

광고 목록을 띄우기 위한 Activity 2개를 <activity/>로 아래와 같이 설정합니다. 매체앱인 경우에만 설정하시면 됩니다. 광고만 진행하실 경우에는 설정하실 필요가 없습니다.


```xml
<activity android:name="com.tnkfactory.ad.AdWallActivity" android:exported="true"/>
<activity android:name="com.tnkfactory.ad.AdMediaActivity" android:screenOrientation="portrait" android:exported="true"/>

<!-- 또는 아래와 같이 설정-->
<activity android:name="com.tnkfactory.ad.AdMediaActivity" android:screenOrientation="sensorLandscape" android:exported="true"/>
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
        <activity android:name="com.tnkfactory.ad.AdWallActivity" android:exported="true"/>
        <activity android:name="com.tnkfactory.ad.AdMediaActivity" android:screenOrientation="fullSensor" android:exported="true"/>
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

```java
public class MainActivity extends AppCompatActivity {


    lateinit var offerwall:TnkOfferwall 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TnkOfferwall(this).apply {

            lifecycleScope.launch(Dispatchers.IO) {
                // google adid가져오기 (백그라운드 스래드에서 처리해야한다.)
                val adid = AdvertisingIdInfo.requestIdInfo(this@MainActivity)

                // 유저 고유 식별값 설정
                setUserName(adid.id)
                // COPPA 설정(https://www.ftc.gov/business-guidance/privacy-security/childrens-privacy)
                setCOPPA(false)
            }
        }

        btnShowAd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 새 액티비티로 호출
                AdWallActivity.start(this@MainActivity)
            }
        });

    }
}
```

## 3. Publisher API

게시앱(Publisher)을 위한 가이드입니다.

### 가. 광고 목록 출력

#### 유저 식별 값 설정

앱이 실행되면 우선 앱 내에서 사용자를 식별하는 고유한 ID를 아래의 API를 사용하시어 Tnk SDK에 설정하시기 바랍니다. 

사용자 식별 값으로는 게임의 로그인 ID 등을 사용하시면 되며, 적당한 값이 없으신 경우에는 Device ID 값 등을 사용할 수 있습니다.

(유저 식별 값이 Device ID 나 전화번호, 이메일 등 개인 정보에 해당되는 경우에는 암호화하여 설정해주시기 바랍니다.) 

유저 식별 값을 설정하셔야 이후 사용자가 적립한 포인트를 개발사의 서버로 전달하는 callback 호출 시에  같이 전달받으실 수 있습니다.

##### Method

- void TnkSession.setUserName(Context context, String userName)

##### Parameters

| 파라메터 명칭 | 내용                                                         |
| ------------- | ------------------------------------------------------------ |
| context       | 현재 Activity 또는 Context 객체                              |
| userName      | 앱에서 사용자를 식별하기 위하여 사용하는 고유 ID 값 (로그인 ID 등)  길이는 256 bytes 이하입니다. |

#### 광고 목록 띄우기 (Activity)

자신의 앱에서 광고 목록을 띄우기 위하여 TnkSession.showAdListByType() 함수를 사용합니다. 멀티탭 광고목록을 보여주기 위하여 새로운 Activity를 띄웁니다.

##### Method

- void TnkSession.showAdListByType(Activity activity, AdListType... adListType)
- void TnkSession.showAdListByType(Activity activity, String title, AdListType... adListType)
- void TnkSession.showAdListByType(Activity activity, String title, TnkLayout userLayout, AdListType... adListType)
- void TnkSession.showAdListByType(Activity activity, TnkLayout userLayout, AdListType... adListType)

##### Description

멀티탭 광고 목록 화면 (AdWallActivity)를 화면에 띄웁니다. 

반드시 Main UI Thread 상에서 호출하여야 합니다.

##### Parameters

| 파라메터 명칭 | 내용                                                         |
| ------------- | ------------------------------------------------------------ |
| activity      | 현재 Activity 객체                                           |
| title         | 광고 리스트의 타이틀을 지정함  (기본값 : 무료 포인트 받기)   |
|adListType | 광고 리스트의 타입 (ALL : 보상형과 구매형 모두 표시, PPI : 보상형, CPS : 구매형) |
| userLayout    | 원하는 Layout을 지정할 수 있습니다. 자세한 내용은  [[디자인 변경하기](#라-디자인-변경하기)] 내용을 참고해주세요. |

##### 적용예시

```java
@Override

public void onCreate(Bundle savedInstanceState) {

    ...

    final Button button = (Button)findViewById(R.id.main_ad);

    button.setOnClickListener(new OnClickListener() {

        @Override

        public void onClick(View v) {
            TnkSession.showAdListByType(MainActivity.this,
                                  "Your title here",
                                  AdListType.ALL,
                                  AdListType.PPI,
                                  AdListType.CPS      
                                 );
        }

    });
}
```
