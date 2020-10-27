<div><img width="2000" src ="https://user-images.githubusercontent.com/72478198/97257461-53adff00-1859-11eb-815c-6927a4c5a47c.PNG"></div>
<div><img width="2000" src ="https://user-images.githubusercontent.com/72478198/97257310-dd110180-1858-11eb-89da-de2e9cb5d748.PNG">
</div>


# MainActivity클래스


# RequirePerms()메소드

## Description 

 - sms 권한을 얻어옵니다.

## Parameter

- 없음
    
## Return 

 - type : void
 
 - value : 없음

## Dependence function

- ActivityCompat.requestPermissions() 
  - Permission을 요청합니다
  - Parameter는 요청 코드를 넣습니다.
  - https://developer.android.com/training/permissions/requesting?hl=ko
- ContextCompat.checkSelfPermission()
  - 앱에 특정권한이 부여됬는지 확인합니다.
  - 앱에 권한이 있는지에 따라 PERMISSION_GRANTED 또는 PERMISSION_DENIED를 반환합니다.
  - https://developer.android.com/training/permissions/requesting?hl=ko

## Source code 
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

# sendSms()메소드

## Description 

 - 문자 기반 sms를 보냅니다.

## Parameter

- String PhoneNumber
  - 메세지를 보낼 전화번호에 해당합니다.

- String message
  - 메세지의 내용에 해당합니다.
  
## Return 

 - type : void
 
 - value : 없음

## Dependence function

- Smsmanager.getDefalut() 
  - SMS작업을 관리하는 SmsManager 개체를 가져옵니다.
  - https://developer.android.com/reference/android/telephony/gsm/SmsManager
  
- smsmanger.sendTextMessage()
  - 문자 기반 SMS를 보냅니다
  - https://developer.android.com/reference/android/telephony/gsm/SmsManager

## Source code 
```
 private void sendSMS(String phoneNumber,String message){
        SmsManager smsManager= SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber,null,message,null,null);

    }
```

# onNewIntent()메소드

- Activity가 이미 생성되어있으면 새로생긴 Intent는 OnNewIntent메소드에 전달된다
- https://developer.android.com/reference/android/app/Activity#onNewIntent(android.content.Intent)
- https://cishome.tistory.com/68

## Description 

 - activity

## Parameter

- 없음
    
## Return 

 - type : void
 
 - value : 없음

## Dependence function

- ShowSMS() 

## Source code 

```
 @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ShowSMS(intent);
    }
```

# ShowSMS메소드

## Description 

 - 받아온 메세지 값을 토스트 메세지로 띄움

## Parameter

- Intent intent
    
## Return 

 - type : void
 
 - value : 없음

## Dependence function

- intent.getStringExtra()
  - 인텐트에서 문자열을 가져오기위해 사용하는 메소드
  - https://developer.android.com/reference/android/app/SearchManager#QUERY

## Source code 

```
public void ShowSMS(Intent intent) {

        if (intent != null) {
            String  sender = intent.getStringExtra("sender");
            String content = intent.getStringExtra("content");


            tv_sender = sender;
            tv_content = content;

            Toast.makeText(getApplicationContext(),tv_content,Toast.LENGTH_SHORT).show()

        }

    }
```


# MyReceiver클래스

# Manifest


## Description 

 - MyReceiver서브 클래스에 대하여 인텐트 필터를 추가하여 어느 유형의 인텐트를 수락하는지 지정합니다.

## Source code 

```
 <receiver
            android:name=".MyReceiver"
            android:enabled="true"
            android:exported="true">

            <intent-filter>
                <action android:name="android.provider.Telephony.SMS_RECEIVED"/>
            </intent-filter>
        </receiver>
```

# OnReceive 메소드

## Description 

 - 이 메서드는 BroadcastReceiver가 Intent를 수신 할 때 호출됩니다.
 - message배열에 메세지의 주소,내용,번호를 추출합니다. 
 - BroadcastReceiver란
   - Context에서 보낸 Broadcast intents를 수신하고 처리하는 기본 클래스
   - https://developer.android.com/reference/android/content/BroadcastReceiver

## Parameter

- Context context
- Intent intent
    
## Return 

 - type : void
 
 - value : 없음

## Dependence function

- sendToActivity()

- getExtras()
  - getExtras()를 이용해서 데이터를 받을 수 있다.
  - 인텐트에서 확장 된 데이터의 맵을 검색합니다.
  - https://developer.android.com/reference/android/content/Intent

- getOriginatingAddress()
  - 이 메소드는 실제 송신자 번호를 알려줍니다
  - https://developer.android.com/reference/android/telephony/SmsMessage#getOriginatingAddress()
  
- getMessageBody()
  - 메시지 본문이 존재하고 텍스트기반인 경우 문자열로 반환합니다.
  - https://developer.android.com/reference/android/telephony/SmsMessage#getMessageBody()
  
- getTimestampMillis()
  - currentTimeMillis () 형식으로 서비스 센터 타임 스탬프를 반환합니다.
  - https://developer.android.com/reference/android/telephony/SmsMessage

## Source code 
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

# SendToActivity()메소드

## Description 

 - 받아온 메시지 데이터를 기반으로 activity를 시작합니다.

## Parameter

- Context context
- String sender
- Date date
    
## Return 

 - type : void
 
 - value : 없음

## Dependence function

- addFlags()
  - Activity를 해당 Task에 띄웁니다.
  - https://iw90.tistory.com/85
- putExtra()
  - 인텐트에 확장 데이터를 추가합니다.
  - 지정된 문자열에 데이터를 넣습니다.
  - https://developer.android.com/reference/android/content/Intent#putExtra(java.lang.String,%20boolean)
  
- context.startActivity()
  - 새로운 activity를 시작합니다.
  - https://developer.android.com/reference/android/content/Context#startActivity(android.content.Intent)
## Source code 
```
private void sendToActivity(Context context, String sender, String content, Date date){
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                |Intent.FLAG_ACTIVITY_SINGLE_TOP
                |Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("sender", sender);
        intent.putExtra("content", content);
        intent.putExtra("date", format.format(date));
        context.startActivity(intent);
    }
```
# parseSmsMessage()

## Description 

 - SMS문자의 내용을 뽑아내는 코드이다.

## Parameter

- 없음
    
## Return 

 - type : SmsMessage[]
 
 - value : messages

## Dependence function

- createFromPdu() 
  - 원시 PDU에서 SmsMessage를 만듭니다.
  - 원시 pdu: 데이터를 전송하기 위한 전송단위
  - https://developer.android.com/reference/android/telephony/SmsMessage#createFromPdu(byte[],%20java.lang.String)
  - PDU란 데이터 통신에서 상위 계층이 전달한 데이터에 붙이는 제어정보를 뜻한다 
   - https://ko.wikipedia.org/wiki/%ED%94%84%EB%A1%9C%ED%86%A0%EC%BD%9C_%EB%8D%B0%EC%9D%B4%ED%84%B0_%EB%8B%A8%EC%9C%84

## Source code 

```
private SmsMessage[] parseSmsMessage(Bundle bundle){
        Object[] objs = (Object[])bundle.get("pdus");
        SmsMessage[] messages = new SmsMessage[objs.length];
        for (int i= 0; i<objs.length; i++){
            messages[i] = SmsMessage.createFromPdu((byte[])objs[i]);
        }return  messages;
    }
```

# MyReceiver클래스 추가 설명

```
public class MyReceiver extends BroadcastReceiver { ....
```
# BroadcastReceiver
  - 브로드캐스트는 시스템에서 발생하는 이벤트 입니다.
    - ex) 배터리 부족하거나 사진을 캡처했다고 알리는 상태표시줄
  - 브로드캐스트 메시지는 Intent 객체에서 래핑됩니다. putExtra()를 통해 인텐트에 추가 정보를 첨부하거나 할 수 있습니다.
    
## Description
   - 브로드캐스트를 상속받았고 onReceive(Context,Intent)를 구현합니다.
   - MyReceiver 클래스에서 받아온 메시지를 onReceive메소드로 Intent객체로 래핑합니다.

