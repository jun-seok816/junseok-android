# junseok-android

SmsBroadcast 어플에 대한 설명
=============

1. 어플에 퍼미션 추가
2. MyReceiver클래스에 대하여
3. 하이브리드 앱을 이용해 MyReciever클래스로 받은 메세지를 Html 페이지에 띄우기

*어플에 퍼미션 추가
```
<uses-permission android:name="android.permission.RECEIVE_SMS"/>
    <uses-permission android:name="android.permission.SEND_SMS"/>
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.INTERNET"/>
```
안드로이드 스튜디오 Manifest파일에 퍼미션을 추가합니다
퍼미션 추가후
```
 private void requirePerms(){
        String[] permissions={Manifest.permission.RECEIVE_SMS};
        int permissionCheck = ContextCompat.checkSelfPermission(this,Manifest.permission.RECEIVE_SMS);
        if(permissionCheck== PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,permissions,1);

        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);

    }
```
어플이 켜지자마자 requirePerms()메소드를 실행시켜 사용자에게 sms권한을 얻어오게합니다.


2.MyReceiver클래스에 대하여

앱의 Manifest에서 <receiver>요소를 지정합니다.

```    
    receiver
            android:name=".MyReceiver"
            android:enabled="true"
            android:exported="true">

            <intent-filter>
                <action android:name="android.provider.Telephony.SMS_RECEIVED"/>
            </intent-filter>
        </receiver>
```        

MyReceiver 서브클래스를 선언하고 onReceive(Context, Intent)를 구현합니다. 다음 예의 broadcast receiver는 브로드캐스트의 콘텐츠를 기록하고 표시합니다.

```
 @Override
    public void onReceive(Context context, Intent intent) {


        Log.d(TAG,"onReceive() called");

        Bundle bundle= intent.getExtras();
        SmsMessage[] messages = parseSmsMessage(bundle);

        if (messages.length>0){
            String sender= messages[0].getOriginatingAddress();
            String content= messages[0].getMessageBody().toString();
            

            Log.d(TAG,"sender"+sender);
            Log.d(TAG, "content:"+content);


            sendToActivity(context,sender,content);
        }

    }
```

받아온 메세지를 변수 sender content 에 초기화 시킵니다.
그리고 MainActivity의 ShosSMS()함수를 이용해 tv_sender,tv_content값을 초기화 시킵니다.

```
 public void ShowSMS(Intent intent) {

        if (intent != null) {
            String  sender = intent.getStringExtra("sender");
            String content = intent.getStringExtra("content");


            tv_sender = sender;
            tv_content = content;

            Toast.makeText(getApplicationContext(),tv_content,Toast.LENGTH_SHORT).show();

            SMS();



        }

    }
```

3. 하이브리드 앱을 이용해 MyReciever클래스로 받은 메세지를 Html 페이지에 띄우기

assets폴더 안에 index.html을 새로만들어 webView에 띄워줍니다.

*index.html

```
<!DOCTYPE html>
<head>
    <title></title>
</head>
<script src="index.js">


</script>
<body>
<center>

    <p><input type="button" value="SMS" onClick="clickBtn()"/></p>
    <h4 id="aa">SMS</h4>

</center>
</body>
</html>
```

*javascript

```

function clickBtn(){

var tv_content;
var tv_sender;


var e = document.getElementById('aa');
       var c=AndroidDevice.ShowSMS2();
       e.innerHTML=c;
    AndroidDevice.ShowSMS2 ();


}

```
*WebView에 index.html적용

```
public class MainActivity extends AppCompatActivity {


     WebView wv;
  


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        wv=findViewById(R.id.webview);
     
        WebSettings settings=wv.getSettings();
        settings.setJavaScriptEnabled(true);

        wv.setWebViewClient(new WebViewClient());
        wv.setWebChromeClient(new WebChromeClient());
        wv.loadUrl("file:///android_asset/index.html");

    }
```    
index.html에서 만들어준 <input type="button" value="SMS" onClick="clickBtn()"/>을 사용자가 누르면
js안에 있는 clickbtn()메소드에서 AndroidDevice.ShowSMS2();라는 문구를 이용하요 MainActivity의 ShowSMS2()를 실행합니다.

```
 @JavascriptInterface
    public void ShowSMS2 () {

        sendSMS("01039750810", "4567");


    }
```
sendSMS()메소드가 실행되면...

```
private void sendSMS(String phoneNumber,String message){
        SmsManager smsManager= SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber,null,message,null,null);

    }
@Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ShowSMS(intent);
    } 
```
SendSMS()의 파라미터인 전화번호와 인증번호를 받아와 ShowSMS()메소드에게 intent를 전달해
변수에 전화번호랑 인증번호를 넣어줍니다.

마지막으로 SendSMS()메소드에서 SMS()메소드를 실행시킵니다.

```
  public void SMS(){

        a = "전화번호" +tv_sender + "인증번호" + tv_content;
        wv.post(new Runnable() {
            @Override
            public void run() {
                    wv.loadUrl("javascript:(function() { "
                            + "document.getElementById('aa').innerHTML = '"+a+"'; "
                            + "})()");
            }
        });
    }
```
SMS()메소드는 안드로이드에서 js에있는 내부함수document.getElementById를 호출시켜 <p>태그의 값을 a = "전화번호" +tv_sender + "인증번호" + tv_content;로 바꿔주면서 어플리캐이션이 종료됩니다.


